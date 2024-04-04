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

public class Tenure_Calculator extends AppCompatActivity {
    TextInputEditText edtPrincipalAmt, edtEmi, edtRate;
    TextView txtMonths, txtYears, edtPrincipalAmtAns, edtTotalInterest, edtTotalPayment;
    ExtendedFloatingActionButton btnReset, btnCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenure_calculator);


        // Find TextInputEditText by ID
        edtPrincipalAmt = findViewById(R.id.edt_principal_amt);
        edtEmi = findViewById(R.id.edt_emi);
        edtRate = findViewById(R.id.edt_rate);

        // Find TextViews by ID
        txtMonths = findViewById(R.id.txt_Months_second);
        txtYears = findViewById(R.id.txt_Years_second);
        edtPrincipalAmtAns = findViewById(R.id.edt_principal_amt_ans);
        edtTotalInterest = findViewById(R.id.edt_t_interest);
        edtTotalPayment = findViewById(R.id.edt_t_payment);

        // Find ExtendedFloatingActionButtons by ID
        btnReset = findViewById(R.id.btn_business_reset);
        btnCalculate = findViewById(R.id.btn_business_calculate);
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


    }


    //--------------------------------------------------------------------------------------------------

    private void reset(View view) {
        // Clear the text of all TextInputEditText fields
        edtPrincipalAmt.setText("");
        edtEmi.setText("");
        edtRate.setText("");
        txtMonths.setText("0");
        txtYears.setText("0");
        edtPrincipalAmtAns.setText("");
        edtTotalInterest.setText("");
        edtTotalPayment.setText("");
    }

    private void calculate(View view) {
        // Hide the soft keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        // Check if input fields are empty
        if (edtPrincipalAmt.getText().toString().isEmpty() ||
                edtEmi.getText().toString().isEmpty() ||
                edtRate.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter all inputs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Perform calculation
        double principalAmt = Double.parseDouble(edtPrincipalAmt.getText().toString());
        double emi = Double.parseDouble(edtEmi.getText().toString());
        double rate = Double.parseDouble(edtRate.getText().toString());

        // Check if rate is zero
        if (emi == 0 || rate == 0) {
            // Display appropriate message and return
            Toast.makeText(getApplicationContext(), "EMI and rate values must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate tenure in months
        double monthlyRate = rate / 1200;
        double tenureMonths = Math.log(emi / (emi - monthlyRate * principalAmt)) / Math.log(1 + monthlyRate);

        // Calculate years and months
        int years = (int) (tenureMonths / 12);
        int months = (int) (tenureMonths % 12);

        // Adjust years if months exceed 12
        if (months > 0) {
            years += months / 12;
            months %= 12;
        }

        // Set the tenure in years and months combined format
        txtYears.setText(String.format("%d yr %d mon", years, months));
        // Set the total number of months in months field
        txtMonths.setText(String.format("%d mon", (int)tenureMonths));

        // Calculate total interest
        double totalInterest = (emi * tenureMonths) - principalAmt;

        // Calculate total payment
        double totalPayment = principalAmt + totalInterest;

        // Set the principal amount, total interest, and total payment
        edtPrincipalAmtAns.setText(String.format("₹%.2f", principalAmt));
        edtTotalInterest.setText(String.format("₹%.2f", totalInterest));
        edtTotalPayment.setText(String.format("₹%.2f", totalPayment));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}