package com.inf2050.equipe19;

import static com.inf2050.equipe19.CreateurJournal.ErreurPermis.*;

public enum PermisMessage {
    HreTotalPermisArchitect() {
        @Override
        public String affichagePermis(Permis permis) {
            return hrsRequiseInvalideArchitecte();
        }
    },
    HreTotalPermisGeologue() {
        @Override
        public String affichagePermis(Permis permis) {
            return hrsRequiseInvalideGeologue();
        }
    },
    HreTotalPermisPsychologue() {
        @Override
        public String affichagePermis(Permis permis) {
            return hrsRequiseInvalidePsychologue();
        }
    },
    HreTotalPermisPodiatres() {
        @Override
        public String affichagePermis(Permis permis) {
            return hrsRequiseInvalidePodiatre();
        }
    },
    MinHreSixPermis() {
        @Override
        public String affichagePermis(Permis permis) {
            return minHrsNonAtteintQuatreCategories();
        }
    },
    EntrerInvalide(){
        @Override
        public String affichagePermis(Permis permis) {
            return "Le fichier d'entrée est invalide.";
        }
    },
    CyclePermis() {
        @Override
        public String affichagePermis(Permis permis) {
            return cycleInvalide(permis);
        }
    },
    HreFormatPermis() {
        @Override
        public String affichagePermis(Permis permis) {
            return hrsEnTrop();
        }
    },
    HreEnTropPermis() {
        @Override
        public String affichagePermis(Permis permis) {
            return CreateurJournal.ErreurPermis.hrsEnTrop(permis);
        }
    },
    HreNegPermis() {
        @Override
        public String affichagePermis(Permis permis) {
            return hrsSontNegative();
        }
    },
    NumeroInvalidePermis() {
        @Override
        public String affichagePermis(Permis permis) {
            return formatNumeroDePermis(permis);
        }
    },
    InvalidePremierLettrePermis() {
        @Override
        public String affichagePermis(Permis permis) {return formatPermisPremierLettre(permis);}
    },
    NomPermis() {
        @Override
        public String affichagePermis(Permis permis) {return formatNom(); }
    },
    PrenomPermis() {
        @Override
        public String affichagePermis(Permis permis) {return formatPrenom(); }
    },
    SexePermis() {
        @Override
        public String affichagePermis(Permis permis) {return sexeInvalide(); }
    },
    MinHreCoursPermisGeologue () {
        @Override
        public String affichagePermis(Permis permis) {
            return hrsCoursRequiseInvalideGeologue();
        }
    },
    MinHreCoursPermisPsychologue() {
        @Override
        public String affichagePermis(Permis permis) {
            return hrsRequiseCoursInvalidePsychologue();
        }
    },
    MinHreProjDeRechPermis() {
        @Override
        public String affichagePermis(Permis permis) {
            return hrsProjetDeRechRequiseInvalide();
        }
    },
    MinHreCoursPermisPodiatres () {
        @Override
        public String affichagePermis(Permis permis) {
            return hrsCoursRequiseInvalidePodiatre();
        }
    };

    public abstract String affichagePermis(Permis permis);
}
