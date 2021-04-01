package com.example.anothertodo.data;

import android.content.Context;

import com.example.anothertodo.Preferences;

import java.util.ArrayList;

public class DataKeeper implements DataSource {
    private static DataKeeper sInstance;
    private DataSource dataSource;

    public static DataKeeper getInstance(Context context) {
        DataKeeper instance = sInstance;
        if (instance == null) {
            synchronized (DataKeeper.class) {
                if (sInstance == null) {
                    instance = new DataKeeper(context);
                    sInstance = instance;
                }
            }
        }
        return instance;
    }

    private DataKeeper(Context context) {
        Preferences settings = Preferences.getInstance(context);
        if (settings.read(Preferences.getKeySettingsUseCloud(), false)) {
            dataSource = new CloudSource();
        } else {
            dataSource = new LocalSource(context.getResources());
        }
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
