package com.example.anothertodo.data;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.anothertodo.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Note {

    Date mCreatedAt, mModifiedAt;
    String mText;
    String mTitle;
    ArrayList<Task> mTasks = new ArrayList<>();
    ArrayList<Bitmap> mImages = new ArrayList<>();
    Boolean mPinned;
    Integer mColor;
    String mID;

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    private static volatile AtomicInteger maxID = new AtomicInteger(0);

    public void setCreatedAt(Date createdAt) {
        this.mCreatedAt = createdAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.mModifiedAt = modifiedAt;
    }

    public void setPinned(Boolean pinned) {
        this.mPinned = pinned;
    }

    public void setID(String ID) {
        this.mID = ID;
    }

    public Note() {
        this.mID = Note.getNewID();
        this.mCreatedAt = new Date(System.currentTimeMillis());
        this.mModifiedAt = this.mCreatedAt;
        this.mTitle = "";
        this.mText = "";
        this.mPinned = false;
        this.mColor = Utils.getNextNoteColor();
    }

    public Note(String title, String text, Integer tasksCount, Integer imagesCount, Boolean pinned, Integer color) {
        this.mID = Note.getNewID();
        this.mCreatedAt = new Date(System.currentTimeMillis());
        this.mModifiedAt = this.mCreatedAt;
        this.mTitle = title;
        this.mText = text;
        this.mPinned = pinned;
        this.mColor = color;

        for (int i = 0; i < tasksCount; i++) {
            mTasks.add(new Task(String.format("%s task", i), i % 2 == 0));
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
    }

    public String getID() {
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

    public synchronized static String getNewID() {
        return String.valueOf(maxID.incrementAndGet());
    }

    public void setColor(Integer mColor) {
        this.mColor = mColor;
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

}
