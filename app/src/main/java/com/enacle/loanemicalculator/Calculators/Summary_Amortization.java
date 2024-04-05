package com.enacle.loanemicalculator.Calculators;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.enacle.loanemicalculator.R;
import com.google.android.material.textview.MaterialTextView;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;

public class Summary_Amortization extends AppCompatActivity {
    MaterialTextView txtLoanAmt, txtRate, txtMonths, txtMonthlyEmi, txtTotalPayment, txtTotalInterest;
    TableLayout tableLayout;
    double principalAmt, rate, monthlyEMI, totalInterest, totalPayment;
    int totalMonths;
    static com.itextpdf.kernel.colors.Color loan_Summary = new DeviceRgb(6, 33, 168);
    static com.itextpdf.kernel.colors.Color footer_color = new DeviceRgb(6, 33, 168);
    static com.itextpdf.kernel.colors.Color cell_color = new DeviceRgb(6, 33, 168);

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


    //-----------------------------------------------------------------------------------------------------------------------
    public void download_pdf(View v) {
        double[][] amortizationSchedule = calculateAmortizationSchedule(principalAmt, rate, monthlyEMI, totalMonths);

        // Generate and save PDF
        generatePdf(this, principalAmt, rate, totalMonths, monthlyEMI, totalInterest, totalPayment, amortizationSchedule);
    }

    // Method to calculate the amortization schedule
    private double[][] calculateAmortizationSchedule(double principalAmt, double rate, double monthlyEMI, int totalMonths) {
        double[][] amortizationSchedule = new double[totalMonths][4];
        double remainingPrincipal = principalAmt;

        for (int i = 0; i < totalMonths; i++) {
            double interest = remainingPrincipal * (rate / 12) / 100;
            double principalPayment = monthlyEMI - interest;
            remainingPrincipal -= principalPayment;

            amortizationSchedule[i][0] = i + 1;
            amortizationSchedule[i][1] = principalPayment;
            amortizationSchedule[i][2] = interest;
            amortizationSchedule[i][3] = remainingPrincipal;
        }

        return amortizationSchedule;
    }

    public static void generatePdf(Context context, double principalAmt, double rate, int totalMonths, double monthlyEMI,
                                   double totalInterest, double totalPayment, double[][] amortizationSchedule) {
        // Get the directory for storing the PDF in the Downloads folder
        File pdfFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!pdfFolder.exists()) {
            pdfFolder.mkdirs();
        }

        // Generate the PDF file name
        String fileName = String.format(Locale.getDefault(), "LoanSummary_%d.pdf", System.currentTimeMillis());
        File pdfFile = new File(pdfFolder, fileName);

        try {
            // Create PDF document
            PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4);

            // Add title
            Paragraph title = new Paragraph("Loan Summary")
                    .setFont(com.itextpdf.kernel.font.PdfFontFactory.createFont())
                    .setBold()
                    .setFontSize(20)
                    .setFontColor(loan_Summary)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Add loan details table
            Table loanTable = new Table(2)
                    .useAllAvailableWidth()
                    .setMarginBottom(20);

            loanTable.addCell(createCell("Loan Amount", true));
            loanTable.addCell(createCell(formatCurrency(principalAmt), false));

            loanTable.addCell(createCell("Interest Rate Per Year", true));
            loanTable.addCell(createCell(String.format(Locale.getDefault(), "%.2f%%", rate), false));

            loanTable.addCell(createCell("Number of Months", true));
            loanTable.addCell(createCell(String.valueOf(totalMonths), false));

            loanTable.addCell(createCell("Monthly EMI", true));
            loanTable.addCell(createCell(formatCurrency(monthlyEMI), false));

            loanTable.addCell(createCell("Total Amount Payment", true));
            loanTable.addCell(createCell(formatCurrency(totalPayment), false));

            loanTable.addCell(createCell("Total Interest Payable", true));
            loanTable.addCell(createCell(formatCurrency(totalInterest), false));

            document.add(loanTable);

            // Add amortization schedule table
            Table amortizationTable = new Table(amortizationSchedule[0].length)
                    .useAllAvailableWidth()
                    .setMarginBottom(20);

            // Add table header
            addTableHeader(amortizationTable, new String[]{"Months", "Principal", "Interest", "Remaining Principal"});

            // Add table content
            // Add table content
            for (int rowIndex = 0; rowIndex < amortizationSchedule.length; rowIndex++) {
                double[] row = amortizationSchedule[rowIndex];
                for (int i = 0; i < row.length; i++) {
                    if (i == 0) { // If it's the months column
                        // Cast the month value to an integer to remove decimal points
                        amortizationTable.addCell(createCell(String.valueOf((int) row[i]), false));
                    } else {
                        if (i == 3 && (rowIndex == amortizationSchedule.length - 1 || row[3] <= 1)) {
                            // If it's the remaining principal column and it's the last row or remaining principal is less than or equal to 1
                            amortizationTable.addCell(createCell("0.00", false));
                        } else {
                            amortizationTable.addCell(createCell(formatCurrency(row[i]), false));
                        }
                    }
                }
            }


            document.add(amortizationTable);

            // Close the document
            document.close();

            // Show a toast message indicating successful PDF creation
            Toast.makeText(context, "PDF saved to " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to generate PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private static void addTableHeader(Table table, String[] headers) {
        for (String header : headers) {
            table.addCell(createHeaderCell(header));
        }
    }

    private static Paragraph createHeaderCell(String text) {
        return new Paragraph(text)
                .setBackgroundColor(footer_color)
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.CENTER);
    }

    private static Paragraph createCell(String text, boolean isHeader) {
        Paragraph cell = new Paragraph(text)
                .setFontColor(cell_color)
                .setTextAlignment(TextAlignment.CENTER);
        if (isHeader) {
            cell.setBackgroundColor(footer_color);
            cell.setFontColor(ColorConstants.WHITE);
        }
        return cell;
    }

    private static String formatCurrency(double amount) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(amount);
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
        textView.setPadding(2, 15, 2, 15);
        textView.setTextColor(getColor(R.color.field_color)); // Set text color
        textView.setGravity(gravity); // Set text alignment
        textView.setText(text);
        textView.setBackgroundColor(Color.WHITE); // Set grid lines color
        return textView;
    }

    public void txt_back(View v) {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
