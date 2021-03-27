package com.example.anothertodo.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.anothertodo.R;
import com.example.anothertodo.Utils;
import com.example.anothertodo.data.Note;


public class NoteFragment extends Fragment {

    private Note mNote;

    public NoteFragment() {

    }

    public static NoteFragment newInstance(Integer noteID) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putInt(Utils.getKeyNoteElementKey(), noteID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mNote = Utils.getNote(getResources(), (getArguments().getInt(Utils.getKeyNoteElementKey(), -1)));
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