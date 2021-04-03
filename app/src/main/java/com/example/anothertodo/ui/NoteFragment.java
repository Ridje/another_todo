package com.example.anothertodo.ui;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.anothertodo.R;
import com.example.anothertodo.Utils;
import com.example.anothertodo.data.DataKeeper;
import com.example.anothertodo.data.Note;
import com.example.anothertodo.data.OnDialogListener;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;


public class NoteFragment extends Fragment implements OnDialogListener {

    private Note mNote;
    private DataKeeper dataKeeper;

    private static TypedArray colorValues;
    private static TypedArray colorIcons;

    private ArrayList<MenuItem> menuItems = new ArrayList<>();
    private SubMenu mRootColorChoose;

    private ViewGroup mNoteElementView;
    private MaterialTextView mHashCodeView;
    private TextInputEditText mTitleView;
    private ViewGroup mImageListContainer;
    private ViewGroup mTaskListContainer;
    private TextInputEditText mTextView;
    private TextInputEditText mModifiedAtView;
    private ImageButton mButtonAddTask;

    public NoteFragment() {
    }

    public static NoteFragment newInstance(String notePosition) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putString(Utils.getKeyNoteId(), notePosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataKeeper = DataKeeper.getInstance(getContext());
        if (getArguments() != null) {
            mNote = dataKeeper.getNoteByKey(getArguments().getString(Utils.getKeyNoteId()));
        }
        if (savedInstanceState != null && getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT) {
            FragmentManager fragmentManager = requireFragmentManager();
            FragmentTransaction myTr = fragmentManager.beginTransaction();
            myTr.remove(this);
            myTr.commit();
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup fragmentRoot = (ViewGroup) inflater.inflate(R.layout.fragment_note, container, false);
        mNoteElementView = (ViewGroup) inflater.inflate(R.layout.note_edit, fragmentRoot, true);
        mTitleView = mNoteElementView.findViewById(R.id.note_title);
        mImageListContainer = mNoteElementView.findViewById(R.id.note_images_list);
        mTaskListContainer = mNoteElementView.findViewById(R.id.note_tasks_list);
        mTextView = mNoteElementView.findViewById(R.id.note_text);
        mModifiedAtView = mNoteElementView.findViewById(R.id.note_modified_at);
        mButtonAddTask = mNoteElementView.findViewById(R.id.note_task_add_button);
        mButtonAddTask.setOnClickListener(v -> {
            mNote.getTasks().add(mNote.new Task("", false));
            updateTasksView();
            dataKeeper.updateNote(mNote);
        });
        mTitleView.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                updateTitle();
                dataKeeper.updateNote(mNote);
            }
        });
        mTextView.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                updateText();
                dataKeeper.updateNote(mNote);
            }
        });



        return fragmentRoot;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.note_edit_fragment_option_menu, menu);

    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (menuItems.isEmpty()) {
            MenuItem menuColors = menu.findItem(R.id.menu_item_choose_colors);
            mRootColorChoose = menuColors.getSubMenu();

            for (int i = 0; i < colorIcons.length(); i++) {
                MenuItem addedColorItem = menuColors.getSubMenu().add("");
                addedColorItem.setIcon(colorIcons.getDrawable(i));
                menuItems.add(addedColorItem);
                if (mNote.getColor().equals(colorValues.getColor(i, 0))) {
                    menuColors.getSubMenu().setIcon(colorIcons.getDrawable(i));
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        for (int i = 0; i < menuItems.size(); i++) {
            if (menuItems.get(i).equals(item)) {
                mNote.setColor(colorValues.getColor(i, mNote.getColor()));
                updateNoteViewBackgroundColor();
                dataKeeper.updateNote(mNote);
                mRootColorChoose.setIcon(colorIcons.getDrawable(i));
                return true;
            }
        }
        if (item.getItemId() == R.id.menu_item_remove_note) {
            BottomDialogFragment dialogFragment =
                    BottomDialogFragment.newInstance();
            dialogFragment.setOnDialogListener(this);
            dialogFragment.show(requireFragmentManager(),
                    "dialog_fragment");

            return true;
        }

        return true;
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateViewFromDataSource();
        colorValues = getResources().obtainTypedArray(R.array.android_colors);
        colorIcons = getResources().obtainTypedArray(R.array.android_drawablew_colors);
    }

    private void updateViewFromDataSource() {
        updateTitleView();
        updateImagesView();
        updateTasksView();
        updateTextView();
        updateModifiedAtView();
        updateNoteViewBackgroundColor();
    }

    private void updateTitleView() {
        mTitleView.setText(mNote.getTitle());
    }

    private void updateImagesView() {
        mImageListContainer.removeAllViews();
        for (int i = 0; i < mNote.getImages().size(); i++) {
            ImageView image = (ImageView) getLayoutInflater().inflate(R.layout.note_image, mImageListContainer, false);
            image.setImageDrawable(new BitmapDrawable(getResources(), mNote.getImages().get(i)));
            mImageListContainer.addView(image);
        }
    }

    private void updateTasksView() {

        while (mTaskListContainer.getChildCount() != 1 ){
            mTaskListContainer.removeViewAt(0);
        }
        for (int i = 0; i < mNote.getTasks().size(); i++) {
            final Note.Task currentTask = mNote.getTasks().get(i);
            ViewGroup taskLine = (ViewGroup) getLayoutInflater().inflate(R.layout.note_task, mTaskListContainer, false);
            taskLine.setId(FrameLayout.generateViewId());

            MaterialCheckBox taskCheckbox = taskLine.findViewById(R.id.note_task_checkbox);
            taskCheckbox.setChecked(mNote.getTasks().get(i).isCompleted());
            taskCheckbox.setBackgroundColor(Color.TRANSPARENT);

            final ImageButton discardChangedButton = taskLine.findViewById(R.id.note_task_discard_changes);

            TextInputEditText taskText = taskLine.findViewById(R.id.note_task_text);
            taskText.setText(currentTask.getText());
            taskText.setOnTouchListener((v, listener) -> {
                        discardChangedButton.setVisibility(View.VISIBLE);
                        return false;
                    }
            );

            taskText.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    discardChangedButton.setVisibility(View.INVISIBLE);
                    currentTask.setText(taskText.getText().toString());
                    dataKeeper.updateNote(mNote);
                }
            });

            discardChangedButton.setOnClickListener(v -> {
                discardChangedButton.setVisibility(View.INVISIBLE);
                taskText.setText(currentTask.getText());
                taskText.clearFocus();
            });

            Utils.setFlagStrikeThroughText(taskText, taskCheckbox.isChecked());


            taskCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Utils.setFlagStrikeThroughText(taskText, isChecked);
                currentTask.setCompleted(isChecked);
                dataKeeper.updateNote(mNote);
            });

            ImageButton destroyTaskButton = taskLine.findViewById(R.id.note_task_destroy_image_button);

            destroyTaskButton.setVisibility(View.VISIBLE);
            destroyTaskButton.setOnClickListener(v -> {
                        removeTaskFromNote(currentTask);
                        mTaskListContainer.removeView(taskLine);
                        dataKeeper.updateNote(mNote);
                    }
            );
            mTaskListContainer.addView(taskLine, mTaskListContainer.getChildCount() - 1);
        }
    }

    private void updateTextView() {
        mTextView.setText(mNote.getText());
    }

    private void updateModifiedAtView() {
        mModifiedAtView.setText(Utils.getDateTimeInLocalFormat(getContext(), mNote.getModifiedAt()));
    }

    private void updateNoteViewBackgroundColor() {
        mNoteElementView.setBackgroundColor(mNote.getColor());
        updateModifiedAt();
    }

    private void removeTaskFromNote(Note.Task task) {
        task.getNote().getTasks().remove(task);
        updateModifiedAt();
    }

    private void updateTitle() {
        mNote.setTitle(mTitleView.getText().toString());
        updateTitleView();
        updateModifiedAt();
    }

    private void updateText() {
        mNote.setText(mTextView.getText().toString());
        updateTextView();
        updateModifiedAt();
    }

    private void updateModifiedAt() {
        mNote.setModifiedAt();
        updateModifiedAtView();
    }

    @Override
    public void resultDialogYes() {
        dataKeeper.deleteNote(mNote);
        if (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) {
            FragmentManager fragmentManager = requireFragmentManager();
            FragmentTransaction tr = fragmentManager.beginTransaction();
            tr.remove(this);
            tr.commit();
        } else {
            FragmentManager fragmentManager = requireFragmentManager();
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void resultDialogNo() {

    }
}
