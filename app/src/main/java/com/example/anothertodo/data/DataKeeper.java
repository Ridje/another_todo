package com.example.anothertodo.data;

import android.content.res.Resources;

import java.util.ArrayList;

public class DataKeeper implements DataSource {
    private static DataKeeper sInstance;
    private DataSource dataSource;

    public static DataKeeper getInstance(Resources resources) {
        DataKeeper instance = sInstance;
        if (instance == null) {
            synchronized (DataKeeper.class) {
                if (sInstance == null) {
                    instance = new DataKeeper(resources);
                    sInstance = instance;
                }
            }
        }
        return instance;
    }

    private DataKeeper(Resources resources) {
        dataSource = new LocalSource(resources);
    }

    @Override
    public Note getNoteByKey(int ID) {
        return dataSource.getNoteByKey(ID);
    }

    @Override
    public Note getNoteByPosition(int position) {
        return dataSource.getNoteByPosition(position);
    }

    @Override
    public void deleteNote(int position) {
        dataSource.deleteNote(position);
    }

    @Override
    public void deleteNote(Note note) {
        dataSource.deleteNote(note);
    }

    @Override
    public void updateNote(Note noteData) {
        dataSource.updateNote(noteData);
    }

    @Override
    public int addNote() {
        return dataSource.addNote();
    }

    @Override
    public ArrayList<Note> getNotes(boolean favouriteOnly) {
        return dataSource.getNotes(favouriteOnly);
    }

    @Override
    public boolean isEmpty() {
        return dataSource.isEmpty();
    }
}
