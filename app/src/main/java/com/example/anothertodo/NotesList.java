package com.example.anothertodo;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anothertodo.data.Note;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class NotesList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    HashMap<Integer, HashMap<String, Integer>> savedViewIds = new HashMap<Integer, HashMap<String, Integer>>(){};

    private static final Integer COLUMN_NUMBER = 2;
    private static final String LAYOUT_ID_KEY_ = "layout";
    private static final String TITLE_ID_KEY = "title";
    private static final String TEXT_ID_KEY = "text";
    private static final String MAP_SAVED_IDS_KEY = "notes_saved_view_id";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotesList() {

    }

    // TODO: Rename and change types and number of parameters
    public static NotesList newInstance() {
        NotesList fragment = new NotesList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(MAP_SAVED_IDS_KEY, savedViewIds);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            HashMap<Integer, HashMap<String, Integer>> restoredIds = (HashMap<Integer, HashMap<String, Integer>>) savedInstanceState.getSerializable(MAP_SAVED_IDS_KEY);
            if (restoredIds != null) {
                savedViewIds = restoredIds;
            }
        }
        ArrayList<Note> listOfNotes = Utils.getTestNotesList(getResources());
        LinearLayout linearLayoutGeneral = (LinearLayout) inflater.inflate(R.layout.fragment_notes_list, container, false);

        for (int i = 0; i < listOfNotes.size(); i++) {
            addNoteToColumn((LinearLayout) ((ViewGroup) linearLayoutGeneral.getChildAt(i % COLUMN_NUMBER)).getChildAt(0), listOfNotes.get(i));
        }

        return linearLayoutGeneral;
    }

    private void addNoteToColumn(LinearLayout column, Note elementToAdd) {

        View noteElement = getLayoutInflater().inflate(R.layout.note_layout, column, false);
        HashMap<String, Integer> pairsOfIDsForNote = getViewsIdForElement(elementToAdd.hashCode());

        View layout = noteElement.findViewById(R.id.note_element);
        layout.setId(pairsOfIDsForNote.get(LAYOUT_ID_KEY_));
        layout.setBackgroundColor(elementToAdd.getColor());

        TextView title = noteElement.findViewById(R.id.note_title);
        title.setText(elementToAdd.getTitle());
        title.setId(pairsOfIDsForNote.get(TITLE_ID_KEY));

        LinearLayout imagesListView = noteElement.findViewById(R.id.note_images_list);
        ArrayList<Bitmap> elementImages = elementToAdd.getmImages();
        if (!elementImages.isEmpty()) {
            for (int i = 0; i < elementImages.size(); i++) {

                ImageView image = (ImageView) getLayoutInflater().inflate(R.layout.note_image, imagesListView, false);
                image.setImageDrawable(new BitmapDrawable(getResources(), elementImages.get(i)));
                image.setId(View.generateViewId());
                imagesListView.addView(image);

            }
        }

        LinearLayout tasksListView = noteElement.findViewById(R.id.note_tasks_list);
        ArrayList<Note.Task> elementTasks = elementToAdd.getmTasks();
        if (!elementTasks.isEmpty()) {
            for (int i = 0; i < elementTasks.size(); i++) {

                MaterialCheckBox taskCheckbox = (MaterialCheckBox) getLayoutInflater().inflate(R.layout.note_task, tasksListView, false);

                taskCheckbox.setChecked(elementTasks.get(i).isCompleted());
                taskCheckbox.setId(View.generateViewId());
                setStrikeThroughText(taskCheckbox, taskCheckbox.isChecked());
                taskCheckbox.setText(elementTasks.get(i).getText());
                taskCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    setStrikeThroughText((CheckBox) buttonView, isChecked);
                });

                tasksListView.addView(taskCheckbox);

            }
        }

        TextView text = noteElement.findViewById(R.id.note_text);
        text.setText(elementToAdd.getText());
        text.setId(pairsOfIDsForNote.get(TEXT_ID_KEY));

        TextView modifiedAt = noteElement.findViewById(R.id.note_modified_at);
        DateFormat dateFormat = android.text.format.DateFormat.getLongDateFormat(getContext());
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(getContext());
        modifiedAt.setText(dateFormat.format(elementToAdd.getModifiedAt()) + " " + timeFormat.format(elementToAdd.getModifiedAt()));
        modifiedAt.setId(View.generateViewId());

        column.addView(noteElement);
    }

    private void setStrikeThroughText(CheckBox checkBox, boolean isChecked) {
        if (isChecked) {
            checkBox.setPaintFlags(checkBox.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            checkBox.setPaintFlags(checkBox.getPaintFlags()  & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    private HashMap<String, Integer> getViewsIdForElement(Integer elementHashcode) {
        HashMap<String, Integer> result = savedViewIds.get(elementHashcode);
        if (result == null) {
            result = new HashMap<>();
            result.put(TITLE_ID_KEY, View.generateViewId());
            result.put(TEXT_ID_KEY, View.generateViewId());
            result.put(LAYOUT_ID_KEY_, View.generateViewId());
            savedViewIds.put(elementHashcode, result);
        }
        return result;
    }
}