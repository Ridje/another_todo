package com.example.anothertodo.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anothertodo.Preferences;
import com.example.anothertodo.R;
import com.example.anothertodo.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textview.MaterialTextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;


public class AuthenticationFragment extends Fragment {

    private static final String TAG = "[authentication]";
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton mGoogleSignInButton;
    private SignInButton mGoogleSignOutButton;
    private ImageView mPhoto;
    private MaterialTextView mEmail;
    private static final int RC_SIGN_IN = 40404;


    public AuthenticationFragment() {
    }


    public static AuthenticationFragment newInstance() {
        AuthenticationFragment fragment = new AuthenticationFragment();
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
        View view = inflater.inflate(R.layout.fragment_authentication, container, false);
        initGoogleSign();
        initView(view);
        return view;
    }

    private void initGoogleSign() {
        // Конфигурация запроса на регистрацию пользователя, чтобы получить
        // идентификатор пользователя, его почту и основной профайл
        // (регулируется параметром)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Получаем клиента для регистрации и данные по клиенту
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
    }

    private void initView(View view) {
        // Кнопка регистрации пользователя
        mGoogleSignInButton = view.findViewById(R.id.sign_in_google_button);
        mGoogleSignInButton.setOnClickListener(v -> signInGoogle());
        mEmail = view.findViewById(R.id.sign_in_email);
        mPhoto = view.findViewById(R.id.sign_in_photo);
        mGoogleSignOutButton = view.findViewById(R.id.sign_out_button);
        mGoogleSignOutButton.setOnClickListener(v -> signOutGoogle());
        for (int i = 0; i < mGoogleSignOutButton.getChildCount(); i++) {
            View v = mGoogleSignOutButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText("Sign out");
                return;
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account != null) {
            updateUI(account.getEmail(), account.getPhotoUrl());
            setUpUnAuthentication();
        }
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void savePreferences(boolean clearPreferences) {
        Preferences sInstance = Preferences.getInstance(requireContext());
        if (clearPreferences) {
            sInstance.removeSetting(Preferences.getKeySettingsUserAvatar());
            sInstance.removeSetting(Preferences.getKeySettingsUserEmail());
            sInstance.write(Preferences.getKeySettingsUseCloud(), false);
        } else {
            sInstance.write(Preferences.getKeySettingsUserEmail(), mEmail.getText().toString());
            sInstance.write(Preferences.getKeySettingsUserAvatar(), Utils.encodeToBase64(((BitmapDrawable) mPhoto.getDrawable()).getBitmap()));
            sInstance.write(Preferences.getKeySettingsUseCloud(), true);
        }
    }

    private void signOutGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
            setUpAuthentication();
            updateUI("example@gmail.com", getContext().getDrawable(R.drawable.no_photo));
            savePreferences(true);
            Preferences.getInstance(getContext()).removeSetting(Preferences.getKeySettingsUserEmail());
            Preferences.getInstance(getContext()).removeSetting(Preferences.getKeySettingsUserAvatar());
        });
    }

    private void setUpUnAuthentication() {
        mGoogleSignInButton.setVisibility(View.INVISIBLE);
        mGoogleSignOutButton.setVisibility(View.VISIBLE);
    }
    private void setUpAuthentication() {
        mGoogleSignInButton.setVisibility(View.VISIBLE);
        mGoogleSignOutButton.setVisibility(View.INVISIBLE);
    }

    private void updateUI(String email, Uri photoURL) {
        mEmail.setText(email);
        Thread downloadPhoto = new Thread(() ->  {
            try  {
                InputStream in = new java.net.URL(photoURL.toString()).openStream();
                final Bitmap userAvatar = BitmapFactory.decodeStream(in);
                requireActivity().runOnUiThread(() -> mPhoto.setImageBitmap(userAvatar));
                savePreferences(false);
            } catch (MalformedURLException e) {
                Log.w(TAG, "url malformed: can't download user's avatar=" + e.toString());
            } catch (IOException e) {
                Log.w(TAG, "can't fownload user's avatar: IO exception=" + e.toString());
            }
        });
        downloadPhoto.start();
    }

    private void updateUI(String email, Drawable avatar) {
        mEmail.setText(email);
        mPhoto.setImageDrawable(avatar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // Когда сюда возвращается Task, результаты аутентификации уже
            // готовы
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account.getEmail(), account.getPhotoUrl());
            setUpUnAuthentication();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure
            // reason. Please refer to the GoogleSignInStatusCodes class
            // reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

}