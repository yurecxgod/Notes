package com.example.notes;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NotesSourceFirebase implements NotesSourceInterface {
    private static final String NOTES_COLLECTION = "notes";
    private static final String TAG = "[NotesSourceFirebase]";

    private FirebaseFirestore store = FirebaseFirestore.getInstance();
    private CollectionReference collection = store.collection(NOTES_COLLECTION);
    private List<Note> notes = new ArrayList<>();

    @Override
    public NotesSourceInterface init(final NotesSourceResponse notesSourceResponse) {
        collection.orderBy(NoteMapping.Fields.DATE, Query.Direction.DESCENDING).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        notes = new ArrayList<>();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Map<String, Object> doc = document.getData();
                            String id = document.getId();
                            Note note = NoteMapping.toNote(id, doc);
                            notes.add(note);
                        }
                        Log.d(TAG, "success " + notes.size() + " qnt");
                        notesSourceResponse.initialized(NotesSourceFirebase.this);
                    } else {
                        Log.d(TAG, "get failed with " + task.getException());
                    }
                })
                .addOnFailureListener(e -> Log.d(TAG, "get failed with " + e));
        return this;
    }

    @Override
    public Note getNote(int position) {
        return notes.get(position);
    }

    @Override
    public int size() {
        if (notes == null) {
            return 0;
        }
        return notes.size();
    }

    @Override
    public void deleteNote(int position) {
        collection.document(notes.get(position).getId()).delete();
        notes.remove(position);
    }

    @Override
    public void changeNote(int position, Note note) {
        String id = note.getId();
        collection.document(id).set(NoteMapping.toDocument(note));
        notes.set(position, note);
    }

    @Override
    public void addNote(final Note note) {
        collection.add(NoteMapping.toDocument(note)).addOnSuccessListener
                (documentReference -> note.setId(documentReference.getId()));
    }
}
