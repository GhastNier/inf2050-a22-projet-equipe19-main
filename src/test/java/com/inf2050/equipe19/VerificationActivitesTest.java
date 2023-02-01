package com.inf2050.equipe19;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;

import static com.inf2050.equipe19.LecteurJson.gson;

class VerificationActivitesTest {
    private final Permis permis;
    private final Permis permis2;
    private final Permis permis3;
    private final Permis permis4;

    VerificationActivitesTest() throws IOException {
        Reader lecteur = Files.newBufferedReader(Path.of("src/main/java/input_avec_ordre_architecte.json"));
        permis = gson.fromJson(lecteur, Permis.class);
        Reader lecteur2 = Files.newBufferedReader(Path.of("src/main/java/input_valide_geologue.json"));
        permis2 = gson.fromJson(lecteur2, Permis.class);
        Reader lecteur3 = Files.newBufferedReader(Path.of("src/main/java/input_psychologue.json"));
        permis3 = gson.fromJson(lecteur3, Permis.class);
        Reader lecteur4 = Files.newBufferedReader(Path.of("src/main/java/input_geologue_mauvaise_date.json"));
        permis4 = gson.fromJson(lecteur4, Permis.class);

    }

    @BeforeAll
    static void initCat(){
        VerificationActivites.initListesCategories();
    }

    @BeforeEach
    void viderTabErreurs() {
        Permis.tabErreurs.clear();
    }

    @Test
    void initListesCategories() {
        assert (VerificationActivites.listeCategories.containsAll(Arrays.asList("cours", "atelier", "séminaire", "colloque", "conférence", "lecture dirigée",
                "présentation", "groupe de discussion", "projet de recherche", "rédaction professionnelle")));
    }

    @Test
    void initListesCategories2(){
        assert (VerificationActivites.listeSixCours.containsAll(Arrays.asList("Cours", "Atelier", "Séminaire", "Colloque", "Conférence", "Lecture dirigée")));
    }


    @Test
    void verifierDescription() {
        Assertions.assertFalse(VerificationActivites.verifierDescription(permis));
    }

    @Test
    void verifierDescription2() {
        Assertions.assertTrue(VerificationActivites.verifierDescription(permis2));
    }

    @Test
    void verifierCategories() {
        assert (Permis.tabErreurs.isEmpty());
        for (Permis.Activites act : permis.activites) {
            VerificationActivites.verifierCategories(act, permis);
        }
    }

    @Test
    void verifierCategories2(){
        assert (Permis.tabErreurs.isEmpty());
        for (Permis.Activites act : permis2.activites) {
            VerificationActivites.verifierCategories(act, permis2);
        }
        Assertions.assertTrue(Permis.tabErreurs.isEmpty());
    }

    @Test
    void verifierMaxParJour() {
        assert (Permis.tabErreurs.isEmpty());
        VerificationActivites.verifierMaxParJour(permis);
        Assertions.assertFalse(Permis.tabErreurs.isEmpty());
    }

    @Test
    void verifierMaxParJour2() {
        assert (Permis.tabErreurs.isEmpty());
        VerificationActivites.verifierMaxParJour(permis2);
        Assertions.assertFalse(Permis.tabErreurs.contains("Un maximum de 10 heures par jour est permis par activité. Seules 10 heures seront"));
    }

    @Test
    void changerHrsAct() {
        VerificationActivites.heuresCours = 18;
        VerificationActivites.changerHrsAct(permis2.activites.get(0), 10);
        assert (VerificationActivites.heuresCours == 8);
    }

    @Test
    void verifierLimite() {
        VerificationActivites.heuresCours = 15;
        assert (Permis.tabErreurs.isEmpty());
        VerificationActivites.verifierLimite(permis2);
        Assertions.assertFalse(Permis.tabErreurs.isEmpty());
    }

    @Test
    void verifierLimite2() {
        VerificationActivites.heuresProjetRech = 3;
        assert (Permis.tabErreurs.isEmpty());
        VerificationActivites.verifierLimite(permis2);
        Assertions.assertTrue(Permis.tabErreurs.isEmpty());
    }

    @Test
    void verifierLimiteArchitecte() {
        VerificationActivites.heuresProjetRech = 25;
        VerificationActivites.verifierLimiteArchitecte(permis);
        assert (VerificationActivites.heuresProjetRech == 23);
    }

    @Test
    void verifierLimiteArchitecte2() {
        VerificationActivites.heuresGroupeDisc = 30;
        VerificationActivites.verifierLimite(permis);
        assert (VerificationActivites.heuresGroupeDisc == 17);
    }

    @Test
    void verifierDateActArchitecte() {
        Assertions.assertFalse(VerificationActivites.verifierDateActArchitecte(
                permis.activites.get(3), LocalDate.parse(permis.activites.get(3).date)
        ));
    }

    @Test
    void verifierDateActArchitecte2() {
        Assertions.assertTrue(VerificationActivites.verifierDateActArchitecte(
                permis.activites.get(0), LocalDate.parse(permis.activites.get(0).date)
        ));
    }

    @Test
    void verifierDateActPsychologue() {
        Assertions.assertFalse(VerificationActivites.verifierDateActPsychologue(
                permis3.activites.get(0), LocalDate.parse(permis3.activites.get(0).date)
        ));
    }

    @Test
    void verifierDateActPsychologue2() {
        Assertions.assertTrue(VerificationActivites.verifierDateActPsychologue(
                permis3.activites.get(1), LocalDate.parse(permis3.activites.get(1).date)
        ));
    }

    @Test
    void verifierDateActGeologue() {
        Assertions.assertFalse(VerificationActivites.verifierDateActGeologue(
                permis4.activites.get(0), LocalDate.parse(permis4.activites.get(0).date)
        ));
    }

    @Test
    void verifierDateActGeologue2() {
        Assertions.assertTrue(VerificationActivites.verifierDateActGeologue(
                permis2.activites.get(1), LocalDate.parse(permis2.activites.get(1).date)
        ));
    }

    @Test
    void verifierSiDateValide() {
        Assertions.assertFalse(VerificationActivites.verifierSiDateValide(
                permis.activites.get(2), permis
        ));
    }

    @Test
    void verifierSiHrsEstInt() {
        assert (Permis.tabErreurs.isEmpty());
        VerificationActivites.verifierSiHrsEstInt(permis.activites.get(2));
        Assertions.assertFalse(Permis.tabErreurs.isEmpty());
    }


}