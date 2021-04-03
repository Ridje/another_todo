package com.example.anothertodo.data;

import android.content.Context;
import android.os.LocaleList;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;

import com.example.anothertodo.Preferences;

import java.util.ArrayList;

public class DataKeeper implements DataSource{
    private static DataKeeper sInstance;
    private DataSource dataSource;
    private static final String TAG = "[SourceProvider]";

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
        instance.updateDataSource(context);
        return instance;
    }

    public void updateDataSource(Context context) {
        Preferences settings = Preferences.getInstance(context);
        boolean useCloud = settings.read(Preferences.getKeySettingsUseCloud(), false);
        if (useCloud && !(dataSource instanceof CloudSource)) {
            try {
                dataSource = new CloudSource(settings.read(Preferences.getKeySettingsUserEmail(), null));
            } catch (UserNotAuthenticatedException e) {
                Log.w(TAG, "Can't login, some problem with login in settings: " + e.getMessage());
            }
        } else if (!useCloud && !(dataSource instanceof LocalSource)) {
            dataSource = new LocalSource(context.getResources());
        }
    }

    private DataKeeper(Context context) {
        Preferences settings = Preferences.getInstance(context);
        if (settings.read(Preferences.getKeySettingsUseCloud(), false)) {
            try {
                dataSource = new CloudSource(settings.read(Preferences.getKeySettingsUserEmail(), null));
            } catch (UserNotAuthenticatedException e) {
                Log.w(TAG, "Can't login, some problem with login in settings: " + e.getMessage());
            }
        } else {
            dataSource = new LocalSource(context.getResources());
        }
    }

    @Override
    public Note getNoteByKey(String ID) {
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
    public void addNote() {
        dataSource.addNote();
    }

    @Override
    public ArrayList<Note> getNotes(boolean favouriteOnly) {
        return dataSource.getNotes(favouriteOnly);
    }

    @Override
    public boolean isEmpty() {
        return dataSource.isEmpty();
    }

    @Override
    public void addListener(DataChangedListener listener) {
        dataSource.addListener(listener);
    }

    @Override
    public void removeListener(DataChangedListener listener) {
        dataSource.removeListener(listener);
    }
}
