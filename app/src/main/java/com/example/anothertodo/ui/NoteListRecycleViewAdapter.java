package com.example.anothertodo.ui;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anothertodo.R;
import com.example.anothertodo.Utils;
import com.example.anothertodo.data.Note;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class NoteListRecycleViewAdapter extends RecyclerView.Adapter {

    ArrayList<Note> mNotes;
    private OnItemClickListener itemClickListener;

    public NoteListRecycleViewAdapter(ArrayList<Note> notes) {
        this.mNotes = notes;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).bindViewHolder(mNotes.get(position));
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView mCardView;
        MaterialTextView mTitle;
        MaterialTextView mHashCode;
        LinearLayoutCompat mImagesContainer;
        LinearLayoutCompat mTasksContainer;
        MaterialTextView mText;
        MaterialTextView mModifiedAt;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.note_card_view);
            mTitle = itemView.findViewById(R.id.note_title);
            mHashCode = itemView.findViewById(R.id.note_hashcode);
            mImagesContainer = itemView.findViewById((R.id.note_images_list));
            mTasksContainer = itemView.findViewById((R.id.note_tasks_list));
            mText = itemView.findViewById((R.id.note_text));
            mModifiedAt = itemView.findViewById((R.id.note_modified_at));
            itemView.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(v, getAdapterPosition());
                }
            });

        }

        void bindViewHolder(Note note) {

            mTitle.setText(note.getTitle());

            mHashCode.setText("#" + Integer.toString(note.hashCode()));

            mCardView.setBackgroundColor(note.getColor());

            ArrayList<Bitmap> noteImages = note.getImages();
            if (!noteImages.isEmpty()) {
                for (int i = 0; i < noteImages.size(); i++) {

                    // TODO: 22-Mar-21 Ask about inflate something here or is it a bad idea to inflate on bind dinamycly
                    ImageView image = (ImageView) LayoutInflater.from(mImagesContainer.getContext()).inflate(R.layout.note_image, mImagesContainer, false);
                    image.setImageDrawable(new BitmapDrawable(mImagesContainer.getContext().getResources(), noteImages.get(i)));
                    image.setId(View.generateViewId());
                    mImagesContainer.addView(image);
                }
            }

            ArrayList<Note.Task> noteTasks = note.getTasks();
            if (!noteTasks.isEmpty()) {
                for (int i = 0; i < noteTasks.size(); i++) {

                    MaterialCheckBox taskCheckbox = (MaterialCheckBox) LayoutInflater.from(mImagesContainer.getContext()).inflate(R.layout.note_task, mTasksContainer, false);
                    taskCheckbox.setChecked(noteTasks.get(i).isCompleted());
                    taskCheckbox.setId(View.generateViewId());
                    Utils.setFlagStrikeThroughText(taskCheckbox, taskCheckbox.isChecked());
                    taskCheckbox.setText(noteTasks.get(i).getText());
                    taskCheckbox.setClickable(false);
                    taskCheckbox.setBackgroundColor(Color.TRANSPARENT);

                    mTasksContainer.addView(taskCheckbox);
                }
            }

            mText.setText(note.getText());
            mModifiedAt.setText(Utils.getDateTimeInLocalFormat(mModifiedAt.getContext(), note.getModifiedAt()));

        }
    }
}
