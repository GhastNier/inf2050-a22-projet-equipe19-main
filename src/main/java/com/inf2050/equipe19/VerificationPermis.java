package com.inf2050.equipe19;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.inf2050.equipe19.LecteurJson.nbHeuresCompletees;
import static com.inf2050.equipe19.Main.*;
import static com.inf2050.equipe19.Permis.tabErreurs;
import static com.inf2050.equipe19.Permis.valide;

public class VerificationPermis extends VerificationActivites {

    public static ArrayList<String> cycleArchitectes = new ArrayList<>(Arrays.asList("2016-2018", "2018-2020", "2021-2023"));
    public static ArrayList<String> cycleGeologues = new ArrayList<>(List.of("2019-2022"));
    public static ArrayList<String> cyclePsychologues = new ArrayList<>(List.of("2018-2023"));

    public static boolean siCycleActuel(Permis permis) {
        if (!cycleSelonOrdre(permis)) {
            nbHeuresCompletees = false;
            valide = false;
            tabErreurs.add(permis(permis, "CyclePermis"));
            return false;
        }
        return true;
    }

    public static boolean cycleSelonOrdre(Permis permis) {
        if (permis.ordre.equalsIgnoreCase("architectes") && !cycleArchitectes.contains(permis.cycle)) {
            return false;
        } else if (permis.ordre.equalsIgnoreCase("géologues") && !cycleGeologues.contains(permis.cycle)) {
            return false;
        } else return !permis.ordre.equalsIgnoreCase("psychologues") || cyclePsychologues.contains(permis.cycle);
    }

    public static boolean numeroPermis(Permis permis) {
        Pattern p = null;
        if(permis.ordre.equalsIgnoreCase("architectes")) {
            p = Pattern.compile("^[AT][0-9]{4}$");
        } else if(permis.ordre.equalsIgnoreCase("psychologues")) {
            p = Pattern.compile("^[0-9]{5}-[0-9]{2}$");
        } else if(permis.ordre.equalsIgnoreCase("géologues")) {
            verifierPermisGeologues(permis);
            p = Pattern.compile("^[A-Z]{2}[0-9]{4}$");
        } else if(permis.ordre.equalsIgnoreCase("podiatres")) {
            p = Pattern.compile("[0-9]{5}");
        }
        if (p == null) return false;
        return verifierNumPermis(permis, p);
    }

    public static void verifierPermisGeologues(Permis permis) {
        if(Character.isUpperCase(permis.numero_de_permis.charAt(0)) && Character.isUpperCase(permis.numero_de_permis.charAt(1)) &&
                (Character.toUpperCase(permis.nom.charAt(0)) == permis.numero_de_permis.charAt(0))  &&
                (Character.toUpperCase(permis.prenom.charAt(0)) == permis.numero_de_permis.charAt(1))) {
        } else {
            valide = false;
            tabErreurs.add(permis(permis,"InvalidePremierLettrePermis"));
        }
    }

    public static boolean verifierNumPermis(Permis permis, Pattern permisPattern) {
        boolean valide = true;
        boolean correcte;
        if(permis.numero_de_permis == null) return false;
        Matcher m = permisPattern.matcher(permis.numero_de_permis);
        correcte = m.matches();
        if(!correcte) {
            tabErreurs.add(permis(permis,"NumeroInvalidePermis"));
            valide = false;
        }
        return valide;
    }

    public static boolean validerNomPrenom(String nomPrenom) {
        boolean pasErreur = nomPrenom != null;
        if(pasErreur) {
            int longueur = nomPrenom.length();
            for (int i = 0; i < longueur && pasErreur; i++) {
                if (!Character.isLetter(nomPrenom.charAt(i))) {
                    pasErreur = false;
                }
            }
        }
        return pasErreur;
    }

    public static void verifierNomPrenom(Permis permis) {
        if (!validerNomPrenom(permis.nom)) {
            valide = false;
            tabErreurs.add(permis(permis, "NomPermis"));
        }
        if (!validerNomPrenom(permis.prenom)) {
            valide = false;
            tabErreurs.add(permis(permis, "PrenomPermis"));
        }
    }

    public static void verifierSexe(Permis permis) {
        if (!(permis.sexe == 0 || permis.sexe == 1 || permis.sexe == 2 )) {
            valide = false;
            tabErreurs.add(permis(permis,"SexePermis"));
        }
    }

    static boolean contientPasErreurInt() {
        int index = 0;
        boolean contient = true;
        while ((index < tabErreurs.size()) && contient) {
            if (tabErreurs.get(index).contains("Le format d'heure n'est pas adéquat pour l'activité")) {
                contient = false;
                valide = false;
            }
            index++;
        }
        return contient;
    }

    static void erreurMenantTerminaisonDeProg(Permis permis) throws Exception {
        if (!numeroPermis(permis)|| !verifierDescription(permis) ||
                !contientPasErreurInt()) {
            if(!numeroPermis(permis)){
                statistique.ajusterDeclarationPermisInvalide();
            }
            tabErreurs.add(permis(permis, "EntrerInvalide"));
            statistique.ajusterDeclarationsTraitees();
            statistique.ajusterDeclarationsIncompletesOuInvalides();
            producteurErreurs(false);
        }
    }

    static void permisEstValide(Permis permis) throws Exception {
        erreurMenantTerminaisonDeProg(permis);
        verifierNomPrenom(permis);
        verifierSexe(permis);
        if ( siCycleActuel(permis)) {
            verifierPermisCycleActuel(permis);
        } else {
            statistique.ajusterDeclarationsTraitees();
            statistique.ajusterDeclarationsIncompletesOuInvalides();
            producteurErreurs(false);
        }
    }

    private static void verifierPermisCycleActuel(Permis permis) throws Exception {
        if (permis.ordre.equalsIgnoreCase("architectes")) {
            hreCyclePrecedentVersInt(permis);
        }
        verifierChqActivite(permis);
        verifierLimite(permis);
        statistique.ajusterDeclarationsTraitees();
        statistique.ajusterSiValideEtIncompletOuValidEtComplet(permis);
        producteurErreurs(true);
    }

    static void hrsCyclePrecedent(Permis permis, int heures) {
        if (heures > 7) {
            tabErreurs.add(permis(permis, "HreEnTropPermis"));
            nbHeuresCyclePrecedent += 7;
        } else if (heures < 0) {
            tabErreurs.add(permis(permis, "HreNegPermis"));
            nbHeuresCyclePrecedent = 0;
        }
    }

    static void hreCyclePrecedentVersInt(Permis permis) {
        double hrePrec = permis.heures_transferees_du_cycle_precedent;
        if ((hrePrec - (int) hrePrec) != 0) {
            tabErreurs.add(permis(permis, "HreFormatPermis"));
            return;
        }
        hrsCyclePrecedent(permis, (int) hrePrec);
    }

    public static boolean heuresTotalesArchitecte(Permis permis) {
        heuresFormationsTotalArchitecte = heuresGroupeDisc + heuresPresentation + nbHeuresCyclePrecedent +
                heuresProjetRech + heuresRedac + heuresQuatreCategories + heuresConference + heuresCours;
        if (cycleArchitectes.get(2).equalsIgnoreCase(permis.cycle)) {
            if(heuresFormationsTotalArchitecte < 40) {
                tabErreurs.add(permis(permis, "HreTotalPermisArchitect"));
                return false;
            }
        } else if (heuresFormationsTotalArchitecte < 42) {
            tabErreurs.add(permis(permis, "HreTotalPermisArchitect"));
            return false;
        }
        return true;
    }

    public static boolean heuresTotalesGeologue(Permis permis) {
        heuresFormationsTotalGeologue = heuresGroupeDisc + heuresPresentation + heuresProjetRech +
                heuresRedac + heuresQuatreCategories + heuresConference + heuresCours;
        if (heuresFormationsTotalGeologue < 55) {
            tabErreurs.add(permis(permis, "HreTotalPermisGeologue"));
            return false;
        }
        return true;
    }

    public static boolean heuresTotalesPsychologue(Permis permis) {
        heuresFormationsTotalPsychologue = heuresGroupeDisc + heuresPresentation + heuresProjetRech +
                heuresRedac + heuresQuatreCategories + heuresConference + heuresCours;
        if (heuresFormationsTotalPsychologue < 90) {
            tabErreurs.add(permis(permis, "HreTotalPermisPsychologue"));
            return false;
        }
        return true;
    }

    public static boolean heuresTotalesPodiatres(Permis permis) {
        heuresFormationsTotalPodiatre = heuresGroupeDisc + heuresPresentation + heuresProjetRech +
                heuresRedac + heuresQuatreCategories + heuresConference + heuresCours;
        if (heuresFormationsTotalPodiatre < 60) {
            tabErreurs.add(permis(permis, "HreTotalPermisPodiatres"));
            return false;
        }
        return true;
    }

    public static boolean heuresTotales(Permis permis) {
        if (permis.ordre.equalsIgnoreCase("architectes")) {
            return heuresTotalesArchitecte(permis);
        } else if (permis.ordre.equalsIgnoreCase("géologues")) {
            return heuresTotalesGeologue(permis);
        } else if (permis.ordre.equalsIgnoreCase("psychologues")){
            return heuresTotalesPsychologue(permis);
        } else if (permis.ordre.equalsIgnoreCase("podiatres")){
            return heuresTotalesPodiatres(permis);
        }
        return false;
    }
}
