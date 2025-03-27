package iut.dam.sae_s04;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ProgressViewHolder> {

    private int currentIndex;
    private int size;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ProgressAdapter(int size, int currentIndex, OnItemClickListener listener) {
        this.size = size;
        this.currentIndex = currentIndex;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProgressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false);
        return new ProgressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressViewHolder holder, int position) {
        if (position == currentIndex) {
            holder.segment.setBackgroundColor(Color.BLUE); // Actif
        } else {
            holder.segment.setBackgroundColor(Color.GRAY); // Inactif
        }

        // Ajout du clic sur l'élément
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
                Log.d("non","click");
            }
        });
    }

    @Override
    public int getItemCount() {
        return size; // Taille de la barre de progression
    }

    public void updateProgress(int newIndex) {
        currentIndex = newIndex;
        notifyDataSetChanged(); // Notifie l'adapter que les données ont changé
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        View segment;

        public ProgressViewHolder(View itemView) {
            super(itemView);
            segment = itemView.findViewById(R.id.progress_segment);
        }
    }
}
