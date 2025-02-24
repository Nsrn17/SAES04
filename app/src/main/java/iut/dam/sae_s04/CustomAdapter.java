package iut.dam.sae_s04;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private List<String> filteredList;

    public CustomAdapter(Context context, List<String> filteredList) {
        this.context = context;
        this.filteredList = filteredList;
    }

    @Override
    public int getCount() {
        return filteredList.size();  // Renvoie la taille de filteredList
    }

    @Override
    public String getItem(int position) {
        return filteredList.get(position);  // Renvoie l'élément à l'index 'position' de filteredList
    }

    @Override
    public long getItemId(int position) {
        return position;  // Retourne l'ID de l'élément, ici la position
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_result, parent, false);
        }

        String item = getItem(position);  // Utilise getItem pour obtenir l'élément à cette position

        TextView textView = convertView.findViewById(R.id.nomA);
        textView.setText(item);  // Affiche l'élément dans le TextView de chaque ligne

        return convertView;
    }
}
