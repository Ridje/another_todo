package com.example.anothertodo.data;

import java.util.ArrayList;

public interface DataSource {
    Note getNoteByKey(int key);
    Note getNoteByPosition(int position);
    void deleteNote(int position);
    void deleteNote(Note note);
    void updateNote(Note noteData);
    int addNote();
    ArrayList<Note> getNotes(boolean favouriteOnly);
    boolean isEmpty();
}
