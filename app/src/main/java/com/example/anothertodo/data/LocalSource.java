package com.example.anothertodo.data;


import android.content.res.Resources;

import com.example.anothertodo.R;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class LocalSource implements DataSource{
    
    private static final ArrayList<Note> notes = new ArrayList<>();
    private static AtomicInteger currentColorNumber = new AtomicInteger(0);
    private static int[] androidColors;

    public LocalSource(Resources resources) {

        androidColors = resources.getIntArray(R.array.android_colors);

        notes.add(new Note("Fix machine", "You could argue that in agreeing to participate in an HBO documentary called Monica in Black and White I had signed up to be shamed and publicly humiliated yet again.", 4, 0,  false, getNextNoteColor()));
        notes.add(new Note("Exchange money", "Don't forget to exchange a money", 0, 0,  false, getNextNoteColor()));
        notes.add(new Note("Take kids", "Production of the S-10 was ended as part of Rans' extensive reorganization of its product line on 1 June 2006, after having been available for 18 years, but the S-10 was reintroduced in about 2009 and is again available.", 3, 0,  false, getNextNoteColor()));
        notes.add(new Note("Send post", "Send email about a new job", 0, 0,  false, getNextNoteColor()));
        notes.add(new Note("Don't smoke", "The Rans S-10 Sakota is an American single-engined, tractor configuration, two-seats in side-by-side configuration, mid-wing monoplane designed by Randy Schlitter for aerobatics and manufactured by Rans Inc. The Sakota is available in kit form for amateur construction.", 0, 0,  true, getNextNoteColor()));
        notes.add(new Note("Don't drink", "You threw it", 0, 0,  false, getNextNoteColor()));
        notes.add(new Note("Story", "The untold story of how Lisa Howard’s intimate diplomacy with Cuba’s revolutionary leader changed the course of the Cold War.", 0, 0,  true, getNextNoteColor()));
        notes.add(new Note("Shame and Survival", "She tried public appearances. She tried being reclusive. She tried leaving the country, and she tried finding a job. But the epic humiliation of 1998, when her affair with Bill Clinton became an all-consuming story, has followed Monica Lewinsky every day.", 0, 0,  false, getNextNoteColor()));
        notes.add(new Note("Fix machine", "You could argue that in agreeing to participate in an HBO documentary called Monica in Black and White I had signed up to be shamed and publicly humiliated yet again.", 0, 0,  false, getNextNoteColor()));
        notes.add(new Note("Exchange money", "Don't forget to exchange a money", 0, 0,  true, getNextNoteColor()));
        notes.add(new Note("Take kids", "task text", 0, 0,  false, getNextNoteColor()));
        notes.add(new Note("Send post", "Send email", 0, 0,  false, getNextNoteColor()));
        notes.add(new Note("Don't smoke", "The Rans S-10 Sakota is an American single-engined, tractor configuration, two-seats in side-by-side configuration, mid-wing monoplane designed by Randy Schlitter for aerobatics and manufactured by Rans Inc. The Sakota is available in kit form for amateur construction.", 0, 0,  true, getNextNoteColor()));
        notes.add(new Note("Don't drink", "You threw it", 0, 0,  false, getNextNoteColor()));
        notes.add(new Note("Story", "The untold story of how Lisa Howard’s intimate diplomacy with Cuba’s revolutionary leader changed the course of the Cold War.", 0, 0,  true, getNextNoteColor()));
        notes.add(new Note("Shame and Survival", "She tried public appearances. She tried being reclusive. She tried leaving the country, and she tried finding a job. But the epic humiliation of 1998, when her affair with Bill Clinton became an all-consuming story, has followed Monica Lewinsky every day.", 0, 0,  false, getNextNoteColor()));

    }

    private int getNextNoteColor() {
        int result = androidColors[currentColorNumber.getAndIncrement() % androidColors.length];
        return result;
    }

    @Override
    public Note getNoteByKey(int ID) {
        return notes.stream().filter(note -> note.getID().equals(ID)).findFirst().get();
    }

    @Override
    public Note getNoteByPosition(int position) {
        return notes.get(position);
    }

    @Override
    public void deleteNote(int position) {
        notes.remove(position);
    }

    @Override
    public void deleteNote(Note note) {
        notes.remove(note);
    }

    @Override
    public void updateNote(Note noteData) {
    }

    @Override
    public int addNote() {
        notes.add(new Note(getNextNoteColor()));
        return notes.size() - 1;
    }

    @Override
    public ArrayList<Note> getNotes(boolean favouriteOnly) {
        return notes;
    }

    @Override
    public boolean isEmpty() {
        return notes.isEmpty();
    }
}
