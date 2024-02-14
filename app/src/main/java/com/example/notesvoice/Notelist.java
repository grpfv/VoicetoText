package com.example.notesvoice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Notelist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notelist);

        // Get reference to notes EditText
        TextView titleTextView = findViewById(R.id.titleTextView);
        EditText notesEditText = findViewById(R.id.notes);

        // Get text passed from VoiceGenerator activity
        String titleText = getIntent().getStringExtra("title");
        String notesText = getIntent().getStringExtra("notes");

        // Set title text to title TextView
        titleTextView.setText(titleText);

        // Set notes text to notes EditText
        notesEditText.setText(notesText);


    }
    public void buttonCLick(View view) {
        Intent intent = new Intent(this, VoiceGenerator.class);
        startActivity(intent);
    }
}
