package com.example.android.inventoryapp;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;
import com.example.android.inventoryapp.data.ProductDbHelper;

import java.util.Objects;

/**
 * Displays our list of books entered into the database
 */
public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ProductDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Database helper providing access to the database
        dbHelper = new ProductDbHelper(this);

        // Setup FAB to open EditorActivity
        final FloatingActionButton fab = findViewById(R.id.fab);
        setFabAnimation(fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseState();
    }

    /**
     * Temporary helper method displaying information about the state of database.
     */
    private void displayDatabaseState() {
        // Create and/or open a database to read from it
        SQLiteDatabase base = dbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_BOOK_TITLE,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_PHONE_NUMBER,
                InventoryEntry.COLUMN_QUANTITY,
                InventoryEntry.COLUMN_BOOK_PRICE};

        // Performing query on the inventory table
        Cursor cursor = base.query(
                InventoryEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        TextView bookList = findViewById(R.id.book_list);

        try {
            // Creating a header in the Text View using html tags for formatting purposes:
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            if (cursor.getCount() == 1) {

                bookList.setText(Html.fromHtml(getResources().getString(R.string.header_align) +
                        getResources().getString(R.string.contains) +
                        InventoryEntry.HTML_DOUBLE_BR +
                        getResources().getString(R.string.font_color) +
                        cursor.getCount() +
                        getResources().getString(R.string.book)));

            } else {

                bookList.setText(Html.fromHtml(getResources().getString(R.string.header_align) +
                        getResources().getString(R.string.contains) +
                        InventoryEntry.HTML_DOUBLE_BR +
                        getResources().getString(R.string.font_color) +
                        cursor.getCount() +
                        getResources().getString(R.string.books)));
            }

            bookList.append(Html.fromHtml(getResources().getString(R.string.header) +
                    InventoryEntry.HTML_DOUBLE_BR + InventoryEntry._ID + " " +
                    InventoryEntry.HTML_B + InventoryEntry.COLUMN_BOOK_TITLE +
                    InventoryEntry.HTML_B2 + InventoryEntry.HTML_DOUBLE_BR +
                    InventoryEntry.COLUMN_SUPPLIER_NAME + InventoryEntry.HTML_BR +
                    InventoryEntry.COLUMN_PHONE_NUMBER + InventoryEntry.HTML_BR +
                    InventoryEntry.COLUMN_QUANTITY + InventoryEntry.HTML_BR +
                    InventoryEntry.COLUMN_BOOK_PRICE + InventoryEntry.HTML_DOUBLE_BR +
                    getResources().getString(R.string.inventory)));

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
            int bookTitleColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_TITLE);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
            int phoneNumberColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PHONE_NUMBER);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
            int bookPriceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_BOOK_PRICE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentBookTitle = cursor.getString(bookTitleColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentPhoneNumber = cursor.getString(phoneNumberColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                Double currentBookPrice = cursor.getDouble(bookPriceColumnIndex);

                // Display the values from each column of the current row in the cursor in the TextView
                bookList.append(Html.fromHtml(InventoryEntry.HTML_DOUBLE_BR +
                        currentID + " " + InventoryEntry.HTML_B +
                        currentBookTitle + InventoryEntry.HTML_B_BR +
                        getResources().getString(R.string.supplier_name_cursor) +
                        InventoryEntry.HTML_B +
                        currentSupplierName + InventoryEntry.HTML_B_BR +
                        getResources().getString(R.string.phone_cursor) +
                        InventoryEntry.HTML_B +
                        currentPhoneNumber + InventoryEntry.HTML_B_BR +
                        getResources().getString(R.string.quantity_cursor) +
                        InventoryEntry.HTML_B +
                        currentQuantity + InventoryEntry.HTML_B_BR +
                        getResources().getString(R.string.price_cursor) +
                        InventoryEntry.HTML_B +
                        currentBookPrice + getResources().getString(R.string.unit_cursor)));

                Log.i(LOG_TAG, getResources().getString(R.string.cursor_data) + bookList.toString() + cursor);
            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    /**
     * This method inserts example data into database
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void insertExampleData() {

        // Getting the database in writable mode
        SQLiteDatabase base = dbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and an example book attributes represent the values
        ContentValues values = new ContentValues();

        values.put(InventoryEntry.COLUMN_BOOK_TITLE, getResources().getString(R.string.example_title));
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, InventoryEntry.SUPPLIER_SEVEN);
        values.put(InventoryEntry.COLUMN_PHONE_NUMBER, getResources().getString(R.string.example_phone_number));
        values.put(InventoryEntry.COLUMN_QUANTITY, 1);
        values.put(InventoryEntry.COLUMN_BOOK_PRICE, 27);

        // Inserting the new row into the database
        long newRowId = base.insert(InventoryEntry.TABLE_NAME, null, values);

        String newRow = Objects.toString(newRowId);
        Log.i(LOG_TAG, getResources().getString(R.string.example_data) + values + newRow);

    }

    /**
     * This method deletes all rows from the database
     */
    private void deleteRows() {

        // Getting the database in writable mode
        SQLiteDatabase base = dbHelper.getWritableDatabase();

        // Erasing the database content
        base.delete(InventoryEntry.TABLE_NAME, null, null);

        // Closing the database
        base.close();

    }

    /**
     * This method set the animation for floating button
     */
    private void setFabAnimation(FloatingActionButton floatingButton) {

        final TranslateAnimation ta = new TranslateAnimation(-10, 0, 0, 0);
        ta.setInterpolator(new CycleInterpolator(1));
        ta.setStartOffset(0);
        ta.setDuration(1500);
        ta.setRepeatMode(TranslateAnimation.RESTART);
        ta.setRepeatCount(TranslateAnimation.INFINITE);
        floatingButton.setAnimation(ta);

    }

    /**
     * Menu methods
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_main.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Menu "Insert example data"
        if (item.getItemId() == R.id.action_insert_example_data) {

            insertExampleData();
            displayDatabaseState();

        }

        // Menu "Delete all entries"
        if (item.getItemId() == R.id.action_delete_all_products) {

            deleteRows();
            displayDatabaseState();

        }

        return super.onOptionsItemSelected(item);
    }
}
