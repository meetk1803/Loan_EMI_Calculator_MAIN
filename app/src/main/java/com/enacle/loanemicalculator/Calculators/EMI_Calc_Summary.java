package com.enacle.loanemicalculator.Calculators;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.enacle.loanemicalculator.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class EMI_Calc_Summary extends AppCompatActivity {
    TextInputEditText edt_principal_amt, edt_rate, edt_year;
    TextView tv_years, tv_months;
    int monthOrYear = 1;
    TextInputEditText edt_monthly_emi, edt_t_interest, edt_t_payment;
    ExtendedFloatingActionButton btn_business_reset, btn_business_calculate,btn_amortization;
    public int totalMonths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emi_calc_summary);

        edt_principal_amt = findViewById(R.id.edt_principal_amt);
        edt_rate = findViewById(R.id.edt_rate);
        edt_year = findViewById(R.id.edt_year);
        tv_years = findViewById(R.id.tv_years);
        tv_months = findViewById(R.id.tv_months);
        edt_monthly_emi = findViewById(R.id.edt_monthly_emi);
        edt_t_interest = findViewById(R.id.edt_t_interest);
        edt_t_payment = findViewById(R.id.edt_t_payment);
        btn_business_reset = findViewById(R.id.btn_business_reset);
        btn_business_calculate = findViewById(R.id.btn_business_calculate);
        btn_amortization = findViewById(R.id.btn_amortization);

        btn_amortization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_principal_amt.getText().toString().isEmpty() ||
                        edt_rate.getText().toString().isEmpty() ||
                        edt_year.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter all inputs", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(EMI_Calc_Summary.this, Summary_Amortization.class);
                intent.putExtra("principal", edt_principal_amt.getText().toString());
                intent.putExtra("rate", edt_rate.getText().toString());
                intent.putExtra("duration", String.valueOf(totalMonths)); // Pass totalMonths instead of edt_year.getText().toString()
                intent.putExtra("monthlyEMI", edt_monthly_emi.getText().toString());
                intent.putExtra("totalInterest", edt_t_interest.getText().toString());
                intent.putExtra("totalPayment", edt_t_payment.getText().toString());
                startActivity(intent);
            }
        });
        btn_business_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate(v);
            }
        });

        btn_business_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset(v);
            }
        });


        tv_months.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setColorsOfSelectedTextView(tv_years, tv_months);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Set monthOrYear to 2 indicating months
                monthOrYear = 2;
            }
        });

        tv_years.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setColorsOfSelectedTextView(tv_months, tv_years);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Set monthOrYear to 1 indicating years
                monthOrYear = 1;
            }
        });
    }

    //--------------------------------------------------------------------------------------------------

    private void reset(View view) {

        // Clear the text of all TextInputEditText fields
        edt_principal_amt.setText("");
        edt_rate.setText("");
        edt_year.setText("");
        edt_monthly_emi.setText("");
        edt_t_interest.setText("");
        edt_t_payment.setText("");
    }
    private void calculate(View view) {
        // Hide the soft keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        // Check if input fields are empty
        if (edt_principal_amt.getText().toString().isEmpty() ||
                edt_rate.getText().toString().isEmpty() ||
                edt_year.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter all inputs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform EMI calculation
        double principalAmt = Double.parseDouble(edt_principal_amt.getText().toString());
        double rate = Double.parseDouble(edt_rate.getText().toString());
        int duration = Integer.parseInt(edt_year.getText().toString());

        // Check if rate or duration is zero
        if (principalAmt == 0 || rate == 0 || duration == 0) {
            // Display appropriate message and return
            Toast.makeText(getApplicationContext(), "Input values must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        double monthlyRate = rate / 1200; // Convert annual rate to monthly rate

        // Calculate based on months or years

        if (monthOrYear == 1) { // If years is selected
            totalMonths = duration * 12; // Convert years to total months
        } else { // If months is selected
            totalMonths = duration;
        }

        double numerator = principalAmt * monthlyRate * Math.pow(1 + monthlyRate, totalMonths);
        double denominator = Math.pow(1 + monthlyRate, totalMonths) - 1;

        // Check if denominator is zero
        if (denominator == 0) {
            // Display appropriate message and return
            Toast.makeText(getApplicationContext(), "Invalid input. Please ensure interest rate and loan duration are not zero", Toast.LENGTH_SHORT).show();
            return;
        }

        double monthlyEMI = numerator / denominator;
        double totalPayment = monthlyEMI * totalMonths;
        double totalInterest = totalPayment - principalAmt;

        // Display the results
        edt_monthly_emi.setText(String.format("%.2f", monthlyEMI));
        edt_t_interest.setText(String.format("%.2f", totalInterest));
        edt_t_payment.setText(String.format("%.2f", totalPayment));
    }


    public void setColorsOfSelectedTextView(TextView textView, TextView textView2) throws Exception {
        textView2.setBackgroundColor(getResources().getColor(R.color.white));
        textView2.setTextColor(getResources().getColor(R.color.field_color));
        textView.setBackground(getResources().getDrawable(R.drawable.gradient));
        textView.setTextColor(getResources().getColor(R.color.white));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}