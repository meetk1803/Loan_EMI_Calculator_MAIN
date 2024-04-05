package com.enacle.loanemicalculator.Calculators;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.enacle.loanemicalculator.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class Age_Calculator extends AppCompatActivity {

    TextView txt_years, txt_months, txt_days, txt_months_second, txt_days_second, txt_years_third, txt_months_third, txt_days_third, txt_weeks_third, txt_hours_third, txt_minutes_third, txt_seconds_third;
    EditText edt_today_date, edt_date_of_birth;
    ExtendedFloatingActionButton btn_business_reset, btn_business_calculate;
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_calculator);

        edt_today_date = findViewById(R.id.edt_today_date);
        edt_date_of_birth = findViewById(R.id.edt_date_of_birth);
        btn_business_reset = findViewById(R.id.btn_business_reset);
        btn_business_calculate = findViewById(R.id.btn_business_calculate);
        txt_years = findViewById(R.id.txt_Years);
        txt_months = findViewById(R.id.txt_Months);
        txt_days = findViewById(R.id.txt_Days);
        txt_months_second = findViewById(R.id.txt_Months_second);
        txt_days_second = findViewById(R.id.txt_Days_second);
        txt_years_third = findViewById(R.id.txt_Years_third);
        txt_months_third = findViewById(R.id.txt_Months_third);
        txt_days_third = findViewById(R.id.txt_Days_third);
        txt_weeks_third = findViewById(R.id.txt_Weeks_third);
        txt_hours_third = findViewById(R.id.txt_hours_third);
        txt_minutes_third = findViewById(R.id.txt_Minutes_third);
        txt_seconds_third = findViewById(R.id.txt_Seconds_third);
        tableLayout = findViewById(R.id.tableLayout);

        // Set default date to today's date
        setCurrentDateOnEditText();

        // Set OnClickListener to show custom date picker dialog
        edt_today_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDatePicker(edt_today_date);
            }
        });

        edt_date_of_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDatePicker(edt_date_of_birth);
            }
        });
        // Set OnClickListener to calculate age
        btn_business_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to calculate age
                calculateAge();

            }
        });

        // Set OnClickListener to reset
        btn_business_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear birthdate EditText and set today's date to today's date EditText
                edt_date_of_birth.setText("dd/mm/yyyy");
                setCurrentDateOnEditText();
                txt_years.setText("0");
                txt_months.setText("0");
                txt_days.setText("0");
                txt_months_second.setText("0");
                txt_days_second.setText("0");
                txt_years_third.setText("0");
                txt_months_third.setText("0");
                txt_days_third.setText("0");
                txt_weeks_third.setText("0");
                txt_hours_third.setText("0");
                txt_minutes_third.setText("0");
                txt_seconds_third.setText("0");

                tableLayout.removeAllViews();

            }
        });
    }

    //-----------------------------------------------------------------------------------------
    private void calculateAge() {
        String strTodayDate = edt_today_date.getText().toString();
        String strBirthDate = edt_date_of_birth.getText().toString();

        if (strBirthDate.isEmpty() || strBirthDate.equals("dd/mm/yyyy")) {
            // EditText is empty, show toast
            Toast.makeText(getApplicationContext(), "Date of birth is empty", Toast.LENGTH_SHORT).show();
        }

        // Parse the input dates
        LocalDate todayDate = parseDate(strTodayDate);
        LocalDate birthDate = parseDate(strBirthDate);

        if (todayDate != null && birthDate != null) {
            // Calculate age
            Period age = Period.between(birthDate, todayDate);
            int years = age.getYears();
            int months = age.getMonths();
            int days = age.getDays();

            // Set age in TextViews
            txt_years.setText(String.valueOf(years));
            txt_months.setText(String.valueOf(months));
            txt_days.setText(String.valueOf(days));

            // Calculate next birthday
            LocalDate nextBirthday = birthDate.plusYears(years);
            if (nextBirthday.isBefore(todayDate) || nextBirthday.isEqual(todayDate)) {
                nextBirthday = nextBirthday.plusYears(1);
            }
            Period timeUntilNextBirthday = Period.between(todayDate, nextBirthday);
            int monthsUntilNextBirthday = timeUntilNextBirthday.getMonths();
            int daysUntilNextBirthday = timeUntilNextBirthday.getDays();

            // Set next birthday in TextViews
            txt_months_second.setText(String.valueOf(monthsUntilNextBirthday));
            txt_days_second.setText(String.valueOf(daysUntilNextBirthday));

            // Additional information (Using ChronoUnit for compatibility)
            long yearsDiff = ChronoUnit.YEARS.between(birthDate, todayDate);
            long monthsDiff = ChronoUnit.MONTHS.between(birthDate, todayDate);
            long daysDiff = ChronoUnit.DAYS.between(birthDate, todayDate);
            long weeksDiff = daysDiff / 7; // Weeks are not directly supported by Period
            long hoursDiff = ChronoUnit.HOURS.between(birthDate.atStartOfDay(), todayDate.atStartOfDay());
            long minutesDiff = ChronoUnit.MINUTES.between(birthDate.atStartOfDay(), todayDate.atStartOfDay());
            long secondsDiff = ChronoUnit.SECONDS.between(birthDate.atStartOfDay(), todayDate.atStartOfDay());

            // Set additional information in TextViews
            txt_years_third.setText(String.valueOf(yearsDiff));
            txt_months_third.setText(String.valueOf(monthsDiff));
            txt_days_third.setText(String.valueOf(daysDiff));
            txt_weeks_third.setText(String.valueOf(weeksDiff));
            txt_hours_third.setText(String.valueOf(hoursDiff));
            txt_minutes_third.setText(String.valueOf(minutesDiff));
            txt_seconds_third.setText(String.valueOf(secondsDiff));

            printNextBirthdays(strBirthDate);
        }
    }


    private void printNextBirthdays(String strBirthDate) {
        tableLayout.removeAllViews();

        printTableHeader();
        LocalDate birthdate_main = parseDate(strBirthDate);
        int birthDate = birthdate_main.getDayOfMonth();
        int birthMonth = birthdate_main.getMonthValue();
        LocalDate currentDate = LocalDate.now();

        for (int i = 1; i <= 10; i++) {
            LocalDate nextBirthdate = LocalDate.of(currentDate.getYear() + i, birthMonth, birthDate);
            String formattedDate = String.format("%02d/%02d/%d", birthDate,birthMonth, nextBirthdate.getYear());
            String dayofweeks = nextBirthdate.getDayOfWeek().toString();
            addTableRow(formattedDate, dayofweeks);
        }

    }

    private void addTableRow(String date, String dayOfWeek) {
        TableRow tableRow = new TableRow(this);
        tableRow.setPadding(3, 3, 3, 3);
        tableRow.setBackgroundColor(getColor(R.color.answer_color)); // Set background color

        TextView txtDate = createTextView(date, Gravity.CENTER);
        txtDate.setTextColor(getColor(R.color.field_color));
        txtDate.setBackgroundColor(getColor(R.color.white)); // Set grid lines color
        tableRow.addView(txtDate);

        TextView txtDay = createTextView(dayOfWeek, Gravity.CENTER);
        txtDay.setTextColor(getColor(R.color.field_color));
        txtDay.setBackgroundColor(getColor(R.color.white)); // Set grid lines color
        tableRow.addView(txtDay);

        tableLayout.addView(tableRow);
    }

    private void printTableHeader() {
        TableRow tableRow = new TableRow(this);
        tableRow.setPadding(3, 3, 3, 3);
        tableRow.setBackgroundColor(getColor(R.color.answer_color)); // Set background color

        TextView txtNo = createTextView("Date", Gravity.CENTER);
        txtNo.setTextColor(getColor(R.color.white));
        txtNo.setBackgroundColor(getColor(R.color.field_color)); // Set grid lines color
        tableRow.addView(txtNo);

        TextView txtPrincipal = createTextView("Day", Gravity.CENTER);
        txtPrincipal.setTextColor(getColor(R.color.white));
        txtPrincipal.setBackgroundColor(getColor(R.color.field_color)); // Set grid lines color
        tableRow.addView(txtPrincipal);

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


    private LocalDate parseDate(String dateString) {
        try {
            String[] parts = dateString.split("/");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            return LocalDate.of(year, month, day);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void setCurrentDateOnEditText() {
        // Get current date
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        // Set current date to EditText
        String currentDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
        edt_today_date.setText(currentDate);
    }

    private void showCustomDatePicker(EditText editText) {
        // Create a DatePickerDialog with today's date as default
        DatePickerDialog datePickerDialog = new DatePickerDialog(Age_Calculator.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    // Set selected date to the EditText
                    String selectedDate = String.format("%02d/%02d/%d", selectedDayOfMonth, selectedMonth + 1, selectedYear);
                    editText.setText(selectedDate);
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        // Set date picker dialog color
        datePickerDialog.getDatePicker().setBackgroundColor(Color.WHITE); // Setting background color
        datePickerDialog.getDatePicker().setCalendarViewShown(false); // Hiding calendar view

        // Show the dialog
        datePickerDialog.show();
    }

    public void share_info_age(View v) {
        // Get the age information from TextViews
        String years = txt_years.getText().toString();
        String months = txt_months.getText().toString();
        String days = txt_days.getText().toString();
        String monthsUntilNextBirthday = txt_months_second.getText().toString();
        String daysUntilNextBirthday = txt_days_second.getText().toString();
        String yearsDiff = txt_years_third.getText().toString();
        String monthsDiff = txt_months_third.getText().toString();
        String daysDiff = txt_days_third.getText().toString();
        String weeksDiff = txt_weeks_third.getText().toString();
        String hoursDiff = txt_hours_third.getText().toString();
        String minutesDiff = txt_minutes_third.getText().toString();
        String secondsDiff = txt_seconds_third.getText().toString();

        // Construct the message to be shared
        String shareMessage = "Age Information:\n" +
                "Age: " + years + " years, " + months + " months, " + days + " days\n" +
                "Months until next birthday: " + monthsUntilNextBirthday + "\n" +
                "Days until next birthday: " + daysUntilNextBirthday + "\n" +
                "Additional Information:\n" +
                "Years difference: " + yearsDiff + "\n" +
                "Months difference: " + monthsDiff + "\n" +
                "Days difference: " + daysDiff + "\n" +
                "Weeks difference: " + weeksDiff + "\n" +
                "Hours difference: " + hoursDiff + "\n" +
                "Minutes difference: " + minutesDiff + "\n" +
                "Seconds difference: " + secondsDiff;

        // Create an Intent to share the message
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

        // Launch the sharing activity
        startActivity(Intent.createChooser(shareIntent, "Share Age Information"));
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
