package com.example.libra404;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "libra404.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE books(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT UNIQUE, author TEXT, isBorrowed INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT, role TEXT)");
        ContentValues v1 = new ContentValues();
        v1.put("username","admin");
        v1.put("password","admin");
        v1.put("role","admin");
        db.insert("users",null,v1);
        ContentValues v2 = new ContentValues();
        v2.put("username","student");
        v2.put("password","1234");
        v2.put("role","student");
        db.insert("users",null,v2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS books");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public boolean addBook(String title, String author) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("title", title.trim());
        v.put("author", author.trim());
        long r = db.insert("books", null, v);
        return r != -1;
    }

    public boolean deleteBook(String title) {
        SQLiteDatabase db = getWritableDatabase();
        int r = db.delete("books","title = ?", new String[]{title.trim()});
        return r > 0;
    }

    public boolean borrowBook(String title) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT isBorrowed FROM books WHERE title = ?", new String[]{title.trim()});
        if (!c.moveToFirst()) { c.close(); return false; }
        int b = c.getInt(0);
        c.close();
        if (b == 1) return false;
        ContentValues v = new ContentValues();
        v.put("isBorrowed", 1);
        int r = db.update("books", v, "title = ?", new String[]{title.trim()});
        return r > 0;
    }

    public boolean returnBook(String title) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT isBorrowed FROM books WHERE title = ?", new String[]{title.trim()});
        if (!c.moveToFirst()) { c.close(); return false; }
        int b = c.getInt(0);
        c.close();
        if (b == 0) return false;
        ContentValues v = new ContentValues();
        v.put("isBorrowed", 0);
        int r = db.update("books", v, "title = ?", new String[]{title.trim()});
        return r > 0;
    }

    public ArrayList<String> getAllBooks() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT title, author, isBorrowed FROM books ORDER BY title", null);
        while (c.moveToNext()) {
            String t = c.getString(0);
            String a = c.getString(1);
            int b = c.getInt(2);
            String s = b==1 ? "Borrowed" : "Available";
            list.add(t + " • " + a + " • " + s);
        }
        c.close();
        return list;
    }

    public String getRole(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT role FROM users WHERE username=? AND password=?", new String[]{username.trim(), password.trim()});
        if (c.moveToFirst()) {
            String r = c.getString(0);
            c.close();
            return r;
        }
        c.close();
        return null;
    }

    public boolean addUser(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("username", username.trim());
        v.put("password", password.trim());
        v.put("role", "student");
        long r = db.insert("users", null, v);
        return r != -1;
    }
}
