package iut.dam.sae_s04;

import java.util.ArrayList;
import java.util.List;

public class AssociationData {
    private static AssociationData instance;
    private List<Association> associations;

  private AssociationData() {
        associations = new ArrayList<>();
        associations.add(new Association("Association nationale de défense des malades, invalides et handicapés", R.drawable.assoami, "Depuis 1936, avec et pour les personnes malades, invalides et/ou en situation de handicap, l’A.M.I. nationale agit au quotidien sans discrimination ni privilège, avec pour ojectifs de : promouvoir le « vivre ensemble » avec nos différences, s’engager dans des actions de représentation et de revendication,\n" +
                "défendre les personnes malades et les personnes en situation de handicap, soutenir ces personnes dans leur quotidien, contribuer à leur épanouissement par des activités ludiques, culturelles, sportives.","santé","AMI"));

      associations.add(new Association("Association B", R.drawable.image22, "Description de l'Association B","famille" , "AMI"));
      associations.add(new Association("Association C", R.drawable.image33, "Description de l'Association C","mentale" , "AMI"));
      associations.add(new Association("Association D", R.drawable.coeuric, "Description de l'Association D","santé", "AMI"));
      associations.add(new Association("Association E", R.drawable.home, "Description de l'Association E","famille" , "AMI"));
      associations.add(new Association("Association F", R.drawable.pluspleinnppouroweys, "Description de l'Association F","mentale" , "AMI"));


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

    public Association getAssociationByName(String name) {
        for (Association association : associations) {
            if (association.getTitle().equals(name)) {
                return association;
            }
        }
        return null; // Si aucune association ne correspond
    }
}
