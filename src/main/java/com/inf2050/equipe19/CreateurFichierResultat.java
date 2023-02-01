package com.inf2050.equipe19;

import java.util.List;

public class CreateurFichierResultat {
    public boolean complet;
    public List<String> erreurs;

    public CreateurFichierResultat(boolean complet, List<String> erreurs) {
        this.complet = complet;
        this.erreurs = erreurs;
    }


}

