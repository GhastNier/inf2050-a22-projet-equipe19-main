package com.inf2050.equipe19;

import com.inf2050.equipe19.Permis.Activites;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.inf2050.equipe19.CategorieType.interrupteurTypeActivite;
import static com.inf2050.equipe19.LecteurJson.nbHeuresCompletees;
import static com.inf2050.equipe19.Main.nbHeuresCyclePrecedent;
import static com.inf2050.equipe19.Permis.tabErreurs;
import static com.inf2050.equipe19.Permis.valide;
import static com.inf2050.equipe19.VerificationPermis.heuresTotales;

public class VerificationActivites extends GestionnaireJournalErreurs {
    static LocalDate cycleDebut2021 = LocalDate.parse("2021-04-01");
    static LocalDate cycleFin2021 = LocalDate.parse("2023-04-01");
    static LocalDate cycleDebut2018 = LocalDate.parse("2018-04-01");
    static LocalDate cycleFin2018 = LocalDate.parse("2020-04-01");
    static LocalDate cycleDebut2016 = LocalDate.parse("2016-04-01");
    static LocalDate cycleFin2016 = LocalDate.parse("2018-07-01");
    static LocalDate cycleDebutPsych = LocalDate.parse("2018-01-01");
    static LocalDate cycleFinPsych = LocalDate.parse("2023-01-01");
    static LocalDate cycleDebutGeo = LocalDate.parse("2019-06-01");
    static LocalDate cycleFinGeo = LocalDate.parse("2022-06-01");
    static LocalDate dateActivite;
    static int heuresPresentation = 0;
    static int heuresGroupeDisc = 0;
    static int heuresProjetRech = 0;
    static int heuresRedac = 0;
    static int heuresQuatreCategories = 0;
    static int heuresAtelierPourStats = 0;
    static int heuresSeminairePourStats = 0;
    static int heuresColloquePourStats = 0;
    static int heuresLectureDirigeePourStats = 0;
    static int heuresFormationsTotalArchitecte = 0;
    static int heuresFormationsTotalGeologue = 0;
    static int heuresFormationsTotalPsychologue = 0;
    static int heuresFormationsTotalPodiatre = 0;
    static int heuresCours = 0;
    static int heuresConference = 0;
    static List<String> listeCategories = new ArrayList<>();
    static List<String> listeSixCours = new ArrayList<>();

    static void initListesCategories() {
        listeCategories.addAll(Arrays.asList("cours", "atelier", "séminaire", "colloque", "conférence", "lecture dirigée",
                "présentation", "groupe de discussion", "projet de recherche", "rédaction professionnelle"));
        listeSixCours.addAll(Arrays.asList("Cours", "Atelier", "Séminaire", "Colloque", "Conférence", "Lecture dirigée"));
    }

    static void verifierChqActivite(Permis permis) {
        initListesCategories();
        permis.activites.forEach(act -> verifierCategories(act, permis));
        verifierDescription(permis);
        verifierMaxParJour(permis);
        if (heuresQuatreCategories + heuresCours + nbHeuresCyclePrecedent + heuresConference < 17
                && permis.ordre.equalsIgnoreCase("architectes")) {
            tabErreurs.add(GestionnaireJournalErreurs.permis(permis, "MinHreSixPermis"));
        }
        LecteurJson.nbHeuresCompletees = heuresTotales(permis);
    }

    static boolean verifierDescription(Permis permis) {
        for (Activites act : permis.activites) {
            if (act.description.length() <= 20) {
                nbHeuresCompletees = false;
                tabErreurs.add(activites(act, "DescVingtCar"));
                valide = false;
                return false;
            }
        }
        return true;
    }

    static void verifierCategories(Activites act, Permis permis) {
        if (!(listeCategories.contains(act.categorie))) {
            valide = false;
            tabErreurs.add(activites(act, "ActCategorie"));
        } else {
            interrupteurTypeActivite(act, permis);
        }
    }

    static void verifierMaxParJour(Permis permis) {
        simplifierActivites(permis);
        for (Activites activite : permis.activites) {
            if (activite.heures > 10) {
                tabErreurs.add(activites(activite, "maxHrsParJour"));
                double diff = activite.heures - 10;
                changerHrsAct(activite, diff);
            }
        }
    }
    static void simplifierActivites(Permis permis) {
        ArrayList<Activites> activites = new ArrayList<>();
        for (Activites activite : permis.activites) {
            Activites activiteExistante = activites.stream().filter(a -> a.description.equals(activite.description)
                    && a.date.equals(activite.date)).findAny().orElse(null);
            if (activiteExistante != null) {
                activiteExistante.heures += activite.heures;
            } else {
                activites.add(activite);
            }
        }
        permis.activites = activites;
    }

    static void changerHrsAct(Activites act, double diff) {
        switch (act.categorie) {
            case "atelier", "séminaire", "colloque", "lecture dirigée" -> {
                heuresQuatreCategories -= diff;
                changerHrsActPourStats(act, diff);
            }
            case "présentation" -> heuresPresentation -= diff;
            case "groupe de discussion" -> heuresGroupeDisc -= diff;
            case "projet de recherche" -> heuresProjetRech -= diff;
            case "rédaction professionnelle" -> heuresRedac -= diff;
            case "cours" -> heuresCours -= diff;
            case "conférence" -> heuresConference -= diff;
        }
    }

    static void changerHrsActPourStats (Activites act, double diff){
        switch (act.categorie) {
            case "atelier" -> heuresAtelierPourStats -= diff;
            case "séminaire" -> heuresSeminairePourStats -= diff;
            case "colloque" -> heuresColloquePourStats -= diff;
            case "lecture dirigée" -> heuresLectureDirigeePourStats -= diff;
        }
    }

    static void verifierLimite(Permis permis) { // C'est bon Super.
        verifierLimiteHeuresCours(permis);

        if (heuresProjetRech < 3 && permis.ordre.equalsIgnoreCase("géologues")) {
            tabErreurs.add(GestionnaireJournalErreurs.permis(permis, "MinHreProjDeRechPermis"));
        }
        if (heuresProjetRech < 3 && permis.ordre.equalsIgnoreCase("podiatres")) {
            tabErreurs.add(GestionnaireJournalErreurs.permis(permis, "MinHreProjDeRechPermis"));
        }
        if (heuresConference >= 15 && permis.ordre.equalsIgnoreCase("psychologues")) heuresConference = 15;
        verifierLimiteArchitecte(permis);
    }

    private static void verifierLimiteHeuresCours(Permis permis) {
        if (heuresCours < 22 && permis.ordre.equalsIgnoreCase("géologues")) {
            tabErreurs.add(GestionnaireJournalErreurs.permis(permis, "MinHreCoursPermisGeologue"));
        } else if (heuresCours < 22 && permis.ordre.equalsIgnoreCase("podiatres")) {
            tabErreurs.add(GestionnaireJournalErreurs.permis(permis, "MinHreCoursPermisPodiatres"));
        } else if (heuresCours < 25 && permis.ordre.equalsIgnoreCase("psychologues")) {
            tabErreurs.add(GestionnaireJournalErreurs.permis(permis, "MinHreCoursPermisPsychologue"));
        }
    }

    static void verifierLimiteArchitecte(Permis permis) {
        if (heuresProjetRech >= 23 && permis.ordre.equalsIgnoreCase("architectes")) {
            heuresProjetRech = 23;
        }
        if (heuresGroupeDisc >= 17 && permis.ordre.equalsIgnoreCase("architectes")) {
            heuresGroupeDisc = 17;
        }
        if (heuresPresentation >= 23 && permis.ordre.equalsIgnoreCase("architectes")) {
            heuresPresentation = 23;
        }
        if (heuresRedac >= 17 && permis.ordre.equalsIgnoreCase("architectes")) heuresRedac = 17;
    }

    static boolean verifierDateActArchitecte(Activites act, LocalDate dateAct) {
        if (!dateAct.isEqual(cycleDebut2021) && !(dateAct.isAfter(cycleDebut2021) &&
                dateAct.isBefore(cycleFin2021)) && !dateAct.isEqual(cycleFin2021)
                && !dateAct.isEqual(cycleDebut2016) && !(dateAct.isAfter(cycleDebut2016) &&
                dateAct.isBefore(cycleFin2016)) && !dateAct.isEqual(cycleFin2016)
                && !dateAct.isEqual(cycleDebut2018) && !(dateAct.isAfter(cycleDebut2018) &&
                dateAct.isBefore(cycleFin2018)) && !dateAct.isEqual(cycleFin2018)) {
            valide = false;
            tabErreurs.add(activites(act, "ActHorsCycle"));
            return false;
        }
        return true;
    }

    static boolean verifierDateActPsychologue(Activites act, LocalDate dateAct) {
        if (!dateAct.isEqual(cycleDebutPsych) && !(dateAct.isAfter(cycleDebutPsych) &&
                dateAct.isBefore(cycleFinPsych)) && !dateAct.isEqual(cycleFinPsych)) {
            valide = false;
            tabErreurs.add(activites(act, "ActHorsCycle"));
            return false;
        }
        return true;
    }

    static boolean verifierDateActGeologue(Activites act, LocalDate dateAct) {
        if (!dateAct.isEqual(cycleDebutGeo) && !(dateAct.isAfter(cycleDebutGeo) &&
                dateAct.isBefore(cycleFinGeo)) && !dateAct.isEqual(cycleFinGeo)) {
            valide = false;
            tabErreurs.add(activites(act, "ActHorsCycle"));
            return false;
        }
        return true;
    }

    static boolean verifierSiDateValide(Activites act, Permis permis) {
        try {
            dateActivite = LocalDate.parse(act.date);
        } catch (DateTimeParseException e) {
            valide = false;
            tabErreurs.add(activites(act, "ActFormatDate"));
            return false;
        }
        return heuresOrdreValide(act, permis.ordre.toLowerCase(Locale.ROOT));

    }

    private static boolean heuresOrdreValide(Activites act, String ordre) {
        return switch (ordre) {
            case "architectes" -> verifierDateActArchitecte(act, LocalDate.parse(act.date));
            case "géologues", "podiatres" -> verifierDateActGeologue(act, LocalDate.parse(act.date));
            case "psychologues" -> verifierDateActPsychologue(act, LocalDate.parse(act.date));
            default -> false;
        };
    }


    static int verifierSiHrsEstInt(Activites act) {
        double hreAct = act.heures;
        if ((hreAct - (int) hreAct) != 0 || (int) hreAct < 0) {
            valide = false;
            tabErreurs.add(activites(act, "ActFormatHre"));
            return 0;
        } else {
            return (int) hreAct;
        }
    }
}

