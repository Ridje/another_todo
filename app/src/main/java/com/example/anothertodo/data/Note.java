package com.example.anothertodo.data;

import android.graphics.drawable.Drawable;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Note {

    Timestamp createdAt, modifyedAt;
    String text;
    String title;
    ArrayList<Task> tasks;
    ArrayList<Drawable> images;
    Boolean pinned;

    protected class Task {
        String text;
        boolean completed;
    }

}
