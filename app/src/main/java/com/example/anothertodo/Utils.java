package com.example.anothertodo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.TimeFormatException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.anothertodo.data.Note;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Utils {

    private static ArrayList<Note> notesList;
    private static int currentColorNumber = 0;
    private static final String KEY_NOTE_ELEMENT = "NoteActivity.NoteElement";

    public static ArrayList<Note> getTestNotesList(Resources resources) {

        initializeNotesList(resources);
        return notesList;
    }

    public static Note getNote(Resources resources, Integer hashCode) {
        initializeNotesList(resources);
        for (Note e: notesList) {
            if (e.hashCode() == hashCode) return e;
        }
        return new Note(getNextNoteColor(resources));
    }

    private static void initializeNotesList(Resources resources) {
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
    }

    public static int getNextNoteColor(Resources resources) {
        int[] androidColors = resources.getIntArray(R.array.androidcolors);
        int result = androidColors[currentColorNumber % androidColors.length];
        currentColorNumber++;
        return result;
    }

    public static String getKeyNoteElement() {
        return KEY_NOTE_ELEMENT;
    }

    public static String getDateTimeInLocalFormat(Context context, Date date) {
        DateFormat longDateFormat = android.text.format.DateFormat.getLongDateFormat(context);
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        return longDateFormat.format(date) + " " + timeFormat.format(date);
    }

    public static View inflateNote(Fragment fragment, ViewGroup rootContainer, int layoutIDtoInflate, Note note, boolean isMultiplyShowing) {

        View noteElement = fragment.getLayoutInflater().inflate(layoutIDtoInflate, rootContainer, false);

        View layout = noteElement.findViewById(R.id.note_element);
        layout.setBackgroundColor(note.getColor());

        TextView title = noteElement.findViewById(R.id.note_title);
        title.setText(note.getTitle());

        LinearLayout imagesListView = noteElement.findViewById(R.id.note_images_list);
        ArrayList<Bitmap> elementImages = note.getImages();
        if (!elementImages.isEmpty()) {
            for (int i = 0; i < elementImages.size(); i++) {

                ImageView image = (ImageView) fragment.getLayoutInflater().inflate(R.layout.note_image, imagesListView, false);
                image.setImageDrawable(new BitmapDrawable(fragment.getContext().getResources(), elementImages.get(i)));
                image.setId(View.generateViewId());
                imagesListView.addView(image);

            }
        }

        LinearLayout tasksListView = noteElement.findViewById(R.id.note_tasks_list);
        ArrayList<Note.Task> elementTasks = note.getTasks();
        if (!elementTasks.isEmpty()) {
            for (int i = 0; i < elementTasks.size(); i++) {

                MaterialCheckBox taskCheckbox = (MaterialCheckBox) fragment.getLayoutInflater().inflate(R.layout.note_task, tasksListView, false);

                taskCheckbox.setChecked(elementTasks.get(i).isCompleted());
                taskCheckbox.setId(View.generateViewId());
                setFlagStrikeThroughText(taskCheckbox, taskCheckbox.isChecked());
                taskCheckbox.setText(elementTasks.get(i).getText());
                taskCheckbox.setClickable(!isMultiplyShowing);
                taskCheckbox.setBackgroundColor(Color.TRANSPARENT);
                if (!isMultiplyShowing) {
                    taskCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        setFlagStrikeThroughText((CheckBox) buttonView, isChecked);
                    });
                }

                tasksListView.addView(taskCheckbox);
            }
        }

        TextView text = noteElement.findViewById(R.id.note_text);
        text.setText(note.getText());

        TextView modifiedAt = noteElement.findViewById(R.id.note_modified_at);
        modifiedAt.setText(Utils.getDateTimeInLocalFormat(fragment.getContext(), note.getModifiedAt()));

        if (isMultiplyShowing) {
            noteElement.setId(View.generateViewId());
            title.setId(View.generateViewId());
            imagesListView.setId(View.generateViewId());
            tasksListView.setId(View.generateViewId());
            text.setId(View.generateViewId());
            modifiedAt.setId(View.generateViewId());
        }

        rootContainer.addView(noteElement);

        return noteElement;

    }

    private static void setFlagStrikeThroughText(TextView textView, boolean isChecked) {
        if (isChecked) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags()  & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }
}
