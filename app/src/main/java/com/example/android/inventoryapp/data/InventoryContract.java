package com.example.android.inventoryapp.data;

import android.provider.BaseColumns;

/**
 * API Contract for the InventoryApp.
 */
public final class InventoryContract {

    // Giving an empty constructor to set this class as a unique one.
    private InventoryContract() {}

    /**
     * Inner class that defines constant values for the inventory database table.
     * Each entry in the table represents a single book.
     */
    public static final class InventoryEntry implements BaseColumns {

        /** Name of database table for books */
        public static final String TABLE_NAME = "books";

        /**
         * Unique ID number for the book.
         *
         * Type: INTEGER
         */
        public static final String _ID = BaseColumns._ID;

        /**
         * Book name.
         *
         * Type: TEXT
         */
        public static final String COLUMN_BOOK_TITLE ="BookTitle";

        /**
         * Price of the book.
         *
         * Type: REAL
         */
        public static final String COLUMN_BOOK_PRICE = "BookPrice";

        /**
         * Quantity.
         *
         * Type: INTEGER
         */
        public static final String COLUMN_QUANTITY = "Quantity";

        /**
         * Supplier name.
         *
         * The only possible values are {@link #SUPPLIER_ONE}, {@link #SUPPLIER_TWO},
         * {@link #SUPPLIER_THREE}, {@link #SUPPLIER_FOUR}, {@link #SUPPLIER_FIVE},
         * {@link #SUPPLIER_SIX}, {@link #SUPPLIER_SEVEN}.
         *
         * Type: INTEGER
         */
        public static final String COLUMN_SUPPLIER_NAME = "SupplierName";

        /**
         * Supplier phone number.
         *
         * Type: TEXT
         */
        public static final String COLUMN_PHONE_NUMBER = "SupplierPhoneNumber";

        /**
         * Possible values for the supplier's name:
         */
        public static final String SUPPLIER_ONE = "Supplier One";
        public static final String SUPPLIER_TWO = "Supplier Two";
        public static final String SUPPLIER_THREE = "Supplier Three";
        public static final String SUPPLIER_FOUR = "Supplier Four";
        public static final String SUPPLIER_FIVE = "Supplier Five";
        public static final String SUPPLIER_SIX = "Supplier Six";
        public static final String SUPPLIER_SEVEN = "Supplier Seven";

        /**
         * From Html tags
         */
        public static final String HTML_DOUBLE_BR = "<br><br>";
        public static final String HTML_BR = "<br>";
        public static final String HTML_B_BR = "</b><br>";
        public static final String HTML_B = "<b>";
        public static final String HTML_B2 = "</b>";
    }
}


