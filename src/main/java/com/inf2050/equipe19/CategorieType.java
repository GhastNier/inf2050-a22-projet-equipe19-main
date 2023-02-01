package com.inf2050.equipe19;

import com.inf2050.equipe19.Permis.Activites;

public class CategorieType extends VerificationActivites {

    public static void interrupteurTypeActivite(Activites act, Permis permis) {
        if (estUneCategorieValide(act, permis)) {
            switch (act.categorie.toLowerCase()) {
                case "atelier", "séminaire", "colloque", "lecture dirigée" -> typeActiviteQuatreCategorie(act);
                case "présentation" -> typeActivitePresentation(act);
                case "groupe de discussion" -> typeActiviteGroupeDeDiscussion(act);
                case "projet de recherche" -> typeActiviteProjetDeRecherche(act);
                case "rédaction professionnelle" -> typeActiviteRedactionPro(act);
                case "cours" -> typeActiviteCours(act);
                case "conférence" -> typeActiviteConference(act);
            }
        }
    }

    private static void typeActiviteQuatreCategorie(Activites act) {
        heuresQuatreCategories += act.heures;
        switch (act.categorie.toLowerCase()){
            case "atelier" -> heuresAtelierPourStats += act.heures;
            case "séminaire" -> heuresSeminairePourStats += act.heures;
            case "colloque" -> heuresColloquePourStats += act.heures;
            case "lecture dirigée" -> heuresLectureDirigeePourStats += act.heures;
        }
    }

    private static void typeActivitePresentation(Activites act) {
        heuresPresentation += act.heures;
    }

    private static void typeActiviteGroupeDeDiscussion(Activites act) {
        heuresGroupeDisc += act.heures;
    }

    private static void typeActiviteProjetDeRecherche(Activites act) {
        heuresProjetRech += act.heures;
    }

    private static void typeActiviteRedactionPro(Activites act) {
        heuresRedac += act.heures;
    }

    private static void typeActiviteCours(Activites act) {
        heuresCours += act.heures;
    }

    private static void typeActiviteConference(Activites act) {
        heuresConference += act.heures;
    }

    private static boolean estUneCategorieValide(Activites act, Permis permis) {
        return verifierSiHrsEstInt(act) >= 1 && verifierSiDateValide(act, permis);
    }
}

