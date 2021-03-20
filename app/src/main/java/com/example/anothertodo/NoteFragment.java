package com.example.anothertodo;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anothertodo.data.Note;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.text.DateFormat;
import java.util.ArrayList;


public class NoteFragment extends Fragment {

    private Note mNote;

    public NoteFragment() {

    }

    public static NoteFragment newInstance(Integer noteID) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putInt(Utils.getKeyNoteElement(), noteID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNote = Utils.getNote(getResources(), (getArguments().getInt(Utils.getKeyNoteElement(), -1)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        FrameLayout noteView = (FrameLayout) inflater.inflate(R.layout.fragment_note, container, false);
        return noteView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.inflateNote(this, (ViewGroup) view, R.layout.note_edit, mNote, false);
    }
}