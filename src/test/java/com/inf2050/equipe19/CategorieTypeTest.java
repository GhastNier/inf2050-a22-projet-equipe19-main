package com.inf2050.equipe19;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.inf2050.equipe19.LecteurJson.gson;
import static com.inf2050.equipe19.VerificationActivites.listeCategories;

class CategorieTypeTest {

    private Permis permis;

    @BeforeEach
    void preparer() throws IOException {
        Reader lecteur = Files.newBufferedReader(Path.of("src/main/java/input_avec_ordre_architecte.json"));
        permis = gson.fromJson(lecteur, Permis.class);
        VerificationActivites.initListesCategories();
    }

    @Test
    void verificationCategorie() {
        for (Permis.Activites activite : permis.activites) {
            boolean categorieExiste = true;
            for (String categorie : listeCategories) {
                if (!activite.categorie.equalsIgnoreCase(categorie)) {
                    categorieExiste = false;
                    break;
                }
            }
            Assertions.assertFalse(categorieExiste);
        }
    }
}