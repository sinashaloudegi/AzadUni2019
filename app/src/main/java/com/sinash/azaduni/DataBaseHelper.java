package com.sinash.azaduni;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    static DataBaseHelper Dbhelper = null;
    String DB_PATH = "/data/data/" + this.getClass().getPackage().getName() + "/databases/";


    private static String DB_NAME = "azad.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        this.myContext = context;

    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();


            } catch (IOException e) {

                throw new Error("Error copying database");

            }

        }
    }

    static synchronized SQLiteDatabase open() throws SQLException {
        return Dbhelper.getWritableDatabase();
    }


    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();

    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */

    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        myContext.getAssets().open(DB_NAME);


        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }


    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertBitmap(Bitmap bm, String columnName, String table, String where) {

        // Convert the image into byte array

        // Open the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Start the transaction.
        db.beginTransaction();
        ContentValues values;

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] buffer = out.toByteArray();
            values = new ContentValues();
            values.put(columnName, buffer);
            // Insert Row
            long i = db.update(table, values, where, null);

            Log.i("Insert", i + "");
            // Insert into database successfully.
            db.setTransactionSuccessful();
            Log.e("Database_Try?", "OK!!!");

        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e("Database_Try?", "Faild!!!");


        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
    }

    public Bitmap getBitmap(int id) {
        Bitmap bitmap = null;
        // Open the database for reading
        SQLiteDatabase db = this.getReadableDatabase();
        // Start the transaction.
        db.beginTransaction();

        try {
            String selectQuery = "SELECT * FROM " + "Image";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // Convert blob data to byte array
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex("ImageBlob"));
                    // Convert the byte array to Bitmap
                    bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);

                }

            }
            db.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            // End the transaction.
            db.close();
            // Close database
        }
        return bitmap;

    }
}