package com.example.anothertodo.data;

import java.util.ArrayList;

public interface DataSource {

    void addListener(DataChangedListener listener);
    void removeListener(DataChangedListener listener);

    Note getNoteByKey(String key);
    Note getNoteByPosition(int position);
    void deleteNote(int position);
    void deleteNote(Note note);
    void updateNote(Note noteData);
    void addNote();
    ArrayList<Note> getNotes(boolean favouriteOnly);
    boolean isEmpty();
}
