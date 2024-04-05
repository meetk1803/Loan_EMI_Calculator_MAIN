package com.enacle.loanemicalculator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.enacle.loanemicalculator.Calculators.EMI_Calc_Summary;
import com.enacle.loanemicalculator.Calculators.EMI_Calculator;
import com.enacle.loanemicalculator.Calculators.GST_Calculator;
import com.enacle.loanemicalculator.Calculators.Loan_Calculator;
import com.enacle.loanemicalculator.Calculators.Loan_Compare;
import com.enacle.loanemicalculator.Calculators.SIP_Calculator;
import com.enacle.loanemicalculator.Calculators.Tenure_Calculator;
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
        card_sip_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Loan_EMI_Selection.this, SIP_Calculator.class));
            }
        });
        card_tenure_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Loan_EMI_Selection.this, Tenure_Calculator.class));
            }
        });

        card_gst_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Loan_EMI_Selection.this, GST_Calculator.class));
            }
        });
        card_emi_calc_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Loan_EMI_Selection.this, EMI_Calc_Summary.class));
            }

        });

    }
    //-------------------------------------------------------------------------------------------------------------------
    public void txt_back(View v){
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}