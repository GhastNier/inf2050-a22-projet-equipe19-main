package com.inf2050.equipe19;

import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;

import static com.inf2050.equipe19.GestionnaireJournalErreurs.activites;

public class Permis {
    public String numero_de_permis, cycle, ordre, nom, prenom; ;
    public int sexe;
    public static ArrayList<String> tabErreurs = new ArrayList<>();
    public double heures_transferees_du_cycle_precedent;
    public ArrayList<Activites> activites;
    public static boolean valide = true;

    public Permis() {
    }

    private Permis(Constructeur constructeur) {
        try {
            this.numero_de_permis = constructeur.numero_de_permis;
            this.nom = constructeur.nom;
            this.prenom =constructeur.prenom;
            this.sexe = constructeur.sexe;
            this.ordre = constructeur.ordre;
            this.cycle = constructeur.cycle;
            construireHeureCyclePrecedent(constructeur);
            this.activites = constructeur.activites;
        } catch (JsonSyntaxException e) {
            this.heures_transferees_du_cycle_precedent = 0;
            tabErreurs.add(GestionnaireJournalErreurs.permis(this, "HreFormatPermis"));
            e.printStackTrace();
        }
    }

    private void construireHeureCyclePrecedent(Constructeur constructeur) {
        if (constructeur.ordre.equalsIgnoreCase("architecte")) {
            this.heures_transferees_du_cycle_precedent = constructeur.heures_transferees_du_cycle_precedent;
        } else {
            this.heures_transferees_du_cycle_precedent = 0;
        }
    }

    @Override
    public String toString() {
        return "Numéro de permis: " + this.numero_de_permis
                + "\nNom: " + this.nom
                + "\nPrenom: " + this.prenom
                + "\nSexe " + this.sexe
                + "\nCycle: " + this.cycle
                + "\nHeures du Cycle Précédent: " + this.heures_transferees_du_cycle_precedent
                + "\nActivités: " + this.activites;
    }

    public static class Activites {
        String description, categorie, date;
        double heures;

        public Activites() {
        }

        public Activites(String description, String categorie, double heures, String date) {
            try {
                this.description = description;
                this.categorie = categorie;
                this.heures = heures;
                this.date = date;
            } catch (NumberFormatException e) {
                this.heures = 0;
                e.printStackTrace();
                tabErreurs.add(activites(this, "ActFormatHre"));

            }
        }

        @Override
        public String toString() {
            return " \n Description: " + description + '\n' +
                    " Categorie: " + categorie + '\n' +
                    " Heures: " + heures +
                    "\n Date: " + date + "\n";
        }

        @Override
        public boolean equals(Object o) {
            if (o.equals(this)) {
                return true;
            }
            if (o.getClass() != this.getClass()) {
                return false;
            }
            return (((Activites) o).description.equalsIgnoreCase(this.description) && ((Activites) o).categorie.equalsIgnoreCase(this.categorie) &&
                    ((Activites) o).date.equalsIgnoreCase(this.date));
        }
    }

    public static class Constructeur {
        private String numero_de_permis;
        private String nom;
        private String prenom;
        private int sexe;
        private String ordre;
        private String cycle;
        private double heures_transferees_du_cycle_precedent;
        private ArrayList<Permis.Activites> activites;

        public Constructeur definirNumeroDePermis(String numero_de_permis) {
            this.numero_de_permis = numero_de_permis;
            return this;
        }

        public Constructeur definirNom(String nom) {
            this.nom = nom;
            return this;
        }

        public Constructeur definirPrenom(String prenom) {
            this.prenom = prenom;
            return this;
        }

        public Constructeur definirSexe(int sexe) {
            this.sexe = sexe;
            return this;
        }

        public Constructeur definirOrdre(String ordre) {
            this.ordre = ordre;
            return this;
        }

        public Constructeur definirCycle(String cycle) {
            this.cycle = cycle;
            return this;
        }

        public Constructeur definirHeuresTransfereesDuCyclePrecedent(double heures_transferees_du_cycle_precedent) {
            this.heures_transferees_du_cycle_precedent = heures_transferees_du_cycle_precedent;
            return this;
        }

        public Constructeur definirActivites(ArrayList<Permis.Activites> activites) {
            this.activites = activites;
            return this;
        }

        public Permis creerPermis() {
            return new Permis(this);
        }
    }
}




