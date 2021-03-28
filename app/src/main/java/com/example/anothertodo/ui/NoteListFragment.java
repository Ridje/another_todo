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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.anothertodo.R;
import com.example.anothertodo.Utils;
import com.example.anothertodo.data.Note;
import com.example.anothertodo.observer.NotelistObserver;

import java.util.ArrayList;

public class NoteListFragment extends Fragment implements NotelistObserver {

    private static final Integer COLUMN_NUMBER = 2;
    private boolean showFavouriteOnly = false;
    private Integer mSelectedNote = -1;
    private RecyclerView mRecycleView;
    private ArrayList<Note> mNotes;
    private NoteListRecycleViewAdapter mRecycleViewAdapter;


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
        args.putBoolean(Utils.getKeyShowFavouriteOnly(), showFavouriteOnly);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            showFavouriteOnly = getArguments().getBoolean(Utils.getKeyShowFavouriteOnly(), false);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Utils.getKeyNoteElementHash(), mSelectedNote);
    }

    // TODO: 22-Mar-21 Ask about use findViewByID before ViewCreated
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note_list, container, false);
        setHasOptionsMenu(true);
        mNotes = Utils.getTestNotesList(getResources(), showFavouriteOnly);
        mRecycleView = rootView.findViewById(R.id.note_list_recycle_view);
        initRecycleView();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            mSelectedNote = savedInstanceState.getInt(Utils.getKeyNoteElementHash(), -1);
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
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.options_note_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.add_new_note:
                Note newNote = new Note(Utils.getNextNoteColor(getContext().getResources()));
                mNotes.add(newNote);
                int position = Utils.addNoteToTestNotesList(getContext().getResources(), newNote);
                mRecycleViewAdapter.notifyItemInserted(position);
                mRecycleView.smoothScrollToPosition(position);
                proceedClickOnNote(newNote);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    private void initRecycleView() {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(COLUMN_NUMBER, StaggeredGridLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleViewAdapter = new NoteListRecycleViewAdapter(mNotes);
        mRecycleView.setAdapter(mRecycleViewAdapter);
        mRecycleViewAdapter.setOnItemClickListener((view, position) -> proceedClickOnNote(mNotes.get(position)));
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

    @Override
    public void notifyNotelistChanged() {
        mNotes = Utils.getTestNotesList(getResources(), showFavouriteOnly);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {

            if (!mNotes.isEmpty()) {
                mSelectedNote = mNotes.get(0).hashCode();
            }

            if (mSelectedNote >= 1) {
                showAtTheEnd(Utils.getNote(getResources(), mSelectedNote));
            }
        }
        // TODO: 28-Mar-21 remove it after setting source of data - database 
        mRecycleViewAdapter.changeSource(mNotes);
        mRecycleViewAdapter.notifyDataSetChanged();
    }
}