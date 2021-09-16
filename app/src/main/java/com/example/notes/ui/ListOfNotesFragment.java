package com.example.notes.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.Note;
import com.example.notes.NotesSource;
import com.example.notes.R;
import com.example.notes.observe.Publisher;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static com.example.notes.ui.NoteFragment.CURRENT_DATA;
import static com.example.notes.ui.NoteFragment.CURRENT_NOTE;

public class ListOfNotesFragment extends Fragment {
    private com.example.notes.Note currentNote;
    private NotesSource data;
    private NotesAdapter adapter;
    private RecyclerView recyclerView;
    private com.example.notes.Navigation navigation;
    private Publisher publisher;
    private boolean moveToLastPosition;

    public static ListOfNotesFragment newInstance() {
        return new ListOfNotesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (data == null) {
            data = new NotesSource(getResources()).init();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_of_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.notes_recycler_view);
        initRecyclerView(recyclerView, data);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        com.example.notes.MainActivity activity = (com.example.notes.MainActivity) context;
        navigation = activity.getNavigation();
        publisher = activity.getPublisher();
    }

    @Override
    public void onDetach() {
        navigation = null;
        publisher = null;
        super.onDetach();
    }

    private void initRecyclerView(RecyclerView recyclerView, NotesSource data) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        if (moveToLastPosition) {
            recyclerView.smoothScrollToPosition(data.size() - 1);
            moveToLastPosition = false;
        }

        adapter = new NotesAdapter(data, this);
        adapter.setOnItemClickListener((position, note) -> {
            navigation.addFragment(NoteFragment.newInstance(data.getNote(position)),
                    true);
            publisher.subscribe(note1 -> {
                data.changeNote(position, note1);
                adapter.notifyItemChanged(position);
            });
        });

        recyclerView.setAdapter(adapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration
                (requireContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(Objects.requireNonNull
                (ContextCompat.getDrawable(getContext(), R.drawable.separator)));
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(CURRENT_NOTE, currentNote);
        outState.putParcelable(CURRENT_DATA, data);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            data = savedInstanceState.getParcelable(CURRENT_DATA);
            currentNote = savedInstanceState.getParcelable(CURRENT_NOTE);
        } else {
            currentNote = data.getNote(0);
        }
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v,
                                    @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getMenuPosition();
        if (item.getItemId() == R.id.menu_delete_note) {
            data.deleteNote(position);
            adapter.notifyItemRemoved(position);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuItem addNote = menu.findItem(R.id.menu_add_note);
        addNote.setOnMenuItemClickListener(item -> {
            navigation.addFragment(NoteFragment.newInstance(), true);
            publisher.subscribe(note -> {
                data.addNote(note);
                adapter.notifyItemInserted(data.size() - 1);
                moveToLastPosition = true;
            });
            return true;
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}
