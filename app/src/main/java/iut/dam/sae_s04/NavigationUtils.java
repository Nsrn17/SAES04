package iut.dam.sae_s04;

import android.content.Context;
import android.content.Intent;

public class NavigationUtils {
    public static void openDetailActivity(Context context, Association assoc) {
        Intent intent = new Intent(context, AssosActivity.class);
        intent.putExtra("name", assoc.getTitle());
        intent.putExtra("imageResId", assoc.getLogoResId());
        intent.putExtra("description", assoc.getDescription());
        context.startActivity(intent);
    }
}
