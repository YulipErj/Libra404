package com.example.libra404;

public class Book {
    private String title;
    private String author;
    private boolean isBorrowed;

    public Book(String title, String author, boolean isBorrowed) {
        this.title = title;
        this.author = author;
        this.isBorrowed = isBorrowed;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }
}
