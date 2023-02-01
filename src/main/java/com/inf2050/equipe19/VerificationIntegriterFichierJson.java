package com.inf2050.equipe19;

import java.io.IOException;
import java.nio.file.Files;

import static com.inf2050.equipe19.CreateurJournal.journalErreurConsole.dateEtHeureActuelle;
import static com.inf2050.equipe19.Main.cheminFichierEntre;
import static java.lang.System.*;

public class VerificationIntegriterFichierJson {
    String fichierType = "Inconnu";

    public void existe() {
        if (!Files.exists(cheminFichierEntre)) {
            out.println("Fichier d'entrée introuvable");
            exit(-10);
        } else {
            estJson();
        }
    }

    void estJson() {
        fichierType = obtenirTypeDeFichier();
        if (!fichierType.equals("application/json")) {
            err.append(dateEtHeureActuelle).append(" Fichier de type [ ").append(fichierType).append(" ] non valide.");
            out.println("Fichier de type [ " + fichierType + " ] non valide.");
            exit(-20);
        }
    }

    String obtenirTypeDeFichier() {
        String typeFichier = "Inconnu";
        try {
            typeFichier = Files.probeContentType(cheminFichierEntre);
        } catch (IOException ignored) {
        }
        return typeFichier;
    }
}

