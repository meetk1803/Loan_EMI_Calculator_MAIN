package com.enacle.loanemicalculator.Starting_Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.enacle.loanemicalculator.R;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class Splash_Screen_Privacy extends AppCompatActivity {
    ExtendedFloatingActionButton BTN_accept,BTN_decline;
    MaterialCheckBox chk_privacy;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        // Check if privacy policy has been accepted before
        boolean isPrivacyAccepted = sharedPreferences.getBoolean("privacy_accepted", false);
        if (isPrivacyAccepted) {
            navigateToMainScreen();
            return;
        }
        setContentView(R.layout.activity_splash_screen_privacy);

        chk_privacy=findViewById(R.id.chk_privacy);
        BTN_accept =findViewById(R.id.BTN_accept);
        BTN_decline =findViewById(R.id.BTN_decline);

        BTN_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Splash_Screen_Privacy.this, Rate_us_Screen.class);
                startActivity(i);
            }
        });


        BTN_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chk_privacy.isChecked()){

                    // Store acceptance in SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("privacy_accepted", true);
                    editor.apply();

                    navigateToMainScreen();
                }else {
                    Toast.makeText(Splash_Screen_Privacy.this, "Please accept terms & conditions", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
//---------------------------------------------------------------------------------------------------------
    private void navigateToMainScreen() {
        startActivity(new Intent(Splash_Screen_Privacy.this, Start_Screen.class));
        finish();
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Splash_Screen_Privacy.this, Rate_us_Screen.class);
        startActivity(i);
    }
}