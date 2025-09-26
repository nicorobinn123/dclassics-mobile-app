// app/src/main/java/com/example/bookspage/Book.java
package com.example.bookspage;

public class Book {
    private String title;
    private String author;
    private String synopsis;
    private int coverResourceId;
    private String price;// This holds the drawable resource ID

    // Constructor
    public Book(String title, String author, String synopsis, int coverResourceId, String price) {
        this.title = title;
        this.author = author;
        this.synopsis = synopsis;
        this.coverResourceId = coverResourceId;
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    // Getter methods
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getSynopsis() {
        return synopsis;
    }

    // *** YOU NEED TO ADD THIS METHOD ***
    public int getCoverResourceId() {
        return coverResourceId;
    }
}