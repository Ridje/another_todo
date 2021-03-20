package com.example.anothertodo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.anothertodo.data.Note;
import java.util.ArrayList;

public class NoteListFragment extends Fragment {

    private static final Integer COLUMN_NUMBER = 2;
    private static Integer mSelectedNote = -1;

    public NoteListFragment() {

    }

    public static NoteListFragment newInstance() {
        NoteListFragment fragment = new NoteListFragment();
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
        outState.putInt(Utils.getKeyNoteElement(), mSelectedNote);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout linearLayoutGeneral = (LinearLayout) inflater.inflate(R.layout.fragment_note_list, container, false);
        return linearLayoutGeneral;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<Note> listOfNotes = Utils.getTestNotesList(getResources());

        LinearLayout linearLayoutGeneral = (LinearLayout) view;
        LinearLayout currentColumn = null;

        for (int i = 0; i < listOfNotes.size(); i++) {
            final Note currentNote = listOfNotes.get(i);
            currentColumn = (LinearLayout) ((ViewGroup) linearLayoutGeneral.getChildAt(i % COLUMN_NUMBER)).getChildAt(0);
            View noteElement = Utils.inflateNote(this, currentColumn, R.layout.note, listOfNotes.get(i), true);

            noteElement.setOnClickListener(v -> {
                if (getResources().getConfiguration().orientation ==
                        Configuration.ORIENTATION_PORTRAIT) {
                    mSelectedNote = currentNote.hashCode();
                    openNote(currentNote);
                } else {
                    showAtTheEnd(currentNote);
                }

            });
        }

        if (savedInstanceState != null) {
            if (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE)
            mSelectedNote = savedInstanceState.getInt(Utils.getKeyNoteElement(), -1);
        }

        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {

            if ((mSelectedNote == -1 && !listOfNotes.isEmpty())) {
                mSelectedNote = listOfNotes.get(0).hashCode();
            }

            if (mSelectedNote >= 1) {
                showAtTheEnd(Utils.getNote(getResources(), mSelectedNote));
            }
        }

    }

    private void openNote(Note elementToAdd) {
        Intent intent = new Intent(getActivity(), NoteActivity.class);
        intent.putExtra(Utils.getKeyNoteElement(), elementToAdd.getID());
        startActivity(intent);
    }

    private void showAtTheEnd(Note currentNote) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.note_container, NoteFragment.newInstance(currentNote.getID()));
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }
}