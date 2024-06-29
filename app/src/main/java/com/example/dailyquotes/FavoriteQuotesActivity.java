package com.example.dailyquotes;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class FavoriteQuotesActivity extends AppCompatActivity {

    private ListView quotesListView;
    private QuoteDao quoteDao;
    private Toolbar toolbar;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_quotes);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        quotesListView = findViewById(R.id.quotesListView);
        quoteDao = new QuoteDao(this);
        quoteDao.open();

        loadQuotes();

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                int position = quotesListView.pointToPosition((int) e.getX(), (int) e.getY());
                if (position != ListView.INVALID_POSITION) {
                    String quoteToRemove = (String) quotesListView.getItemAtPosition(position);
                    boolean removed = quoteDao.removeQuote(quoteToRemove);
                    if (removed) {
                        Toast.makeText(FavoriteQuotesActivity.this, "Quote removed from favorites", Toast.LENGTH_SHORT).show();
                        loadQuotes(); // Refresh the list after removal
                        return true;
                    }
                }
                return super.onDoubleTap(e);
            }
        });

        // Attach gesture detector to list view
        quotesListView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));
    }

    private void loadQuotes() {
        List<String> quotes = quoteDao.getAllQuotes();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, quotes);
        quotesListView.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        quoteDao.close();
        super.onDestroy();
    }
}

