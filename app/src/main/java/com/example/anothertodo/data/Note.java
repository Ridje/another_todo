package com.example.anothertodo.data;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Note extends Object {

    Date mCreatedAt, mModifiedAt;
    String mText;
    String mTitle;
    ArrayList<Task> mTasks;
    ArrayList<Bitmap> mImages;
    Boolean mPinned;
    Integer mColor;
    Integer mID;

    private static volatile Integer maxID = 0;

    @Override
    public int hashCode() {
        return mID;
    }

    public synchronized static Integer getNewID() {
        maxID++;
        return maxID;
    }

    public Integer getID() {
        return mID;
    }

    public ArrayList<Bitmap> getImages() {
        return mImages;
    }

    public String getText() {
        return mText;
    }

    public Date getModifiedAt() {
        return mModifiedAt;
    }

    public String getTitle() {
        return mTitle;
    }

    public Integer getColor() {
        return mColor;
    }

    public ArrayList<Task> getTasks() {
        return mTasks;
    }

    public class Task {
        String mText;
        boolean mCompleted;

        public String getText() {
            return mText;
        }

        public boolean isCompleted() {
            return mCompleted;
        }

        public Task(String text, boolean completed) {
            this.mText = text;
            this.mCompleted = completed;
        }
    }

    public Note(String title, String text, Integer tasksCount, Integer imagesCount, Bitmap imageSource, Boolean pinned, Integer color) {
        this.mID = Note.getNewID();
        this.mCreatedAt = new Date(System.currentTimeMillis());
        this.mModifiedAt = this.mCreatedAt;
        this.mTitle = title;
        this.mText = text;
        this.mPinned = pinned;
        this.mColor = color;

        mTasks = new ArrayList<>();
        for (int i = 0; i < tasksCount; i++) {
            mTasks.add(new Task(String.format("%s task", i), i % 2 == 0));
        }

        mImages = new ArrayList<>();
        for (int i = 0; i < imagesCount; i++) {
            mImages.add(imageSource);
        }
    }

}
