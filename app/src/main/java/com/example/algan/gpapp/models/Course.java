package com.example.algan.gpapp.models;

/**
 * Created by minhazv on 5/3/2017.
 */

public class Course {
    public String Title;
    public String URL;
    public int id;
    public boolean isFavorite;

    public Course(String title, String description, int id, boolean isFav) {
        this.Title = title;
        this.URL = description;
        this.isFavorite = isFav;
        this.id = id;
    }
}
