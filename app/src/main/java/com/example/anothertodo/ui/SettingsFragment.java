package com.example.anothertodo.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.example.anothertodo.Preferences;
import com.example.anothertodo.R;
import com.example.anothertodo.data.DataKeeper;
import com.google.android.material.switchmaterial.SwitchMaterial;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private Preferences settings;
    private SwitchMaterial mSwitchUseCloud;
    private boolean mUseCloud;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = Preferences.getInstance(requireContext());
        mUseCloud = settings.read(Preferences.getKeySettingsUseCloud(), false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwitchUseCloud = view.findViewById(R.id.settings_keep_notes_in_cloud);
        mSwitchUseCloud.setChecked(mUseCloud);
        mSwitchUseCloud.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mUseCloud = isChecked;
            settings.write(Preferences.getKeySettingsUseCloud(), mUseCloud);
            DataKeeper.getInstance(getContext()).updateDataSource(getContext());
        }
        );
    }
}