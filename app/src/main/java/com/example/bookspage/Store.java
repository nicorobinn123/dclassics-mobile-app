package com.example.bookspage; // Make sure this matches your package name

public class Store {
    private String name;
    private int imageResId; // Resource ID for the drawable image

    public Store(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}