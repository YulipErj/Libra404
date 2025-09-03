package com.example.libra404;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "libra404.db";
    private static final int DB_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE books(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT UNIQUE, author TEXT, isBorrowed INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT, role TEXT)");
        db.execSQL("CREATE TABLE borrow_history(id INTEGER PRIMARY KEY AUTOINCREMENT, student TEXT, book TEXT, borrow_date TEXT, due_date TEXT, return_date TEXT)");
        db.execSQL("INSERT INTO users(username, password, role) VALUES('admin', 'admin123', 'admin')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS books");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS borrow_history");
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

    public boolean borrowBook(String title, String student, String borrowDate, String dueDate) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT isBorrowed FROM books WHERE title = ?", new String[]{title.trim()});
        if (!c.moveToFirst()) { c.close(); return false; }
        int b = c.getInt(0);
        c.close();
        if (b == 1) return false;
        ContentValues v = new ContentValues();
        v.put("isBorrowed", 1);
        int r = db.update("books", v, "title = ?", new String[]{title.trim()});
        if (r > 0) {
            ContentValues h = new ContentValues();
            h.put("student", student);
            h.put("book", title.trim());
            h.put("borrow_date", borrowDate);
            h.put("due_date", dueDate);
            db.insert("borrow_history", null, h);
            return true;
        }
        return false;
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
        if (r > 0) {
            db.execSQL("UPDATE borrow_history SET return_date = date('now') WHERE book = ? AND return_date IS NULL", new String[]{title.trim()});
            return true;
        }
        return false;
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

    public Cursor searchBooks(String keyword) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT title, author, isBorrowed FROM books WHERE title LIKE ? OR author LIKE ? ORDER BY title",
                new String[]{"%" + keyword + "%", "%" + keyword + "%"});
    }

    public ArrayList<String> getBorrowHistory(String student) {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT book, borrow_date, due_date, return_date FROM borrow_history WHERE student=? ORDER BY borrow_date DESC", new String[]{student});
        while (c.moveToNext()) {
            String b = c.getString(0);
            String bd = c.getString(1);
            String dd = c.getString(2);
            String rd = c.getString(3);
            if (rd == null) rd = "Not returned";
            list.add(b + " • Borrowed: " + bd + " • Due: " + dd + " • Returned: " + rd);
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
