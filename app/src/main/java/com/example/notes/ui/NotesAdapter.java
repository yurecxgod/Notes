package com.example.notes.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.Note;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.example.notes.NotesSource;
import com.example.notes.NotesSourceInterface;
import com.example.notes.R;

public class NotesAdapter extends RecyclerView.Adapter<com.example.notes.ui.NotesAdapter.ViewHolder> {
    private final Fragment fragment;
    private NotesSourceInterface dataSource;
    private MyClickListener myClickListener;
    private int menuPosition;

    public NotesAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setDataSource(NotesSourceInterface dataSource) {
        this.dataSource = dataSource;
        notifyDataSetChanged();
    }

    public int getMenuPosition() {
        return menuPosition;
    }

    public void setOnItemClickListener(MyClickListener itemClickListener) {
        myClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        holder.getTitleTextView().setText(dataSource.getNote(position).getTitle());
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy",
                Locale.getDefault());
        holder.getDateTextView().setText(dataSource.getNote(position).getCreationDate());
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, Note note);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout itemLayout;
        private TextView titleTextView;
        private TextView dateTextView;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.element_of_recycler_view);
            titleTextView = itemView.findViewById(R.id.first_tv_of_item);
            dateTextView = itemView.findViewById(R.id.second_tv_of_item);

            registerContextMenu(itemView);

            itemLayout.setOnClickListener(v -> {
                int position = getAdapterPosition();
                myClickListener.onItemClick(position, dataSource.getNote(position));
            });

            itemLayout.setOnLongClickListener(v -> {
                menuPosition = getLayoutPosition();
                itemView.showContextMenu();
                return true;
            });
        }

        private void registerContextMenu(@NonNull View itemView) {
            if (fragment != null) {
                itemView.setOnLongClickListener(v -> {
                    menuPosition = getLayoutPosition();
                    return false;
                });
                fragment.registerForContextMenu(itemView);
            }
        }

        public LinearLayout getItemLayout() {
            return itemLayout;
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public TextView getDateTextView() {
            return dateTextView;
        }
    }
}
