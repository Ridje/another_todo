package com.example.anothertodo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.anothertodo.data.Note;

import java.util.ArrayList;
public class Utils {

    private static ArrayList<Note> notesList;
    private static int currentColorNumber = 0;

    public static ArrayList<Note> getTestNotesList(Resources resources) {

        if (notesList == null) {
            Bitmap defaultImage = BitmapFactory.decodeResource(resources, R.drawable.no_photo);
            notesList = new ArrayList<Note>(){{
                add(new Note("Fix machine", "You could argue that in agreeing to participate in an HBO documentary called Monica in Black and White I had signed up to be shamed and publicly humiliated yet again.", 0, 2, defaultImage, false, getNextNoteColor(resources)));
                add(new Note("Exchange money", "Don't forget to exchange a money", 0, 0, defaultImage, false, getNextNoteColor(resources)));
                add(new Note("Take kids", "Production of the S-10 was ended as part of Rans' extensive reorganization of its product line on 1 June 2006, after having been available for 18 years, but the S-10 was reintroduced in about 2009 and is again available.", 3, 0, defaultImage, false, getNextNoteColor(resources)));
                add(new Note("Send post", "Send email about a new job", 0, 1, defaultImage, false, getNextNoteColor(resources)));
                add(new Note("Don't smoke", "The Rans S-10 Sakota is an American single-engined, tractor configuration, two-seats in side-by-side configuration, mid-wing monoplane designed by Randy Schlitter for aerobatics and manufactured by Rans Inc. The Sakota is available in kit form for amateur construction.", 0, 0, defaultImage, true, getNextNoteColor(resources)));
                add(new Note("Don't drink", "You threw it", 0, 0, defaultImage, false, getNextNoteColor(resources)));
                add(new Note("Story", "The untold story of how Lisa Howard’s intimate diplomacy with Cuba’s revolutionary leader changed the course of the Cold War.", 0, 0, defaultImage, true, getNextNoteColor(resources)));
                add(new Note("Shame and Survival", "She tried public appearances. She tried being reclusive. She tried leaving the country, and she tried finding a job. But the epic humiliation of 1998, when her affair with Bill Clinton became an all-consuming story, has followed Monica Lewinsky every day.", 0, 0, defaultImage, false, getNextNoteColor(resources)));
            }};
        }
        return notesList;
    }

    public static int getNextNoteColor(Resources resources) {
        int[] androidColors = resources.getIntArray(R.array.androidcolors);
        int result = androidColors[currentColorNumber % androidColors.length];
        currentColorNumber++;
        return result;
    }
}
