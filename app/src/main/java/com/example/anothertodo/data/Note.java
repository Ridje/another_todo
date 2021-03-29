package com.example.anothertodo.data;
import android.graphics.Bitmap;

import com.example.anothertodo.Utils;

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

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public void setModifiedAt() {
        mModifiedAt = new Date(System.currentTimeMillis());
    }

    public synchronized static Integer getNewID() {
        maxID++;
        return maxID;
    }

    public void setColor(Integer mColor) {
        this.mColor = mColor;
    }

    public Integer getID() {
        return mID;
    }

    public Boolean getPinned() {
        return mPinned;
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

        public void setText(String mText) {
            this.mText = mText;
        }

        public void setCompleted(boolean mCompleted) {
            this.mCompleted = mCompleted;
        }

        public Note getNote() {
            return Note.this;
        }

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

    public Note(Integer color) {
        this.mID = Note.getNewID();
        this.mCreatedAt = new Date(System.currentTimeMillis());
        this.mModifiedAt = this.mCreatedAt;
        this.mTitle = "";
        this.mText = "";
        this.mPinned = false;
        this.mColor = color;
        mTasks = new ArrayList<>();
        mImages = new ArrayList<>();
    }
}
