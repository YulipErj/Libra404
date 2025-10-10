package com.example.libra404;

public class BorrowHistory {
    private String book;
    private String borrowDate;
    private String dueDate;
    private String returnDate;

    public BorrowHistory(String book, String borrowDate, String dueDate, String returnDate) {
        this.book = book;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
    }

    public String getBook() {
        return book;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }
}
