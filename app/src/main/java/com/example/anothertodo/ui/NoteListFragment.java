package com.example.anothertodo.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anothertodo.R;
import com.example.anothertodo.Utils;
import com.example.anothertodo.data.Note;

import java.util.ArrayList;

public class NoteListFragment extends Fragment {

    private static final Integer COLUMN_NUMBER = 2;
    private boolean showFavouriteOnly = false;
    private Integer mSelectedNote = -1;
    private RecyclerView mRecycleView;
    private ArrayList<Note> mNotes;


    public NoteListFragment() {
    }

    public static NoteListFragment newInstance() {
        NoteListFragment fragment = new NoteListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static NoteListFragment newInstance(boolean showFavouriteOnly) {
        NoteListFragment fragment = new NoteListFragment();
        Bundle args = new Bundle();
        args.putBoolean(Utils.getShowFavouriteOnlyKey(), showFavouriteOnly);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            showFavouriteOnly = getArguments().getBoolean(Utils.getShowFavouriteOnlyKey(), false);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Utils.getKeyNoteElementKey(), mSelectedNote);
    }

    // TODO: 22-Mar-21 Ask about use findViewByID before ViewCreated
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note_list, container, false);
        mNotes = Utils.getTestNotesList(getResources(), showFavouriteOnly);
        mRecycleView = rootView.findViewById(R.id.note_list_recycle_view);
        initRecycleView();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            if (getResources().getConfiguration().orientation ==
                    Configuration.ORIENTATION_LANDSCAPE)
            mSelectedNote = savedInstanceState.getInt(Utils.getKeyNoteElementKey(), -1);
        }

        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {

            if ((mSelectedNote == -1 && !mNotes.isEmpty())) {
                mSelectedNote = mNotes.get(0).hashCode();
            }

            if (mSelectedNote >= 1) {
                showAtTheEnd(Utils.getNote(getResources(), mSelectedNote));
            }
        }

    }

    private void initRecycleView() {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(COLUMN_NUMBER, StaggeredGridLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        final NoteListRecycleViewAdapter mRecycleAdapter = new NoteListRecycleViewAdapter(mNotes);
        mRecycleView.setAdapter(mRecycleAdapter);
        mRecycleAdapter.setOnItemClickListener((view, position) -> proceedClickOnNote(mNotes.get(position)));
    }

    private void proceedClickOnNote(Note currentNote) {
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            showAtTheEnd(currentNote);
        } else {
            openNote(currentNote);
        }
    }

    private void openNote(Note currentNote) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_workflow, NoteFragment.newInstance(currentNote.getID()));
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showAtTheEnd(Note currentNote) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.note_container, NoteFragment.newInstance(currentNote.getID()));
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }
}