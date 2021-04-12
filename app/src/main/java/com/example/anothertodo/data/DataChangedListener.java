package com.example.anothertodo.data;

public interface DataChangedListener {
    void onItemAdded(int position);
    void onItemRemoved(int position);
    void onItemUpdated(int position);
    void onDataSetChanged();
}
