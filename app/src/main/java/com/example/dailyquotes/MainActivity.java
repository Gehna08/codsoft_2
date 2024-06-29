package com.example.dailyquotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView quoteTextView;
    private Button nextButton, shareButton, saveButton, viewFavoritesButton;
    private QuoteDao quoteDao;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        quoteTextView = findViewById(R.id.quoteTextView);
        nextButton = findViewById(R.id.nextButton);
        shareButton = findViewById(R.id.shareButton);
        saveButton = findViewById(R.id.saveButton);
        viewFavoritesButton = findViewById(R.id.viewFavoritesButton);

        quoteDao = new QuoteDao(this);
        quoteDao.open();

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareQuote();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayRandomQuote();
            }
        });

        displayRandomQuote();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuote();
            }
        });

        viewFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoriteQuotesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void shareQuote() {
        String quote = quoteTextView.getText().toString();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, quote);

        Intent chooser = Intent.createChooser(shareIntent, "Share Quote via");
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    private void displayRandomQuote() {
        Random random = new Random();
        int index = random.nextInt(quotes.quotes.length);
        String randomQuote = quotes.quotes[index];
        quoteTextView.setText(randomQuote);
    }

    private void saveQuote() {
        final String quoteText = quoteTextView.getText().toString();
        quoteDao.addQuote(quoteText);
    }

    @Override
    protected void onDestroy() {
        quoteDao.close();
        super.onDestroy();
    }
}