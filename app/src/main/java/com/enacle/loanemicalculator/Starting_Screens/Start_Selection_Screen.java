package com.enacle.loanemicalculator.Starting_Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.enacle.loanemicalculator.Calculators.Age_Calculator;
import com.enacle.loanemicalculator.Loan_EMI_Selection;
import com.enacle.loanemicalculator.R;

public class Start_Selection_Screen extends AppCompatActivity {

    CardView card_loan_calc,card_age_calc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_selection_screen);

        card_loan_calc=findViewById(R.id.card_loan_calc);
        card_age_calc=findViewById(R.id.card_age_calc);

        card_loan_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Start_Selection_Screen.this, Loan_EMI_Selection.class));
            }
        });

        card_age_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Start_Selection_Screen.this, Age_Calculator.class));
            }
        });
    }
}