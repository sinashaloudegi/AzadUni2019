package com.sinash.azaduni;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;

public class TestAdapter {
    protected static final String TAG = "DataAdapter";


    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;

    public TestAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public TestAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public TestAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            //   Log.e(TAG, "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public Cursor getTestData(String coulmnName, String tbName) {
        try {
            String sql = "SELECT " + coulmnName + " FROM " + tbName;
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            //  Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getTestData(String coulmnName, String tbName, String whereName, String whereValue) {
        try {
            String sql = "SELECT " + coulmnName + " FROM " + tbName + " WHERE " + whereName + " = " + whereValue;
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            //  Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getTestData(String coulmnName, String tbName, String GroupBy) {
        try {
            String sql = "SELECT " + coulmnName + " FROM " + tbName + " GROUP BY " + GroupBy;
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
    }

    void update(int Id_Book) {

        String updateQuery = " UPDATE TbBook SET Vaild=0 WHERE _id = " + 1;
        mDb.execSQL(updateQuery);
    }

    public Cursor getTestData(String coulmnName, String tbName, String whereName, String whereValue, String whereName2, String whereValue2) {
        try {
            String sql = "SELECT " + coulmnName + " FROM " + tbName + " WHERE " + whereName + " = " + whereValue + " AND " + whereName2 + " = " + whereValue2;
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            //     Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getTestData(String query) {
        try {
            String sql = query;
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }
}