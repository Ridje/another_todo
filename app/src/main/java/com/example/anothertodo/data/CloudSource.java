package com.example.anothertodo.data;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CloudSource implements DataSource{

    public enum Fields{
        Title("Title"), Text("Text"), CreatedAt("CreatedAt"), ModifiedAt("ModifiedAt"),
        Pinned("Pinned"), Color("Color"), ID("ID");

        private String dbFieldName;
        Fields(String dbFieldName) {
         this.dbFieldName = dbFieldName;
        }

        public String toString() {
            return dbFieldName;
        }

        public static Note toNote(String id, Map<String, Object> doc) {
            Note note = new Note();
            note.setTitle((String) doc.get(Fields.Title.toString()));
            note.setText((String) doc.get(Fields.Text.toString()));
            note.setModifiedAt(((Timestamp) doc.get(Fields.ModifiedAt.toString())).toDate());
            note.setCreatedAt(((Timestamp) doc.get(Fields.CreatedAt.toString())).toDate());
            note.setPinned((Boolean) doc.get(Fields.Pinned.toString()));
            note.setColor(((Long) doc.get(Fields.Color.toString())).intValue());
            note.setID(Integer.parseInt(id));
            return note;
        }

        public static Map<String, Object> toDocument(Note note) {
            Map<String, Object> doc = new HashMap<>();
            doc.put(Fields.Title.toString(), note.getTitle());
            doc.put(Fields.Text.toString(), note.getText());
            doc.put(Fields.ModifiedAt.toString(), note.getModifiedAt());
            doc.put(Fields.CreatedAt.toString(), note.getCreatedAt());
            doc.put(Fields.Pinned.toString(), note.getPinned());
            doc.put(Fields.Color.toString(), note.getColor());
            return doc;
        }
    }


    private static final String KEY_NOTES_COLLECTION = "Firestore.NotesCollection";
    private final FirebaseFirestore connector = FirebaseFirestore.getInstance();
    private final ArrayList<Note> notes = new ArrayList<>();
    private final CollectionReference referenceNotes = connector.collection(KEY_NOTES_COLLECTION);

    CloudSource() {

        referenceNotes.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    notes.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> doc = document.getData();
                        notes.add(Fields.toNote(document.getId(), doc));
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("[CONNECTION TO FIRESTORE]", "get failed with ", e);
            }
        });

    }

    @Override
    public Note getNoteByKey(int key) {
        return null;
    }

    @Override
    public Note getNoteByPosition(int position) {
        return null;
    }

    @Override
    public void deleteNote(int position) {

    }

    @Override
    public void deleteNote(Note note) {

    }

    @Override
    public void updateNote(Note noteData) {

    }

    @Override
    public int addNote() {
        return 0;
    }

    @Override
    public ArrayList<Note> getNotes(boolean favouriteOnly) {
        return notes;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
