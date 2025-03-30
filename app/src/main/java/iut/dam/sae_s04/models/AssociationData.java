package iut.dam.sae_s04.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import iut.dam.sae_s04.R;

public class AssociationData {
    private static AssociationData instance;
    private List<Association> associations;

  private AssociationData() {
        associations = new ArrayList<>();
        associations.add(new Association("Association nationale de défense des malades, invalides et handicapés", R.drawable.assoami, "Depuis 1936, avec et pour les personnes malades, invalides et/ou en situation de handicap, l’A.M.I. nationale agit au quotidien sans discrimination ni privilège, avec pour ojectifs de : promouvoir le « vivre ensemble » avec nos différences, s’engager dans des actions de représentation et de revendication,\n" + "\n" +
                "défendre les personnes malades et les personnes en situation de handicap, soutenir ces personnes dans leur quotidien, contribuer à leur épanouissement par des activités ludiques, culturelles, sportives.","santé","AMI"));
        associations.add(new Association("Association Alliance Maladies Rares", R.drawable.assoamr, "Association loi 1901, l’Alliance Maladies Rares, créée le 24 février 2000, rassemble plus de 200 associations de malades et accueille en son sein des malades et familles isolés, « orphelins » d’associations.\n" + "\n" + "Elle représente environ 2 000 pathologies rares et 2 millions de malades.","santé" , "AMR"));
        associations.add(new Association("Association Schizo-Oui", R.drawable.assoschizo, "Schizo-oui est une association d’usagers en santé mentale née en janvier 1998. A cette époque, seulement un schizophrène sur cinq avait connaissance de son diagnostic.\n" + "\n" + "Or, cette maladie touche 1% de la population toutes catégories sociales confondues. Il est nécessaire de s’unir pour la combattre en ayant des objectifs précis. C’est la volonté de Schizo-oui, association qui s’organise. Schizo-oui est une association apolitique et indépendante de tout groupe de pression.","mentale" , "SCHIZO"));
        associations.add(new Association("Association Ligue Contre le Cancer", R.drawable.assolcc, "Premier financeur associatif de la recherche contre le cancer, la Ligue contre le cancer est une organisation non-gouvernementale indépendante reposant sur la générosité du public et sur l’engagement de ses militants.\n" + "\n" + "Forte de 600 000 adhérents et de 13 800 bénévoles, la Ligue est un mouvement populaire organisé en une fédération de 103 comités départementaux. Ensemble, ils luttent dans quatre directions complémentaires : chercher pour guérir, prévenir pour protéger, accompagner pour aider, mobiliser pour agir.","santé", "LCC"));
        associations.add(new Association("Association Le Planning Familial", R.drawable.assolpf, "Le Planning Familial est un mouvement militant qui prend en compte toutes les sexualités, défend le droit à la contraception, à l’avortement et à l’éducation à la sexualité.\n" + "\n" + "Il dénonce et combat toutes les formes de violences, lutte contre le SIDA et les IST, contre toutes les formes de discrimination et contre les inégalités sociales.","famille" , "LPF"));
        associations.add(new Association("Association Petits Frères des Pauvres", R.drawable.assopfp, "Depuis 1946, les Petits Frères des Pauvres accompagnent les personnes âgées souffrant d’isolement, prioritairement les plus démunies.\n" +
                "\n" +
                "Par nos actions, nous recréons des liens leur permettant de retrouver une dynamique de vie.\n" +
                "\n" +
                "Par notre voix, nous incitons la société à changer de regard sur la vieillesse, nous témoignons des situations inacceptables que nous rencontrons, nous alertons les pouvoirs publics sur la nécessité d’agir, nous favorisons l’engagement citoyen, nous proposons des réponses nouvelles.","famille" , "PFP"));
        associations.add(new Association("Association pour le Droit de Mourir dans la Dignité ", R.drawable.assodmd, "Depuis 1980, l’Association pour le Droit de Mourir dans la Dignité milite pour que chaque Française et chaque Français puisse choisir les conditions de sa propre fin de vie.\n" + "\n" + "Conformément à ses conceptions personnelles de dignité et de liberté.","santé" , "DMD"));

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
                Log.d("AssociationData", "Association trouvée : " + name);
                return association;
            }
        }
        Log.e("AssociationData", "Association non trouvée : " + name);
        return null;
    }

}
