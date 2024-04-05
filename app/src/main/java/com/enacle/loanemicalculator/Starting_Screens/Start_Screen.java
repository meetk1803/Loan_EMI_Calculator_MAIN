package com.enacle.loanemicalculator.Starting_Screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.View;

import com.enacle.loanemicalculator.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class Start_Screen extends AppCompatActivity {

    ExtendedFloatingActionButton BTN_get_started;
    CardView card_privacy,card_share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        BTN_get_started =findViewById(R.id.BTN_get_started);
        card_privacy =findViewById(R.id.card_privacy);
        card_share =findViewById(R.id.card_share);

        card_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Start_Screen.this,Privacy_policy.class));
            }
        });
        card_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent with ACTION_SEND
                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                // Set the type of content to text/plain
                shareIntent.setType("text/plain");

                // Set the content of the sharing
                String shareMessage = "Check out this amazing app!";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

                // Launch the sharing activity
                startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });
        BTN_get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Start_Screen.this,Start_Selection_Screen.class));
            }
        });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        Intent i = new Intent(Start_Screen.this, Rate_us_Screen.class);
        startActivity(i);
    }
}