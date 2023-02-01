package com.inf2050.equipe19;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.inf2050.equipe19.LecteurJson.gson;

public class StatistiqueTest {
    private final Permis permis;

    StatistiqueTest() throws IOException {
        Reader lecteur = Files.newBufferedReader(Path.of("src/main/java/input_architecte_mauvais_cycle.json"));
        permis = gson.fromJson(lecteur, Permis.class);
    }
    @Test
    void verifierDeclarationTraitee(){
        Statistiques statistiques = Statistiques.lireStatistiques(String.valueOf(permis), true);
        assert (statistiques.declarationTraitees == 0);
        statistiques.declarationTraitees++;
        assert (statistiques.declarationTraitees == 1);
    }

    @Test
    void verifierDeclarationCompletes(){
        Statistiques statistiques = Statistiques.lireStatistiques(String.valueOf(permis), true);
        statistiques.declarationCompletes++;
        assert (statistiques.declarationCompletes == 1);
        statistiques.declarationCompletes--;
        assert (statistiques.declarationCompletes == 0);
    }

    @Test
    void verifierDeclarationIncomplete(){
        Statistiques statistiques = Statistiques.lireStatistiques(String.valueOf(permis), true);
        statistiques.declarationIncompletesOuInvalides++;
        assert (!(statistiques.declarationIncompletesOuInvalides == 0));
    }

    @Test
    void verifierDeclarationHomme(){
        Statistiques statistiques = Statistiques.lireStatistiques(String.valueOf(permis), true);
        assert (statistiques.declarationSexeHommes == 0);
        for (int i = 0; i < 5; i++) {
            statistiques.declarationSexeHommes++;
        }
        assert (!(statistiques.declarationSexeHommes == 0));
        assert (statistiques.declarationSexeHommes == 5);
    }
    @Test
    void verifierDeclarationFemme(){
        Statistiques statistiques = Statistiques.lireStatistiques(String.valueOf(permis), true);
        assert (statistiques.declarationSexeFemmes == 0);
        for (int i = 0; i < 5; i++) {
            statistiques.declarationSexeFemmes++;
        }
        assert (!(statistiques.declarationSexeFemmes == 0));
        assert (statistiques.declarationSexeFemmes == 5);
    }

    @Test
    void verifierDeclarationSexeInconnu(){
        Statistiques statistiques = Statistiques.lireStatistiques(String.valueOf(permis), true);
        assert (statistiques.declarationSexeInconnu == 0);
        assert (!(statistiques.declarationSexeFemmes == 4));
    }

    @Test
    void verifierDeclarationPermisInvalide(){
        Statistiques statistiques = Statistiques.lireStatistiques(String.valueOf(permis), true);
        assert (statistiques.declarationPermisInvalide == 0);
    }

    @Test
    void verifierDeclaration(){
        Statistiques statistiques = Statistiques.lireStatistiques(String.valueOf(permis), true);
        assert (statistiques.declarationValidesEtCompletes == 0);
    }
}

