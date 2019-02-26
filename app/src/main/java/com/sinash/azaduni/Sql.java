package com.sinash.azaduni;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Sql {

    Context context;

    SQLiteDatabase sqLiteDatabase;

    public Sql(Context context) {
        // TODO Auto-generated constructor stub

        this.context = context;

        sqLiteDatabase = context.openOrCreateDatabase("azad.db",
                Context.MODE_PRIVATE, null);

        String tbl_news = "CREATE TABLE IF NOT EXISTS [tbl_news] ([nid] int NOT NULL, "
                + "[type] int NOT NULL, [title] text NOT NULL,"
                + " [text] text NOT NULL, [image] BLOB NOT NULL,"
                + " [link] text NOT NULL, "
                + " [created_at] timestamp NOT NULL,"
                + " [updated_at] timestamp NOT NULL," + " [imageUp] text);";
        sqLiteDatabase.execSQL(tbl_news);
        sqLiteDatabase.close();

    }

    public void insertNews(News news) {

        sqLiteDatabase = context.openOrCreateDatabase("azad.db",
                Context.MODE_PRIVATE, null);

        ContentValues values = new ContentValues();

        values.put("nid", news.getNid());
        values.put("type", news.getType());
        values.put("title", news.getTitle());
        values.put("text", news.getText());
        values.put("link", news.getLink());
        values.put("image", convertBitmap2ByteArray(news.getImage()));
        values.put("created_at", news.getCreated_at());
        values.put("updated_at", news.getUpdated_at());
        values.put("imageUp", news.getImageUp());


        sqLiteDatabase.insert("tbl_news", null, values);
        sqLiteDatabase.close();

    }


    public List<News> select_News() {


        List<News> allNews = new ArrayList<>();

        sqLiteDatabase = context.openOrCreateDatabase("azad.db",
                Context.MODE_PRIVATE, null);

        Cursor cursor = sqLiteDatabase.rawQuery("select * from tbl_news", null);

        if (cursor != null) {

            if (cursor.moveToFirst())
                do {


                    String tbl_news = "CREATE TABLE IF NOT EXISTS [tbl_news] ([nid] int NOT NULL, "
                            + "[type] int NOT NULL, [title] varchar NOT NULL,"
                            + " [text] text NOT NULL, [image] BLOB NOT NULL,"
                            + " [link] varchar NOT NULL, "
                            + " [created_at] timestamp NOT NULL,"
                            + " [updated_at] timestamp NOT NULL," + " [imageUp] VARCHAR);";


                    News news = new News();


                    news.setNid(cursor.getInt(0));
                    news.setType(cursor.getInt(1));
                    news.setTitle(cursor.getString(2));
                    news.setText(cursor.getString(3));
                    news.setImage(convertByteArray2Bitmap(cursor.getBlob(4)));
                    news.setLink(cursor.getString(5));
                    news.setCreated_at(cursor.getString(6));
                    news.setUpdated_at(cursor.getString(7));
                    news.setImageUp(cursor.getString(8));


                    allNews.add(news);


                } while (cursor.moveToNext());

        }


        return allNews;

    }

    byte[] convertBitmap2ByteArray(Bitmap bitmap) {
        byte[] img;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        img = outputStream.toByteArray();
        return img;
    }

    Bitmap convertByteArray2Bitmap(byte[] img) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);

        return bitmap;
    }
}
