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

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.anothertodo.R;
import com.example.anothertodo.Utils;
import com.example.anothertodo.data.DataChangedListener;
import com.example.anothertodo.data.DataKeeper;
import com.example.anothertodo.data.Note;

public class NoteListFragment extends Fragment {

    private static final Integer COLUMN_NUMBER = 2;
    private boolean showFavouriteOnly = false;
    private Integer mSelectedNote = -1;
    private RecyclerView mRecycleView;
    private NoteListRecycleViewAdapter mRecycleViewAdapter;
    private DataKeeper dataSource;
    DataChangedListener thisListener;

    public NoteListFragment() {
    }

    @Override
    public void onDetach() {
        super.onDetach();
        dataSource.removeListener(thisListener);
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
        dataSource = DataKeeper.getInstance(getContext());
        thisListener = new DataChangedListener() {
            @Override
            public void onItemAdded(int position) {
                mRecycleViewAdapter.notifyItemInserted(position);
                mRecycleView.smoothScrollToPosition(position);
                proceedClickOnNote(dataSource.getNoteByPosition(position));
            }

            @Override
            public void onItemRemoved(int position) {
                mRecycleViewAdapter.notifyItemRemoved(position);
                if (getResources().getConfiguration().orientation ==
                        Configuration.ORIENTATION_LANDSCAPE) {
                    if (!dataSource.isEmpty()) {
                        mSelectedNote = 0;
                    }

                    if (mSelectedNote >= 0) {
                        showAtTheEnd(dataSource.getNoteByPosition(mSelectedNote));
                    }
                }
            }

            @Override
            public void onItemUpdated(int position) {
            }

            @Override
            public void onDataSetChanged() {
                mRecycleViewAdapter.notifyDataSetChanged();
            }
        };
        dataSource.addListener(thisListener);

        if (getArguments() != null) {
            showFavouriteOnly = getArguments().getBoolean(Utils.getKeyShowFavouriteOnly(), false);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Utils.getKeyNotePosition(), mSelectedNote);
    }

    // TODO: 22-Mar-21 Ask about use findViewByID before ViewCreated
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note_list, container, false);
        setHasOptionsMenu(true);
        mRecycleView = rootView.findViewById(R.id.note_list_recycle_view);
        initRecycleView();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            mSelectedNote = savedInstanceState.getInt(Utils.getKeyNotePosition(), -1);
        }

        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {

            if ((mSelectedNote == -1 && !dataSource.isEmpty())) {
                mSelectedNote = 0;
            }

            if (mSelectedNote >= 0) {
                showAtTheEnd(dataSource.getNoteByPosition(mSelectedNote));
            }
        }

    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.options_note_list_menu, menu);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.note_list_item_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.context_action_delete:
                if (mSelectedNote != -1) {
                    dataSource.deleteNote(mSelectedNote);
                    return true;
                }
        }
        return super.onContextItemSelected(item);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.add_new_note:
                dataSource.addNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    private void initRecycleView() {
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(COLUMN_NUMBER, StaggeredGridLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(layoutManager);
        mRecycleViewAdapter = new NoteListRecycleViewAdapter(dataSource.getNotes(false), this);
        mRecycleView.setAdapter(mRecycleViewAdapter);
        mRecycleViewAdapter.setOnItemClickListener(new NoteListRecycleViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                proceedClickOnNote(dataSource.getNoteByPosition(position));
            }

            @Override
            public void onLongItemClick(View view, int position) {
                mSelectedNote = position;
            }
        });


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