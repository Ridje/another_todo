package com.example.anothertodo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.anothertodo.data.Note;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Utils {

    private static final String KEY_NOTE_POSITION = "NoteFrame.NotePosition";
    private static final String KEY_SHOW_FAVOURITE_ONLY = "NoteList.ShowFavouriteOnly";
    private static final String KEY_CURRENT_SCROLL_POSITION = "NoteList.CurrentScrollPosition";
    private static final String KEY_NOTE_ELEMENT = "NoteFrame.NoteElement";
    private static final String KEY_NOTE_ID = "NoteFrame.NoteID";

    private static AtomicInteger currentColorNumber = new AtomicInteger(0);
    private static int[] androidColors;

    public static String getKeyNoteId() {
        return KEY_NOTE_ID;
    }

    public static String getKeyNoteElement() {
        return KEY_NOTE_ELEMENT;
    }

    public static String getKeyShowFavouriteOnly() {
        return KEY_SHOW_FAVOURITE_ONLY;
    }

    public static String getKeyNotePosition() {
        return KEY_NOTE_POSITION;
    }

    public static String getKeyCurrentScrollPosition() {
        return KEY_CURRENT_SCROLL_POSITION;
    }

    public static String getDateTimeInLocalFormat(Context context, Date date) {
        DateFormat longDateFormat = android.text.format.DateFormat.getLongDateFormat(context);
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        return longDateFormat.format(date) + " " + timeFormat.format(date);
    }

    public static void setFlagStrikeThroughText(TextView textView, boolean isChecked) {
        if (isChecked) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            textView.setPaintFlags(textView.getPaintFlags()  & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
    }

    public static void initColorsForNotes(Resources resources) {
        androidColors = resources.getIntArray(R.array.android_colors);
    }

    public static int getNextNoteColor() {
        if (androidColors == null) {
            return Color.blue(255);
        }
        int result = androidColors[currentColorNumber.getAndIncrement() % androidColors.length];
        return result;
    }

    public static String encodeToBase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    public static Bitmap decodeFromBase64(String input) {
        if (input == null) {
            return null;
        }
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}
