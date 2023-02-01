package com.inf2050.equipe19;

import com.inf2050.equipe19.Permis.Activites;
import com.networknt.schema.ValidationMessage;

import java.util.Set;

import static com.inf2050.equipe19.CreateurJournal.ErreursActivites.*;

public class GestionnaireJournalErreurs extends CreateurJournal {
    public static String activites(Activites act, String s) {
        return switch (s) {
            case "ActCategorie" -> categorieInvalide(act);
            case "ActFormatHre" -> mauvaisFormatHrs(act);
            case "ActFormatDate" -> mauvaisFormatDate(act);
            case "ActHorsCycle" -> estHorsCycle(act);
            case "DescVingtCar" -> descTropCourte(act);
            case "maxHrsParJour" -> maxHrsParJourTropGrand(act);
            default -> "";
        };
    }
    public static String permis(Permis permis, String s) {
        return PermisMessage.valueOf(s).affichagePermis(permis);
    }

    public static void JSON(Set<ValidationMessage> erreurs) throws Exception {
        for (ValidationMessage e : erreurs) {
            if (e.getType().equals("required")) journalErreurConsole.codeDeSorties(e.getMessage());
            if (e.getType().equals("minItems")) journalErreurConsole.codeDeSorties(e.getMessage());
            else journalErreurConsole.codeDeSorties(e.getMessage());
        }
    }
}
