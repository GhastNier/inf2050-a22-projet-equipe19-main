package com.inf2050.equipe19;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Predicate;

import static com.inf2050.equipe19.LecteurJson.nbHeuresCompletees;
import static com.inf2050.equipe19.Permis.tabErreurs;
import static com.inf2050.equipe19.Permis.valide;

public class Main extends VerificationPermis {
    public static int nbHeuresCyclePrecedent = 0;
    public static Path cheminFichierEntre, cheminFichierSortie;
    public static String nomFichierEntre, nomFichierSortie;
    private static FileWriter resultats;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    static Statistiques statistique;
    private static boolean avecStats = false;
    private static boolean reinitialise = false;

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                journalErreurConsole.argumentManquant();
            }
            init(args);
            LecteurJson lecteurJson = new LecteurJson();
            lecteurJson.lireEtValiderJson();
        } catch (Exception ignore) {
        }
    }

    private static void init(String[] args) {
        if(args[0].equals("-S")) {
            statistique = Statistiques.lireStatistiques("Statistique.json", false);
            argumentsAvecStatistique(args);
        } else if (args[0].equals("-SR")) {
            reinitialise = true;
            statistique = Statistiques.lireStatistiques("Statistique.json", true);
            argumentsAvecStatistique(args);
        } else {
            argumentsSansStatistique(args);
        }
    }

    private static void argumentsSansStatistique(String[] args) {
        cheminFichierEntre = Paths.get(args[0]);
        cheminFichierSortie = Paths.get(args[1]);
        nomFichierEntre = args[0];
        nomFichierSortie = args[1];
        try {
            resultats = new FileWriter(nomFichierSortie);
        } catch (IOException ignore) {
        }
    }

    private static void argumentsAvecStatistique(String[] args) {
        avecStats = true;
        cheminFichierEntre = Paths.get(args[1]);
        cheminFichierSortie = Paths.get(args[2]);
        nomFichierEntre = args[1];
        nomFichierSortie = args[2];
        try {
            resultats = new FileWriter(nomFichierSortie);
        } catch (IOException ignore) {
        }
    }


    public static void producteurErreurs(boolean b) throws Exception {
        tabErreurs.removeIf(Predicate.isEqual(""));
        CreateurFichierResultat fichierResultat = new CreateurFichierResultat(nbHeuresCompletees, tabErreurs);
        resultats.write(gson.toJson(fichierResultat).replace("\\u0027", "'").replace("true", "\"Complet\"").
                replace("false", "\"Incomplet\""));
        Statistiques.ecrireStatistiques(statistique, new File("Statistique.json"));
        if(avecStats){
            if (reinitialise) System.out.println("\n\n" + "LES STATISTIQUES ONT ÉTÉ REMISES À 0" + "\n\n\n\n");
            System.out.println(statistique);
        }
        resultats.close();
        if (!b) journalErreurConsole.codeDeSorties(Arrays.toString(fichierResultat.erreurs.toArray()));
    }
}



