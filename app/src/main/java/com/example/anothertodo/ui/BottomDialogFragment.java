package com.example.anothertodo.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anothertodo.R;
import com.example.anothertodo.data.OnDialogListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomDialogFragment extends BottomSheetDialogFragment {

    private OnDialogListener dialogListener;

    public BottomDialogFragment() {

    }

    // Установим слушатель диалога
    public void setOnDialogListener(OnDialogListener dialogListener){
        this.dialogListener = dialogListener;
    }

    public static BottomDialogFragment newInstance() {
        BottomDialogFragment fragment = new BottomDialogFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setCancelable(false);
        View view = inflater.inflate(R.layout.fragment_bottom_dialog, container, false);
        view.findViewById(R.id.btnYes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (dialogListener != null) dialogListener.resultDialogYes();
            }
        });

        view.findViewById(R.id.btnNo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (dialogListener != null) dialogListener.resultDialogNo();
            }
        });
        return view;
    }
}