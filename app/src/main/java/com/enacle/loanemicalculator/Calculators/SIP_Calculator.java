package com.enacle.loanemicalculator.Calculators;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.enacle.loanemicalculator.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;

public class SIP_Calculator extends AppCompatActivity {

    TextInputEditText edtMonthlyAmount, edtRate, edtYear, edtInvestment, edtTotalInterest, edtTotalPayment;
    ExtendedFloatingActionButton btnReset, btnCalculate;
    TextView tvYears, tvMonths;
    int monthOrYear = 1;
    // Define your DecimalFormat formatter
    DecimalFormat formatter = new DecimalFormat("#,##,###.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sip_calculator);

        edtMonthlyAmount = findViewById(R.id.edt_monthly_amt);
        edtRate = findViewById(R.id.edt_rate);
        edtYear = findViewById(R.id.edt_year);
        edtInvestment = findViewById(R.id.edt_investment);
        edtTotalInterest = findViewById(R.id.edt_t_interest);
        edtTotalPayment = findViewById(R.id.edt_t_payment);

        // Find and initialize ExtendedFloatingActionButtons
        btnReset = findViewById(R.id.btn_business_reset);
        btnCalculate = findViewById(R.id.btn_business_calculate);

        // Find and initialize TextViews
        tvYears = findViewById(R.id.tv_years);
        tvMonths = findViewById(R.id.tv_months);

        // Set onClickListeners for buttons and text views
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculate(v);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
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


    //--------------------------------------------------------------------------------------------------

    private void reset(View view) {
        // Clear the text of all TextInputEditText fields
        edtMonthlyAmount.setText("");
        edtRate.setText("");
        edtYear.setText("");
        edtInvestment.setText("");
        edtTotalInterest.setText("");
        edtTotalPayment.setText("");
    }

    private void calculate(View view) {
        // Hide the soft keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        // Check if input fields are empty
        if (edtMonthlyAmount.getText().toString().isEmpty() ||
                edtRate.getText().toString().isEmpty() ||
                edtYear.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter all inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        String rateString = edtRate.getText().toString();
        if (!rateString.isEmpty()) {
            double rateValue = Double.parseDouble(rateString);
            if (rateValue < 0 || rateValue >= 100) {
                Toast.makeText(getApplicationContext(), "Please enter a valid rate of interest (0 to 99.99)", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please enter the rate of interest", Toast.LENGTH_SHORT).show();
            return;
        }
        // Perform SIP calculation
        double monthlyAmount = Double.parseDouble(edtMonthlyAmount.getText().toString());
        double rate = Double.parseDouble(edtRate.getText().toString());
        double years = Double.parseDouble(edtYear.getText().toString());

        // Check if rate or years is zero
        if (monthlyAmount == 0 || rate == 0 || years == 0) {
            // Display appropriate message and return
            Toast.makeText(getApplicationContext(), "Input values must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        double monthlyRate = rate / 1200; // Convert annual rate to monthly rate



// Calculate total investment based on years or months
        double totalInvestment;
        if (monthOrYear == 1) { // If years is selected
            totalInvestment = monthlyAmount * years * 12;
            // Calculate future value of investment for years
            double futureValue = monthlyAmount * ((Math.pow(1 + monthlyRate, years * 12) - 1) / monthlyRate) * (1 + monthlyRate);
            // Display the results
            edtInvestment.setText(formatter.format(totalInvestment));
            edtTotalInterest.setText(formatter.format(futureValue - totalInvestment));
            edtTotalPayment.setText(formatter.format(futureValue));
        } else { // If months is selected
            totalInvestment = monthlyAmount * years;
            // Calculate future value of investment for months
            double futureValue = monthlyAmount * ((Math.pow(1 + monthlyRate, years) - 1) / monthlyRate) * (1 + monthlyRate);
            // Display the results
            edtInvestment.setText(formatter.format(totalInvestment));
            edtTotalInterest.setText(formatter.format(futureValue - totalInvestment));
            edtTotalPayment.setText(formatter.format(futureValue));
        }

    }




    public void setColorsOfSelectedTextView(TextView textView, TextView textView2) throws Exception {
        textView2.setBackgroundColor(getResources().getColor(R.color.white));
        textView2.setTextColor(getResources().getColor(R.color.field_color));
        textView.setBackground(getResources().getDrawable(R.drawable.gradient));
        textView.setTextColor(getResources().getColor(R.color.white));
    }


    public void txt_back(View v){
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}