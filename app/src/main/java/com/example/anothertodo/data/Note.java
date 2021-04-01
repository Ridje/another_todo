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

public class Note extends Object implements Parcelable {

    Date mCreatedAt, mModifiedAt;
    String mText;
    String mTitle;
    ArrayList<Task> mTasks;
    ArrayList<Bitmap> mImages;
    Boolean mPinned;
    Integer mColor;
    Integer mID;

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    private static volatile AtomicInteger maxID = new AtomicInteger(0);

    protected Note(Parcel in) {
        mText = in.readString();
        mTitle = in.readString();
        mImages = in.createTypedArrayList(Bitmap.CREATOR);
        byte tmpMPinned = in.readByte();
        mPinned = tmpMPinned == 0 ? null : tmpMPinned == 1;
        if (in.readByte() == 0) {
            mColor = null;
        } else {
            mColor = in.readInt();
        }
        if (in.readByte() == 0) {
            mID = null;
        } else {
            mID = in.readInt();
        }
    }

    public void setCreatedAt(Date createdAt) {
        this.mCreatedAt = createdAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.mModifiedAt = modifiedAt;
    }

    public void setPinned(Boolean pinned) {
        this.mPinned = pinned;
    }

    public void setID(Integer ID) {
        this.mID = ID;
    }

    public Note() {

    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public Integer getID() {
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
        return maxID.incrementAndGet();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mText);
        dest.writeString(mTitle);
        dest.writeTypedList(mImages);
        dest.writeByte((byte) (mPinned == null ? 0 : mPinned ? 1 : 2));
        if (mColor == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mColor);
        }
        if (mID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mID);
        }
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

    public Note(String title, String text, Integer tasksCount, Integer imagesCount, Boolean pinned, Integer color) {
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
