package com.inf2050.equipe19;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.inf2050.equipe19.LecteurJson.nbHeuresCompletees;
import static com.inf2050.equipe19.Permis.valide;
import static java.nio.file.Files.newBufferedReader;

public class Statistiques {
    public int declarationTraitees = 0;
    public int declarationCompletes = 0;
    public int declarationIncompletesOuInvalides = 0;
    public int declarationSexeHommes = 0;
    public int declarationSexeFemmes = 0;
    public int declarationSexeInconnu = 0;
    public int nbTotalActivites = 0;
    public HashMap<String, Integer> activitesParCategorie = new HashMap<>();
    public int declarationValidesEtCompletes = 0;
    public int declarationValidesEtCompletesArchitectes = 0;
    public int declarationValidesEtCompletesPsychologues = 0;
    public int declarationValidesEtCompletesGeologues = 0;
    public int declarationValidesEtCompletesPodiatres = 0;
    public int declarationValidesEtIncompletes = 0;
    public int declarationValidesEtIncompletesArchitectes = 0;
    public int declarationValidesEtIncompletesPsychologues = 0;
    public int declarationValidesEtIncompletesGeologues = 0;
    public int declarationValidesEtIncompletesPodiatres = 0;
    public int declarationPermisInvalide = 0;

    public Statistiques(String fichier) throws IOException {
        Path cheminFichierStats = Paths.get(fichier);
        Gson gson = new Gson();
        Statistiques stats;
        if(Files.exists(cheminFichierStats)) {
            JsonReader lecteur = new JsonReader(newBufferedReader(cheminFichierStats));
            lecteur.setLenient(true);
            stats = gson.fromJson(lecteur, Statistiques.class);
            ConstructeurPremierPartie(stats);
            ConstructeurDeuxiÃ¨mePartie(stats);
        } else {
            new Statistiques();
        }
    }

    public void ConstructeurPremierPartie(Statistiques stats) {
        this.declarationTraitees = stats.declarationTraitees;
        this.declarationCompletes = stats.declarationCompletes;
        this.declarationIncompletesOuInvalides = stats.declarationIncompletesOuInvalides;
        this.declarationSexeHommes = stats.declarationSexeHommes;
        this.declarationSexeFemmes = stats.declarationSexeFemmes;
        this.declarationSexeInconnu = stats.declarationSexeInconnu;
        this.nbTotalActivites = stats.nbTotalActivites;
        this.activitesParCategorie = stats.activitesParCategorie;
        this.declarationValidesEtCompletes = stats.declarationValidesEtCompletes;
        this.declarationValidesEtCompletesArchitectes = stats.declarationValidesEtCompletesArchitectes;
        this.declarationValidesEtCompletesPsychologues = stats.declarationValidesEtCompletesPsychologues;
    }
    private void ConstructeurDeuxiÃ¨mePartie(Statistiques stats) {
        this.declarationValidesEtCompletesGeologues = stats.declarationValidesEtCompletesGeologues;
        this.declarationValidesEtCompletesPodiatres = stats.declarationValidesEtCompletesPodiatres;
        this.declarationValidesEtIncompletes = stats.declarationValidesEtIncompletes;
        this.declarationValidesEtIncompletesArchitectes = stats.declarationValidesEtIncompletesArchitectes;
        this.declarationValidesEtIncompletesPsychologues = stats.declarationValidesEtIncompletesPsychologues;
        this.declarationValidesEtIncompletesGeologues = stats.declarationValidesEtIncompletesGeologues;
        this.declarationValidesEtIncompletesPodiatres = stats.declarationValidesEtIncompletesPodiatres;
        this.declarationPermisInvalide = stats.declarationPermisInvalide;
    }

    public Statistiques(){
        this.activitesParCategorie = initHashmap();
    }

    private HashMap<String, Integer> initHashmap(){
        if(activitesParCategorie == null) {
            List<String> cles = new ArrayList<>();
            cles.addAll(Arrays.asList("cours", "atelier", "sÃ©minaire", "colloque", "confÃ©rence", "lecture dirigÃ©e",
                    "prÃ©sentation", "groupe de discussion", "projet de recherche", "rÃ©daction professionnelle"));
            for (String s : cles) {
                activitesParCategorie.put(s, 0);
            }
        }
        return activitesParCategorie;
    }

    public void ajusterDeclarationsTraitees(){
        declarationTraitees += 1;
    }
     public void ajusterDeclarationsCompletesEtSelonOrdre(Permis permis){
        declarationCompletes += 1;
        switch (permis.ordre.toLowerCase()){
            case "architectes" -> declarationValidesEtCompletesArchitectes += 1;
            case "gÃ©ologues" -> declarationValidesEtCompletesGeologues += 1;
            case "psychologues" -> declarationValidesEtCompletesPsychologues += 1;
            case "podiatres" -> declarationValidesEtCompletesPodiatres += 1;
        }
     }

     public void ajusterDeclarationsValidesEtIncompletesEtSelonOrdre(Permis permis){
        declarationValidesEtIncompletes += 1;
        switch (permis.ordre.toLowerCase()){
            case "architectes" -> declarationValidesEtIncompletesArchitectes += 1;
            case "gÃ©ologues" -> declarationValidesEtIncompletesGeologues += 1;
            case "psychologues" -> declarationValidesEtIncompletesPsychologues += 1;
            case  "podiatres" -> declarationValidesEtIncompletesPodiatres += 1;
        }
     }

     public void ajusterDeclarationsIncompletesOuInvalides(){
        declarationIncompletesOuInvalides += 1;
     }

     public void ajusterDeclarationSelonSexe(Permis permis){
        switch (permis.sexe){
            case 0 -> declarationSexeInconnu += 1;
            case 1 -> declarationSexeHommes += 1;
            case 2 -> declarationSexeFemmes += 1;
        }
     }

     public void ajusterDeclarationPermisInvalide(){
        declarationPermisInvalide += 1;
     }

     public void ajusterTotalParCategorie(Permis permis){
         for (Permis.Activites act : permis.activites) {
             activitesParCategorie.put(act.categorie.toLowerCase(),
                                        activitesParCategorie.getOrDefault(act.categorie.toLowerCase(), 0) + 1);
         }
     }

     public void ajusterTotal(){
        nbTotalActivites = 0;
         for (Integer i : activitesParCategorie.values()) {
             nbTotalActivites += i;
         }
     }

     public void ajusterSiValideEtIncompletOuValidEtComplet(Permis permis){
        if(valide){
            ajusterDeclarationSelonSexe(permis);
            ajusterTotalParCategorie(permis);
            ajusterTotal();
            if(nbHeuresCompletees) ajusterDeclarationsCompletesEtSelonOrdre(permis);
            else {
                ajusterDeclarationsValidesEtIncompletesEtSelonOrdre(permis);
            }
        } else {
            ajusterDeclarationsIncompletesOuInvalides();
        }
     }

    public static Statistiques lireStatistiques(String fichier, boolean reinitialiser) {
        if (reinitialiser) {
            return new Statistiques();
        } else {
            try {
                return new Statistiques(fichier);
            } catch (Exception e) {
                return new Statistiques();
            }
        }
    }

    public static void ecrireStatistiques(Statistiques statistiques, File fichier) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().setLenient().create();
        Object obj = gson.toJson(statistiques);
        FileWriter fileWriter =  new FileWriter(fichier);
        fileWriter.write(obj.toString());
        fileWriter.close();
    }

    @Override
    public String toString(){
        return "STATISTIQUES" + "\n" + "Nombre de dÃ©clarations traitÃ©es : " + declarationTraitees +
                "\n" + "Nombre de dÃ©clarations complÃ¨tes : " + declarationCompletes +
                "\n" + "Nombre de dÃ©clarations incomplÃ¨tes ou invalides : " + declarationIncompletesOuInvalides +
                "\n" + "Nombre de dÃ©clarations faites par des hommes : " + declarationSexeHommes +
                "\n" + "Nombre de dÃ©clarations faites par des femmes : " + declarationSexeFemmes +
                "\n" + "Nombre de dÃ©clarations faites par des gens de sexe inconnu : " + declarationSexeInconnu +
                "\n" + "Nombre total d'activitÃ©s dans les dÃ©clarations valides : " + nbTotalActivites +
                "\n" + "Nombre total de dÃ©clarations valides et complÃ¨tes pour les architectes : " +
                    declarationValidesEtCompletesArchitectes +
                "\n" + "Nombre total de dÃ©clarations valides et complÃ¨tes pour les psychologues : " +
                    declarationValidesEtCompletesPsychologues +
                "\n" + "Nombre total de dÃ©clarations valides et complÃ¨tes pour les gÃ©ologues : " +
                    declarationValidesEtCompletesGeologues +
                "\n" + "Nombre total de dÃ©clarations valides et complÃ¨tes pour les podiatres : " +
                    declarationValidesEtCompletesPodiatres +
                "\n" + "Nombre total de dÃ©clarations valides et incomplÃ¨tes pour les architectes : " +
                    declarationValidesEtIncompletesArchitectes +
                "\n" + "Nombre total de dÃ©clarations valides et incomplÃ¨tes pour les psychologues : " +
                    declarationValidesEtIncompletesPsychologues +
                "\n" + "Nombre total de dÃ©clarations valides et incomplÃ¨tes pour les gÃ©ologues : " +
                    declarationValidesEtIncompletesGeologues +
                "\n" + "Nombre total de dÃ©clarations valides et incomplÃ¨tes pour les podiatres : " +
                    declarationValidesEtIncompletesPodiatres +
                "\n" + "Nombre de dÃ©clarations soumises avec un numÃ©ro de permis invalide : " +
                    declarationPermisInvalide;
    }
}