package iut.dam.sae_s04;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {

    private List<Association> associations;

    public CarouselAdapter() {
        this.associations = AssociationData.getInstance().getAssociations();
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carousel, parent, false);
        return new CarouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {
        Association association = associations.get(position);
        holder.imageView.setImageResource(association.getLogoResId());
        holder.textView.setText(association.getTitle());
    }

    @Override
    public int getItemCount() {
        return associations.size();
    }

    public static class CarouselViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public CarouselViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
