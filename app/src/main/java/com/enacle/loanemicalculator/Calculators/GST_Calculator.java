package com.enacle.loanemicalculator.Calculators;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.enacle.loanemicalculator.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

public class GST_Calculator extends AppCompatActivity {
     TextInputEditText initialAmountEditText, gstRateEditText, gstAmountEditText,
            netAmountEditText, totalAmountEditText, cgstEditText, sgstEditText;
     RadioButton addGSTRadioButton, subtractGSTRadioButton;
     TextView txt_cgst_sgst;
     ExtendedFloatingActionButton btn_business_calculate,btn_business_reset;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gst_calculator);

        // Initialize views
        initialAmountEditText = findViewById(R.id.initial_amount_edit_text);
        gstRateEditText = findViewById(R.id.gst_rate_edit_text);
        gstAmountEditText = findViewById(R.id.gst_amount_edit_text);
        netAmountEditText = findViewById(R.id.net_amount_edit_text);
        totalAmountEditText = findViewById(R.id.total_gst_involves);
//        cgstEditText = findViewById(R.id.cgst_amount);
//        sgstEditText = findViewById(R.id.Sgst_amount);\
        txt_cgst_sgst=findViewById(R.id.txt_cgst_sgst);
        addGSTRadioButton = findViewById(R.id.add_gst_radio_button);
        subtractGSTRadioButton = findViewById(R.id.subtract_gst_radio_button);
        btn_business_calculate=findViewById(R.id.btn_business_calculate);
        btn_business_reset=findViewById(R.id.btn_business_reset);
        // Set OnClickListener for the Calculate button
       btn_business_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateGST();
            }
        });

        // Set OnClickListener for the Reset button
        findViewById(R.id.btn_business_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });
    }

    private void calculateGST() {
        // Check if input fields are empty
        if (initialAmountEditText.getText().toString().isEmpty() || gstRateEditText.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter all inputs", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Retrieve values entered by the user
            double initialAmount = Double.parseDouble(initialAmountEditText.getText().toString());
            double gstRate = Double.parseDouble(gstRateEditText.getText().toString());
            boolean isAddGST = addGSTRadioButton.isChecked(); // Check if user selected "Add GST +"

            // Check if gstRate is zero
            if (initialAmount==0||gstRate == 0) {
                Toast.makeText(getApplicationContext(), "Input values must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }

            // Calculate GST amount based on the GST rate
            double gstAmount = (gstRate / 100) * initialAmount;

            // Calculate CGST and SGST based on the net amount after adding or subtracting GST
            double netAmount = isAddGST ? initialAmount + gstAmount : initialAmount - gstAmount;
            // Calculate CGST and SGST (assuming equal division)
            double cgst = gstAmount / 2;
            double sgst = gstAmount / 2;

            txt_cgst_sgst.setText("(CGST : " +gstRate/2+"% = " + cgst+")\n(SGST : " +gstRate/2+"% = " + sgst +")");
            // Calculate the total amount including GST
            double totalAmount = isAddGST ? netAmount : initialAmount;

            // Display the calculated values in the respective TextInputEditText fields
            gstAmountEditText.setText(String.format("%.2f", gstAmount));
            netAmountEditText.setText(String.format("%.2f", netAmount));
            totalAmountEditText.setText(String.format("%.2f", totalAmount));
//            cgstEditText.setText(String.format("%.2f", cgst));
//            sgstEditText.setText(String.format("%.2f", sgst));
        } catch (NumberFormatException e) {
            // Handle NumberFormatException (e.g., if input is not a valid number)
            Toast.makeText(getApplicationContext(), "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Handle other exceptions
            Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    // Method to reset all input fields
    private void resetFields() {
        initialAmountEditText.setText("");
        gstRateEditText.setText("");
        gstAmountEditText.setText("");
        netAmountEditText.setText("");
        txt_cgst_sgst.setText("(CGST : 0.00% = 0)\n(SGST : 0.00% = 0)");
        addGSTRadioButton.setChecked(true);
    }

    // Method to handle back button click
    public void txt_back(View view) {
        finish();
    }
}
