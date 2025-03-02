package iut.dam.sae_s04;

import java.util.ArrayList;
import java.util.List;

public class AssociationData {
    private static AssociationData instance;
    private List<Association> associations;

    private AssociationData() {
        associations = new ArrayList<>();
        associations.add(new Association("Association A", R.drawable.image1, "Description de l'Association A","santé"));
        associations.add(new Association("Association B", R.drawable.image22, "Description de l'Association B","famille"));
        associations.add(new Association("Association C", R.drawable.image33, "Description de l'Association C","mentale"));
        associations.add(new Association("Association D", R.drawable.coeuric, "Description de l'Association D","santé"));
        associations.add(new Association("Association E", R.drawable.home, "Description de l'Association E","famille"));
        associations.add(new Association("Association F", R.drawable.pluspleinnppouroweys, "Description de l'Association F","mentale"));


    }

    public static AssociationData getInstance() {
        if (instance == null) {
            instance = new AssociationData();
        }
        return instance;
    }

    public List<Association> getAssociations() {
        return associations;
    }
}
