package com.example.notes;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class NotesSource implements NotesSourceInterface, Parcelable {
    public static final Creator<NotesSource> CREATOR = new Creator<NotesSource>() {
        @Override
        public NotesSource createFromParcel(Parcel in) {
            return new NotesSource(in);
        }

        @Override
        public NotesSource[] newArray(int size) {
            return new NotesSource[size];
        }
    };
    private ArrayList<Note> notes;
    private Resources resources;

    public NotesSource(Resources resources) {
        this.resources = resources;
        notes = new ArrayList<>();
    }

    protected NotesSource(Parcel in) {
        notes = in.createTypedArrayList(Note.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(notes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public NotesSourceInterface init(NotesSourceResponse notesSourceResponse) {
        Note[] notesArray = new Note[]{
                new Note(resources.getString(R.string.first_note_title),
                        resources.getString(R.string.first_note_content),
                        getDateOfCreation()),
                new Note(resources.getString(R.string.second_note_title),
                        resources.getString(R.string.second_note_content),
                        getDateOfCreation()),
                new Note(resources.getString(R.string.third_note_title),
                        resources.getString(R.string.third_note_content),
                        getDateOfCreation()),
                new Note(resources.getString(R.string.fourth_note_title),
                        resources.getString(R.string.fourth_note_content),
                        getDateOfCreation()),
                new Note(resources.getString(R.string.fifth_note_title),
                        resources.getString(R.string.fifth_note_content),
                        getDateOfCreation()),
                new Note(resources.getString(R.string.sixth_note_title),
                        resources.getString(R.string.sixth_note_content),
                        getDateOfCreation()),
                new Note(resources.getString(R.string.seventh_note_title),
                        resources.getString(R.string.seventh_note_content),
                        getDateOfCreation()),
                new Note(resources.getString(R.string.eighth_note_title),
                        resources.getString(R.string.eighth_note_content),
                        getDateOfCreation()),
                new Note(resources.getString(R.string.ninth_note_title),
                        resources.getString(R.string.ninth_note_content),
                        getDateOfCreation()),
                new Note(resources.getString(R.string.tenth_note_title),
                        resources.getString(R.string.tenth_note_content),
                        getDateOfCreation()),
                new Note(resources.getString(R.string.eleventh_note_title),
                        resources.getString(R.string.eleventh_note_content),
                        getDateOfCreation()),
                new Note(resources.getString(R.string.twelfth_note_title),
                        resources.getString(R.string.twelfth_note_content),
                        getDateOfCreation())
        };
        Collections.addAll(notes, notesArray);
        if (notesSourceResponse != null) {
            notesSourceResponse.initialized(this);
        }
        return this;
    }

    @Override
    public Note getNote(int position) {
        return notes.get(position);
    }

    @Override
    public int size() {
        return notes.size();
    }

    @Override
    public void deleteNote(int position) {
        notes.remove(position);
    }

    @Override
    public void changeNote(int position, Note note) {
        notes.set(position, note);
    }

    @Override
    public void addNote(Note note) {
        notes.add(note);
    }

    public String getDateOfCreation() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy",
                Locale.getDefault());
        return String.format("%s: %s", "Created data",
                formatter.format(Calendar.getInstance().getTime()));
    }
}
