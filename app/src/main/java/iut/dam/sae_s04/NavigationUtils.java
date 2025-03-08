package iut.dam.sae_s04;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class NavigationUtils {
    public static void openDetailFragment(FragmentActivity activity, Association assoc) {
        AssosFragment assosFragment = new AssosFragment();

        // Passer les données au fragment avec un Bundle
        Bundle bundle = new Bundle();
        bundle.putString("association_name", assoc.getTitle());
        bundle.putInt("association_logo", assoc.getLogoResId());
        bundle.putString("association_description", assoc.getDescription());
        assosFragment.setArguments(bundle);

        // Remplacement du fragment actuel par le fragment AssosActivity
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main, assosFragment); // Remplace avec l'ID du conteneur de fragments
        transaction.addToBackStack(null); // Permet de revenir en arrière
        transaction.commit();
    }
}
