package com.enacle.loanemicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.enacle.loanemicalculator.Calculators.EMI_Calculator;
import com.enacle.loanemicalculator.Calculators.Loan_Calculator;
import com.enacle.loanemicalculator.Calculators.Loan_Compare;
import com.google.android.material.card.MaterialCardView;

public class Loan_EMI_Selection extends AppCompatActivity {
    MaterialCardView card_emi_calc, card_loan_calc, card_loan_compare_calc, card_sip_calc, card_tenure_calc, card_gst_calc, card_emi_calc_summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_emi_selection);


        card_emi_calc = findViewById(R.id.card_emi_calc);
        card_loan_calc = findViewById(R.id.card_loan_calc);
        card_loan_compare_calc = findViewById(R.id.card_loan_compare_calc);
        card_sip_calc = findViewById(R.id.card_sip_calc);
        card_tenure_calc = findViewById(R.id.card_tenure_calc);
        card_gst_calc = findViewById(R.id.card_gst_calc);
        card_emi_calc_summary = findViewById(R.id.card_emi_calc_summary);


        card_emi_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Loan_EMI_Selection.this, EMI_Calculator.class));
            }
        });

        card_loan_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Loan_EMI_Selection.this, Loan_Calculator.class));
            }
        });
        card_loan_compare_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Loan_EMI_Selection.this, Loan_Compare.class));
            }
        });

    }
}