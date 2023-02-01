package com.inf2050.equipe19;

import com.google.gson.Gson;

import java.io.Reader;
import java.nio.file.Files;

import static com.inf2050.equipe19.Main.cheminFichierEntre;
import static com.inf2050.equipe19.Main.statistique;

public class LecteurJson extends VerificationPermis {
    static Gson gson = new Gson();
    static VerificationIntegriterFichierJson verificationFichierJson = new VerificationIntegriterFichierJson();
    static Boolean nbHeuresCompletees = false;
    Permis permis;

    public void lireEtValiderJson() throws Exception {
        journalErreurConsole erreurFichierJson = new journalErreurConsole();
        verificationFichierJson.existe();
        Reader lecteur = Files.newBufferedReader(cheminFichierEntre);
        if (erreurFichierJson.verifierEstValide()) {
            permis = gson.fromJson(lecteur, Permis.class);
            permisEstValide(permis);
        }
        lecteur.close();
    }
}
