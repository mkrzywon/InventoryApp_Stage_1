package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;
import com.example.android.inventoryapp.data.ProductDbHelper;

public class EditorActivity extends AppCompatActivity {

    private EditText bookTitle;

    private Spinner supplierNameSpinner;

    private String supplierName = InventoryEntry.SUPPLIER_ONE;

    private EditText phoneNumber;

    private EditText quantity;

    private EditText price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        bookTitle = findViewById(R.id.book_title_field);
        phoneNumber = findViewById(R.id.phone_number_field);
        phoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        quantity = findViewById(R.id.quantity_field);
        price = findViewById(R.id.price_field);
        supplierNameSpinner = findViewById(R.id.supplier_spinner);

        setSpinner();
    }

    /**
     * Getting the information from the editor and inserting it into database
     */
    private void insertBook() {

        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String bookTitleString = bookTitle.getText().toString().trim();
        String quantityString = quantity.getText().toString().trim();
        String priceString = price.getText().toString().trim();

        // Conditions in case of empty entry fields
        if ((bookTitleString.isEmpty() && quantityString.isEmpty()) || (bookTitleString.isEmpty() && priceString.isEmpty()) || (quantityString.isEmpty() && priceString.isEmpty()) || (bookTitleString.isEmpty() || quantityString.isEmpty() || priceString.isEmpty())) {

            Toast.makeText(this, getResources().getString(R.string.unfilled_fields), Toast.LENGTH_LONG).show();
        }

        if (quantityString.isEmpty() && !priceString.isEmpty() && !bookTitleString.isEmpty()) {

            Toast.makeText(this, getResources().getString(R.string.unfilled_quantity), Toast.LENGTH_LONG).show();
        }

        if (priceString.isEmpty() && !quantityString.isEmpty() && !bookTitleString.isEmpty()) {

            Toast.makeText(this, getResources().getString(R.string.unfilled_price), Toast.LENGTH_LONG).show();
        }

        if (bookTitleString.isEmpty() && !priceString.isEmpty() && !quantityString.isEmpty()) {

            Toast.makeText(this, getResources().getString(R.string.unfilled_title), Toast.LENGTH_LONG).show();
        }

        insertData();
    }

    /**
     * If all required fields are filled this method is executed.
     */
    private void insertData() {

        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String phoneNumberString = phoneNumber.getText().toString().trim();
        String bookTitleString = bookTitle.getText().toString().trim();
        String quantityString = quantity.getText().toString().trim();
        String priceString = price.getText().toString().trim();
        int quantityInt;
        Double priceDouble;

        // If all required fields are filled a database entry can be made
        if (!bookTitleString.isEmpty() && !quantityString.isEmpty() && !priceString.isEmpty()) {

            quantityInt = Integer.parseInt(quantityString);
            priceDouble = Double.parseDouble(priceString);

            // Creating database helper
            ProductDbHelper mDbHelper = new ProductDbHelper(this);

            // Getting the database in write mode
            SQLiteDatabase base = mDbHelper.getWritableDatabase();

            // Create a ContentValues object where column names are represented by the keys,
            // and book attributes from the editor are the values.
            ContentValues values = new ContentValues();
            values.put(InventoryEntry.COLUMN_BOOK_TITLE, bookTitleString);
            values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, supplierName);
            values.put(InventoryEntry.COLUMN_PHONE_NUMBER, phoneNumberString);
            values.put(InventoryEntry.COLUMN_QUANTITY, quantityInt);
            values.put(InventoryEntry.COLUMN_BOOK_PRICE, priceDouble);

            // By inserting a new row for book in the database, the ID of that new row is returned.
            long newRowId = base.insert(InventoryEntry.TABLE_NAME, null, values);

            if (newRowId == -1) {
                // If the row ID is -1, then there was an error with insertion.
                Toast.makeText(this, getResources().getString(R.string.database_writing_error), Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(this, getResources().getString(R.string.successful_database_save) + newRowId, Toast.LENGTH_SHORT).show();
            }

            // Exit activity
            finish();

            // Navigate back to parent activity (MainActivity)
            NavUtils.navigateUpFromSameTask(this);

        }
    }

    /**
     * Dropdown spinner that allows the user to select the supplier name.
     */
    private void setSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter supplierListSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_supplier_name, android.R.layout.simple_spinner_item);

        // Setting the dropdown layout style
        supplierListSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        supplierNameSpinner.setAdapter(supplierListSpinnerAdapter);

        // Set the integer mSelected to the constant values
        supplierNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.supplier2))) {
                        supplierName = InventoryEntry.SUPPLIER_TWO;
                    } else if (selection.equals(getString(R.string.supplier3))) {
                        supplierName = InventoryEntry.SUPPLIER_THREE;
                    } else if (selection.equals(getString(R.string.supplier4))) {
                        supplierName = InventoryEntry.SUPPLIER_FOUR;
                    } else if (selection.equals(getString(R.string.supplier5))) {
                        supplierName = InventoryEntry.SUPPLIER_FIVE;
                    } else if (selection.equals(getString(R.string.supplier6))) {
                        supplierName = InventoryEntry.SUPPLIER_SIX;
                    } else if (selection.equals(getString(R.string.supplier7))) {
                        supplierName = InventoryEntry.SUPPLIER_SEVEN;
                    } else {
                        supplierName = InventoryEntry.SUPPLIER_ONE;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                supplierName = InventoryEntry.SUPPLIER_ONE;
            }
        });
    }

    /**
     * Menu methods
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // User clicked on a menu option in the app bar overflow menu
        // Respond to a click on the "Save" menu option
        if (item.getItemId() == R.id.action_save) {

            // Saving book to database
            insertBook();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
