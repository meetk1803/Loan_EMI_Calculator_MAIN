package com.enacle.loanemicalculator.Calculators;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.enacle.loanemicalculator.R;
import com.google.android.material.textview.MaterialTextView;

import java.text.DecimalFormat;

public class Summary_Amortization extends AppCompatActivity {
    MaterialTextView txtLoanAmt, txtRate, txtMonths, txtMonthlyEmi, txtTotalPayment, txtTotalInterest;
    TableLayout tableLayout;
    double principalAmt, rate, monthlyEMI, totalInterest, totalPayment;
    int totalMonths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_amortization);

        txtLoanAmt = findViewById(R.id.txt_loan_amt);
        txtRate = findViewById(R.id.txt_rate);
        txtMonths = findViewById(R.id.txt_Months);
        txtMonthlyEmi = findViewById(R.id.txt_monthly_emi);
        txtTotalPayment = findViewById(R.id.txt_total_payment);
        txtTotalInterest = findViewById(R.id.txt_total_interest);
        tableLayout = findViewById(R.id.tableLayout);

        Intent i = getIntent();
        principalAmt = Double.parseDouble(i.getStringExtra("principal").replace("", ""));
        rate = Double.parseDouble(i.getStringExtra("rate"));
        totalMonths = Integer.parseInt(i.getStringExtra("duration"));
        monthlyEMI = Double.parseDouble(i.getStringExtra("monthlyEMI").replace("", ""));
        totalInterest = Double.parseDouble(i.getStringExtra("totalInterest").replace("", ""));
        totalPayment = Double.parseDouble(i.getStringExtra("totalPayment").replace("", ""));

        // Display data on views
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        txtLoanAmt.setText(formatter.format(principalAmt));
        txtRate.setText(rate + "%");
        txtMonths.setText(String.valueOf(totalMonths));
        txtMonthlyEmi.setText(formatter.format(monthlyEMI));
        txtTotalInterest.setText(formatter.format(totalInterest));
        txtTotalPayment.setText(formatter.format(totalPayment));

        printAmortizationSchedule();
    }
//---------------------------------------------------------------------------------------------------------------------

    public void download_pdf(View v){

    }

    public void share_info_age(View v) {
        StringBuilder sb = new StringBuilder();
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        sb.append("Loan Amount: " + formatter.format(principalAmt) + "\n");
        sb.append("Interest Rate Per Year: " + rate + "%\n");
        sb.append("Number of Months: " + totalMonths + "\n");
        sb.append("Monthly EMI: " + formatter.format(monthlyEMI) + "\n");
        sb.append("Total Amount Payment: " + formatter.format(totalPayment) + "\n");
        sb.append("Total Interest Payable: " + formatter.format(totalInterest) + "\n");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        startActivity(Intent.createChooser(intent, "Share via"));
    }

    public void printAmortizationSchedule() {
        // Clear table layout before printing
        tableLayout.removeAllViews();

        // Print table header
        printTableHeader();

        // Calculate and print each schedule item
        double remainingPrincipal = principalAmt;
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        for (int i = 1; i <= totalMonths; i++) {
            double interest = remainingPrincipal * (rate / 12) / 100;
            double principalPayment = monthlyEMI - interest;
            remainingPrincipal -= principalPayment;
            printScheduleItem(i, principalPayment, interest, remainingPrincipal, formatter);
        }
    }

    private void printScheduleItem(int i, double principalPayment, double interest, double remainingPrincipal, DecimalFormat formatter) {
        TableRow tableRow = new TableRow(this);
        tableRow.setPadding(3, 3, 3, 3);
        tableRow.setBackgroundColor(Color.LTGRAY); // Set background color

        TextView txtNo = createTextView(String.valueOf(i), Gravity.CENTER);
        tableRow.addView(txtNo);

        TextView txtPrincipal = createTextView(formatter.format(principalPayment), Gravity.CENTER);
        tableRow.addView(txtPrincipal);

        TextView txtInterest = createTextView(formatter.format(interest), Gravity.CENTER);
        tableRow.addView(txtInterest);

        TextView txtRemainingPrincipal;
        if (remainingPrincipal <= 1) {
            txtRemainingPrincipal = createTextView("0.00", Gravity.CENTER);
        } else {
            txtRemainingPrincipal = createTextView(formatter.format(remainingPrincipal), Gravity.CENTER);
        }
        tableRow.addView(txtRemainingPrincipal);

        tableLayout.addView(tableRow);
    }


    private void printTableHeader() {
        TableRow tableRow = new TableRow(this);
        tableRow.setPadding(3, 3, 3, 3);
        tableRow.setBackgroundColor(getColor(R.color.answer_color)); // Set background color

        TextView txtNo = createTextView("Months", Gravity.CENTER);
        txtNo.setTextColor(getColor(R.color.white));
        txtNo.setBackgroundColor(getColor(R.color.field_color)); // Set grid lines color
        tableRow.addView(txtNo);

        TextView txtPrincipal = createTextView("Principal", Gravity.CENTER);
        txtPrincipal.setTextColor(getColor(R.color.white));
        txtPrincipal.setBackgroundColor(getColor(R.color.field_color)); // Set grid lines color
        tableRow.addView(txtPrincipal);

        TextView txtInterest = createTextView("Interest", Gravity.CENTER);
        txtInterest.setTextColor(getColor(R.color.white));
        txtInterest.setBackgroundColor(getColor(R.color.field_color)); // Set grid lines color
        tableRow.addView(txtInterest);

        TextView txtRemainingPrincipal = createTextView("Remaining Principal", Gravity.CENTER);
        txtRemainingPrincipal.setTextColor(getColor(R.color.white));
        txtRemainingPrincipal.setBackgroundColor(getColor(R.color.field_color)); // Set grid lines color
        tableRow.addView(txtRemainingPrincipal);

        tableLayout.addView(tableRow);
    }

    private TextView createTextView(String text, int gravity) {
        TextView textView = new TextView(this);
        textView.setPadding(2, 6, 2, 6);
        textView.setTextColor(getColor(R.color.field_color)); // Set text color
        textView.setGravity(gravity); // Set text alignment
        textView.setText(text);
        textView.setBackgroundColor(Color.WHITE); // Set grid lines color
        return textView;
    }
}
