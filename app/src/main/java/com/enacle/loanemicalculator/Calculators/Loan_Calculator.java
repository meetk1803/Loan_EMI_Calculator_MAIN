package com.enacle.loanemicalculator.Calculators;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.enacle.loanemicalculator.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class Loan_Calculator extends AppCompatActivity {
     TextInputEditText edtEmiAmt, edtRate, edtYear, edtLoanAmt, edtPrincipalAmt, edtTotalInterest, edtTotalPayment;
     TextView tvYears, tvMonths;

    int monthOrYear = 1;
    ExtendedFloatingActionButton btn_business_reset, btn_business_calculate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_calculator);

        edtEmiAmt = findViewById(R.id.edt_emi_amt);
        edtRate = findViewById(R.id.edt_rate);
        edtYear = findViewById(R.id.edt_year);
        edtLoanAmt = findViewById(R.id.edt_loan_amt);
        edtPrincipalAmt = findViewById(R.id.edt_principal_amt);
        edtTotalInterest = findViewById(R.id.edt_t_interest);
        edtTotalPayment = findViewById(R.id.edt_t_payment);
        tvYears = findViewById(R.id.tv_years);
        tvMonths = findViewById(R.id.tv_months);


        btn_business_reset = findViewById(R.id.btn_business_reset);
        btn_business_calculate = findViewById(R.id.btn_business_calculate);

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
        tvMonths.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setColorsOfSelectedTextView(tvYears, tvMonths);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Set monthOrYear to 2 indicating months
                monthOrYear = 2;
            }
        });

        tvYears.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    setColorsOfSelectedTextView(tvMonths, tvYears);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Set monthOrYear to 1 indicating years
                monthOrYear = 1;
            }
        });

    }
    //--------------------------------------------------------------------------------------------------[


    private void reset(View view) {

        // Clear the text of all TextInputEditText fields
        edtEmiAmt.setText("");
        edtRate.setText("");
        edtYear.setText("");
        edtPrincipalAmt.setText("");
        edtLoanAmt.setText("");
        edtTotalInterest.setText("");
        edtTotalPayment.setText("");
    }
    private void calculate(View view) {
        // Hide the soft keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        // Check if input fields are empty
        if (edtEmiAmt.getText().toString().isEmpty() ||
                edtRate.getText().toString().isEmpty() ||
                edtYear.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter all inputs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform EMI calculation
        double emiAmt = Double.parseDouble(edtEmiAmt.getText().toString());
        double rate = Double.parseDouble(edtRate.getText().toString());
        double duration = Double.parseDouble(edtYear.getText().toString());

        // Check if rate or duration is zero
        if (emiAmt == 0 || rate == 0 || duration == 0) {
            // Display appropriate message and return
            Toast.makeText(getApplicationContext(), "Input values must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        double monthlyRate = rate / 1200; // Convert annual rate to monthly rate

        // Calculate based on months or years
        double totalMonths;
        if (monthOrYear == 1) { // If years is selected
            totalMonths = duration * 12; // Convert years to total months
        } else { // If months is selected
            totalMonths = duration;
        }

        // Calculate principal amount
        double principalAmt = emiAmt * ((Math.pow(1 + monthlyRate, totalMonths) - 1) / (monthlyRate * Math.pow(1 + monthlyRate, totalMonths)));

        // Calculate total payment
        double totalPayment = emiAmt * totalMonths;

        // Calculate total interest
        double totalInterest = totalPayment - principalAmt;

        // Display the results
        edtLoanAmt.setText(String.format("%.2f", principalAmt));
        edtPrincipalAmt.setText(String.format("%.2f", principalAmt));
        edtTotalInterest.setText(String.format("%.2f", totalInterest));
        edtTotalPayment.setText(String.format("%.2f", totalPayment));
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