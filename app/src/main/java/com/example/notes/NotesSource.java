package com.example.notes;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

public class NotesSource implements Parcelable {
    public static final Creator<NotesSource> CREATOR = new Creator<NotesSource>() {
        @Override
        public com.example.notes.NotesSource createFromParcel(Parcel in) {
            return new com.example.notes.NotesSource(in);
        }

        @Override
        public com.example.notes.NotesSource[] newArray(int size) {
            return new com.example.notes.NotesSource[size];
        }
    };
    private ArrayList<Note> notes;
    private Resources resources;
    private int counter = 0;

    public NotesSource(Resources resources) {
        this.resources = resources;
        notes = new ArrayList<>();
    }

    protected NotesSource(Parcel in) {
        notes = in.createTypedArrayList(com.example.notes.Note.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(notes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public com.example.notes.NotesSource init() {
        com.example.notes.Note[] notesArray = new com.example.notes.Note[]{
                new com.example.notes.Note(resources.getString(R.string.first_note_title),
                        resources.getString(R.string.first_note_content),
                        getDateOfCreation(), getColor()),
                new com.example.notes.Note(resources.getString(R.string.second_note_title),
                        resources.getString(R.string.second_note_content),
                        getDateOfCreation(), getColor()),
                new com.example.notes.Note(resources.getString(R.string.third_note_title),
                        resources.getString(R.string.third_note_content),
                        getDateOfCreation(), getColor()),
                new com.example.notes.Note(resources.getString(R.string.fourth_note_title),
                        resources.getString(R.string.fourth_note_content),
                        getDateOfCreation(), getColor()),
                new com.example.notes.Note(resources.getString(R.string.fifth_note_title),
                        resources.getString(R.string.fifth_note_content),
                        getDateOfCreation(), getColor()),
                new com.example.notes.Note(resources.getString(R.string.sixth_note_title),
                        resources.getString(R.string.sixth_note_content),
                        getDateOfCreation(), getColor()),
                new com.example.notes.Note(resources.getString(R.string.seventh_note_title),
                        resources.getString(R.string.seventh_note_content),
                        getDateOfCreation(), getColor()),
                new com.example.notes.Note(resources.getString(R.string.eighth_note_title),
                        resources.getString(R.string.eighth_note_content),
                        getDateOfCreation(), getColor()),
                new com.example.notes.Note(resources.getString(R.string.ninth_note_title),
                        resources.getString(R.string.ninth_note_content),
                        getDateOfCreation(), getColor()),
                new com.example.notes.Note(resources.getString(R.string.tenth_note_title),
                        resources.getString(R.string.tenth_note_content),
                        getDateOfCreation(), getColor()),
                new com.example.notes.Note(resources.getString(R.string.eleventh_note_title),
                        resources.getString(R.string.eleventh_note_content),
                        getDateOfCreation(), getColor()),
                new com.example.notes.Note(resources.getString(R.string.twelfth_note_title),
                        resources.getString(R.string.twelfth_note_content),
                        getDateOfCreation(), getColor())
        };
        Collections.addAll(notes, notesArray);
        return this;
    }

    public com.example.notes.Note getNote(int position) {
        return notes.get(position);
    }

    public int size() {
        return notes.size();
    }

    public void deleteNote(int position) {
        notes.remove(position);
    }

    public void changeNote(int position, com.example.notes.Note note) {
        notes.set(position, note);
    }

    public void addNote(com.example.notes.Note note) {
        notes.add(note);
    }

    public String getDateOfCreation() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy",
                Locale.getDefault());
        return String.format("%s: %s", "Дата создания",
                formatter.format(Calendar.getInstance().getTime()));
    }

    public int getColor() {
        int[] colors = resources.getIntArray(R.array.colors);
        int color = colors[counter];
        if (counter < colors.length - 1) {
            counter++;
        } else counter = 0;
        return color;
    }
}
