package com.example.anothertodo.data;

import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CloudSource implements DataSource{

    private static final ArrayList<DataChangedListener> changesListeners = new ArrayList<>();
    private static final String KEY_NOTES_COLLECTION = "Firestore.NotesCollection";
    private static final String KEY_TASKS_BLUESCREEN_COLLECTION = "Firestore.NotesCollection.%s/%s/tasks";
    private final FirebaseFirestore connector = FirebaseFirestore.getInstance();
    private final ArrayList<Note> notes = new ArrayList<>();
    private CollectionReference referenceNotes;
    private String mUserName;

    @Override
    public void addListener(DataChangedListener listener) {
        changesListeners.add(listener);
    }

    @Override
    public void removeListener(DataChangedListener listener) {
        changesListeners.remove(listener);
    }

    public enum Fields{
        Title("Title"), Text("Text"), CreatedAt("CreatedAt"), ModifiedAt("ModifiedAt"),
        Pinned("Pinned"), Color("Color"), ID("ID"),

        TaskCompleted("Completed"), TaskText("Text");


        private String dbFieldName;
        Fields(String dbFieldName) {
         this.dbFieldName = dbFieldName;
        }

        public String toString() {
            return dbFieldName;
        }

        public static Note toNote(String id, Map<String, Object> doc) {
            Note note = new Note();
            if (doc.get(Fields.Title.toString()) != null) {
                note.setTitle((String) doc.get(Fields.Title.toString()));
            }
            if (doc.get(Fields.Text.toString()) != null) {
                note.setText((String) doc.get(Fields.Text.toString()));
            }
            if (doc.get(Fields.ModifiedAt.toString()) != null) {
                note.setModifiedAt(((Timestamp) doc.get(Fields.ModifiedAt.toString())).toDate());
            }
            if (doc.get(Fields.CreatedAt.toString()) != null) {
                note.setCreatedAt(((Timestamp) doc.get(Fields.CreatedAt.toString())).toDate());
            }
            if (doc.get(Fields.Pinned.toString()) != null) {
                note.setPinned((Boolean) doc.get(Fields.Pinned.toString()));
            }
            if (doc.get(Fields.Color.toString()) != null) {
                note.setColor(((Long) doc.get(Fields.Color.toString())).intValue());
            }
            note.setID(id);
            return note;
        }

        public static Note.Task toTask(Note note, Map<String, Object> doc) {
            Note.Task task = note.new Task((String) doc.get(Fields.TaskText), (Boolean) doc.get(Fields.TaskCompleted));
            return task;
        }

        public static Map<String, Object> toDocumentNote(Note note) {
            Map<String, Object> doc = new HashMap<>();
            doc.put(Fields.Title.toString(), note.getTitle());
            doc.put(Fields.Text.toString(), note.getText());
            doc.put(Fields.ModifiedAt.toString(), note.getModifiedAt());
            doc.put(Fields.CreatedAt.toString(), note.getCreatedAt());
            doc.put(Fields.Pinned.toString(), note.getPinned());
            doc.put(Fields.Color.toString(), note.getColor());
            return doc;
        }

        public static Map<String, Object> toDocumentNoteTask(Note.Task task) {
            Map<String, Object> doc = new HashMap<>();
            doc.put(Fields.TaskText.toString(), task.getText());
            doc.put(Fields.TaskCompleted.toString(), task.isCompleted());
            return doc;
        }
    }


    CloudSource(String username) throws UserNotAuthenticatedException {
        if (username == null) {
            throw new UserNotAuthenticatedException();
        }
        mUserName = username;
        referenceNotes = connector.collection(KEY_NOTES_COLLECTION + "." + mUserName);
        referenceNotes.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                notes.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> doc = document.getData();
                    Note newNote = Fields.toNote(document.getId(), doc);

                    CollectionReference referenceNoteTasks = connector.collection(String.format(KEY_TASKS_BLUESCREEN_COLLECTION, mUserName, document.getId()));
                    referenceNoteTasks.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            newNote.mTasks.clear();
                            for (QueryDocumentSnapshot taskDoc : task1.getResult()) {
                                Map<String, Object> noteTask = taskDoc.getData();
                                    newNote.mTasks.add(newNote.new Task((String) noteTask.get("Text"), (Boolean) noteTask.get("Completed")));
                            }
                        }

                    });
                    notes.add(newNote);
                }

                for (int i = 0; i < changesListeners.size(); i++) {
                    changesListeners.get(i).onDataSetChanged();
                }
            }
        }).addOnFailureListener(e -> Log.d("[CONNECTION_TO_FIRESTORE]", "get failed with ", e));

    }

    @Override
    public Note getNoteByKey(String key) {
        return notes.stream().filter(note -> note.getID().equals(key)).findFirst().get();
    }

    @Override
    public Note getNoteByPosition(int position) {
        return notes.get(position);
    }

    @Override
    public void deleteNote(int position) {
        referenceNotes.document(notes.get(position).getID()).delete().addOnSuccessListener(unused -> {
            notes.remove(position);
            CollectionReference referenceNoteTasks = connector.collection(String.format(KEY_TASKS_BLUESCREEN_COLLECTION, mUserName, notes.get(position).getID()));
            referenceNoteTasks.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        referenceNoteTasks.document(document.getId()).delete();
                    }
                    for (int i = 0; i < changesListeners.size(); i++) {
                        changesListeners.get(i).onItemRemoved(position);
                    }
                }
            });
        });

    }

    @Override
    public void deleteNote(Note note) {
        int indexOfRemoved = notes.indexOf(note);
        referenceNotes.document(note.getID()).delete().addOnSuccessListener(unused -> {
            notes.remove(note);
            CollectionReference referenceNoteTasks = connector.collection(String.format(KEY_TASKS_BLUESCREEN_COLLECTION, mUserName, note.getID()));
            referenceNoteTasks.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        referenceNoteTasks.document(document.getId()).delete();
                    }
                    for (int i = 0; i < changesListeners.size(); i++) {
                        changesListeners.get(i).onItemRemoved(indexOfRemoved);
                    }
                }
            });

        });

    }

    @Override
    public void updateNote(Note noteData) {
        int indexOfUpdated = notes.indexOf(noteData);
        referenceNotes.document(noteData.getID()).update(Fields.toDocumentNote(noteData)).addOnSuccessListener(unused -> {
            CollectionReference referenceNoteTasks = connector.collection(String.format(KEY_TASKS_BLUESCREEN_COLLECTION, mUserName, noteData.getID()));
            referenceNoteTasks.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        referenceNoteTasks.document(document.getId()).delete();
                    }
                    for (int i = 0; i < noteData.mTasks.size(); i++) {
                        referenceNoteTasks.add(Fields.toDocumentNoteTask(noteData.mTasks.get(i)));
                    }
                    for (int i = 0; i < changesListeners.size(); i++) {
                        changesListeners.get(i).onItemUpdated(indexOfUpdated);
                    }
                }
            });
            for (int i = 0; i < changesListeners.size(); i++) {
                changesListeners.get(i).onItemUpdated(indexOfUpdated);
            }
        });


    }

    @Override
    public void addNote() {
        Note noteToAdd = new Note();
        referenceNotes.add(Fields.toDocumentNote(noteToAdd)).addOnSuccessListener(documentReference -> {
            noteToAdd.setID(documentReference.getId());
            notes.add(noteToAdd);
            for (int i = 0; i < changesListeners.size(); i++) {
                changesListeners.get(i).onItemAdded(notes.size() - 1);
            }
        });
    }

    @Override
    public ArrayList<Note> getNotes(boolean favouriteOnly) {
        return notes;
    }

    @Override
    public boolean isEmpty() {
        return notes.isEmpty();
    }
}
