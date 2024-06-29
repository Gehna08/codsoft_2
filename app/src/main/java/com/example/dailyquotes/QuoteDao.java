package com.example.dailyquotes;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class QuoteDao {
    private SQLiteDatabase db;
    private QuoteDbHelper dbHelper;

    public QuoteDao(Context context) {
        dbHelper = new QuoteDbHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean addQuote(String quoteText) {
        // Check if the quote already exists in the database
        if (isQuoteExists(quoteText)) {
            return false; // Quote already exists, return false
        }

        ContentValues values = new ContentValues();
        values.put(QuoteDbHelper.COLUMN_QUOTE, quoteText);

        long insertId = db.insert(QuoteDbHelper.TABLE_NAME, null, values);
        return insertId != -1; // Return true if insertion was successful
    }

    private boolean isQuoteExists(String quoteText) {
        Cursor cursor = db.query(QuoteDbHelper.TABLE_NAME,
                new String[]{QuoteDbHelper.COLUMN_QUOTE},
                QuoteDbHelper.COLUMN_QUOTE + "=?",
                new String[]{quoteText},
                null, null, null);

        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean removeQuote(String quoteText) {
        int rowsAffected = db.delete(QuoteDbHelper.TABLE_NAME,
                QuoteDbHelper.COLUMN_QUOTE + "=?",
                new String[]{quoteText});
        return rowsAffected > 0;
    }


    public List<String> getAllQuotes() {
        List<String> quotes = new ArrayList<>();

        Cursor cursor = db.query(QuoteDbHelper.TABLE_NAME,
                new String[]{QuoteDbHelper.COLUMN_QUOTE},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                quotes.add(cursor.getString(cursor.getColumnIndexOrThrow(QuoteDbHelper.COLUMN_QUOTE)));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return quotes;
    }
}