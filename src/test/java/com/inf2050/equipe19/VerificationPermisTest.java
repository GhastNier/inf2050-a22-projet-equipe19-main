package com.inf2050.equipe19;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.inf2050.equipe19.LecteurJson.gson;
import static com.inf2050.equipe19.VerificationActivites.heuresFormationsTotalArchitecte;

public class VerificationPermisTest {
    private final Permis permis;
    private final Permis permis2;
    private final Permis permis3;
    private final Permis permis4;
    private final Permis permis5;
    private final Permis permis6;
    private final Permis permis7;
    private final Permis permis8;

    VerificationPermisTest() throws IOException {
        Reader lecteur = Files.newBufferedReader(Path.of("src/main/java/input_architecte_mauvais_cycle.json"));
        permis = gson.fromJson(lecteur, Permis.class);
        Reader lecteur2 = Files.newBufferedReader(Path.of("src/main/java/input_valide_geologue.json"));
        permis2 = gson.fromJson(lecteur2, Permis.class);
        Reader lecteur3 = Files.newBufferedReader(Path.of("src/main/java/input_geologue_mauvais_cycle.json"));
        permis3 = gson.fromJson(lecteur3, Permis.class);
        Reader lecteur4 = Files.newBufferedReader(Path.of("src/main/java/input_psychologue.json"));
        permis4 = gson.fromJson(lecteur4, Permis.class);
        Reader lecteur5 = Files.newBufferedReader(Path.of("src/main/java/input_numero_permis_invalide.json"));
        permis5 = gson.fromJson(lecteur5, Permis.class);
        Reader lecteur6 = Files.newBufferedReader(Path.of("src/main/java/input_numero_permis_invalide_long_prem_car_ok.json"));
        permis6 = gson.fromJson(lecteur6, Permis.class);
        Reader lecteur7 = Files.newBufferedReader(Path.of("src/main/java/inputTestChampOrdres.json"));
        permis7 = gson.fromJson(lecteur7, Permis.class);
        Reader lecteur8 = Files.newBufferedReader(Path.of("src/main/java/input_architecte_cycle_21_23.json"));
        permis8 = gson.fromJson(lecteur8, Permis.class);
    }


    @Test
    void siCycleActuel() {
        Assertions.assertFalse(VerificationPermis.siCycleActuel(permis));
    }

    @Test
    void siCycleActuel2() {
        Assertions.assertTrue(VerificationPermis.siCycleActuel(permis2));
    }

    @Test
    void cycleSelonOrdre() {
        Assertions.assertFalse(VerificationPermis.cycleSelonOrdre(permis));
    }

    @Test
    void cycleSelonOrdre2() {
        Assertions.assertFalse(VerificationPermis.cycleSelonOrdre(permis3));
    }

    @Test
    void cycleSelonOrdre3() {
        Assertions.assertTrue(VerificationPermis.cycleSelonOrdre(permis4));
    }

    @Test
    void contientErreurInt() {
        Permis.tabErreurs.clear();
        for (Permis.Activites act : permis7.activites) {
            VerificationActivites.verifierSiHrsEstInt(act);
        }
        Assertions.assertFalse(VerificationPermis.contientPasErreurInt());
    }

    @Test
    void contientPasErreurInt2() {
        Permis.tabErreurs.clear();
        for (Permis.Activites act : permis2.activites) {
            VerificationActivites.verifierSiHrsEstInt(act);
        }
        Assertions.assertTrue(VerificationPermis.contientPasErreurInt());
    }

    @Test
    void hrsCyclePrecedent() {
        Main.nbHeuresCyclePrecedent = 0;
        VerificationPermis.hrsCyclePrecedent(permis, 18);
        assert (Main.nbHeuresCyclePrecedent == 7);
    }

    @Test
    void hrsCyclePrecedent2() {
        Main.nbHeuresCyclePrecedent = 0;
        VerificationPermis.hrsCyclePrecedent(permis, -8);
        assert (Main.nbHeuresCyclePrecedent == 0);
    }

    @Test
    void heuresTotalesArchitecte() {
        Main.nbHeuresCyclePrecedent = 0;
        Permis.tabErreurs.clear();
        for (Permis.Activites act : permis8.activites) {
            CategorieType.interrupteurTypeActivite(act, permis8);
        }
        VerificationPermis.hreCyclePrecedentVersInt(permis8);
        VerificationPermis.heuresTotalesArchitecte(permis8);
        System.out.println("Nombre d'heures complétées pour "
                + "cet architecte: " + heuresFormationsTotalArchitecte);
        Assertions.assertFalse(VerificationPermis.heuresTotalesArchitecte(permis8));
    }
}

