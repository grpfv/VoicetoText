package com.example.notesvoice;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceGenerator extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    EditText titleEditText, notesEditText;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_generator);

        titleEditText = findViewById(R.id.Title);
        notesEditText = findViewById(R.id.notes);
        addButton = findViewById(R.id.savebutton);
        ImageView micImageView = findViewById(R.id.mic);

        micImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check permission
                if (checkSelfPermission(android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                    // Permission denied, request it
                    String[] permissions = {Manifest.permission.RECORD_AUDIO};
                    requestPermissions(permissions, REQUEST_CODE_SPEECH_INPUT);
                } else {
                    // Permission already granted
                    startSpeechToText();
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get text from title and notes EditText
                String titleText = titleEditText.getText().toString().trim();
                String notesText = notesEditText.getText().toString().trim();

                // Check if either title or notes are empty
                if (!titleText.isEmpty() && !notesText.isEmpty()) {
                    // Create intent to navigate to Notelist activity
                    Intent intent = new Intent(VoiceGenerator.this, Notelist.class);
                    // Pass title and notes text as extras
                    intent.putExtra("title", titleText);
                    intent.putExtra("notes", notesText);
                    // Start Notelist activity
                    startActivity(intent);

                    // Show toast message
                    Toast.makeText(VoiceGenerator.this, "Notes saved", Toast.LENGTH_SHORT).show();
                } else {
                    // Show toast message if title or notes are empty
                    Toast.makeText(VoiceGenerator.this, "Title or notes are empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Start speech to text intent
    private void startSpeechToText() {
        // Intent to show speech to text dialog
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");

        // Start intent
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Receive speech input result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    // Get text array from voice intent
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    // Set text to notesEditText
                    if (result != null && result.size() > 0) {
                        notesEditText.setText(result.get(0));
                    }
                }
                break;
            }
        }
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    startSpeechToText();
                } else {
                    // Permission denied
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
