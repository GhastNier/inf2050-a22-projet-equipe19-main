package com.inf2050.equipe19;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.inf2050.equipe19.Permis.Activites;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;

import static com.inf2050.equipe19.GestionnaireJournalErreurs.JSON;
import static com.inf2050.equipe19.Main.*;
import static java.lang.System.*;
import static java.nio.file.Files.newBufferedReader;

public class CreateurJournal {

    static class ErreursActivites {
        protected static String categorieInvalide(Activites act) {
            return "La catégorie [ " + act.categorie.toUpperCase() + " ] pour l'activité [ " + act.description.toUpperCase()
                    + " ] n'est pas d'une catégorie valide. Entrée ignorée.";
        }

        protected static String mauvaisFormatHrs(Activites act) {
            return "Le format d'heure n'est pas adéquat pour l'activité [ " + act.description.toUpperCase() +
                    " ]. Entrée ignorée.";
        }

        protected static String mauvaisFormatDate(Activites act) {
            return "La date [ " + act.date + " ] pour l'activité [ " + act.description.toUpperCase()
                    + " ] n'est pas au format ISO 8601.";
        }

        protected static String estHorsCycle(Activites act) {
            return "L'activité [ " + act.description.toUpperCase() + " ] en date du [ " + act.date
                    + " ] ne fait pas partie du cycle actuel. Entrée ignorée.";
        }

        protected static String descTropCourte(Activites act){
            return "La description [ " + act.description.toUpperCase() + " ] doit avoir au moins 21 caractères.";
        }

        protected static String maxHrsParJourTropGrand(Activites act) {
            return "Un maximum de 10 heures par jour est permis par activité. Seules 10 heures seront"
                    + " considérées pour l'activité " + act.description + " du " + act.date + ".";

        }
    }

    static class ErreurPermis {
        protected static String hrsSontNegative() {
            return "Le nombre d'heures du cycle précédent est inférieur à 0. Ces heures ne seront pas prises en compte.";
        }

        protected static String hrsEnTrop(Permis permis) {
            return "Vous aviez [ " + (int) permis.heures_transferees_du_cycle_precedent + " HEURES ] reportée du cycle précédent. " +
                    "Un surplus de [ " +
                    ((int) permis.heures_transferees_du_cycle_precedent - 7) +
                    " HEURES ] au-delà des [ 7 HEURES ] transférables sera ignoré.";
        }

        protected static String hrsEnTrop() {
            return "Le format d'heure n'est pas adéquat. Entrée ignorée.";
        }

        protected static String hrsRequiseInvalideArchitecte() {
            return "Il manque [ " + (42 - heuresFormationsTotalArchitecte) + " HEURES ] de formation pour compléter le cycle.";
        }
        protected static String hrsRequiseInvalideGeologue() {
            return "Il manque [ " + (55 - heuresFormationsTotalGeologue) + " HEURES ] de formation pour compléter le cycle.";
        }
        protected static String hrsRequiseInvalidePsychologue() {
            return "Il manque [ " + (90 - heuresFormationsTotalPsychologue) + " HEURES ] de formation pour compléter le cycle.";
        }
        protected static String hrsRequiseInvalidePodiatre() {
            return "Il manque [ " + (60 - heuresFormationsTotalPodiatre) + " HEURES ] de formation pour compléter le cycle.";
        }

        protected static String minHrsNonAtteintQuatreCategories() {
            return "Il manque [ " + (17 - heuresQuatreCategories - heuresCours - heuresConference - nbHeuresCyclePrecedent)
                    + " HEURES ] de formation dans une des catégories suivantes : " +
                    Arrays.toString(listeSixCours.toArray())
                            .replace(",", " ")
                            .replace("[", "[ ")
                            .replace("]", " ]");
        }

        protected static String cycleInvalide(Permis permis) {
            return "Le cycle [ " + permis.cycle + " ] ne fait pas partie du cycle actuel.";
        }

        protected static String formatNumeroDePermis(Permis permis) {
            return "Le format du numéro de permis [ " + permis.numero_de_permis + " ] n'est pas valide.";
        }

        protected static String formatPermisPremierLettre(Permis permis) {
            return "Le format du numéro de permis [ " + permis.numero_de_permis + " ] n'est pas valide. " +
                    "La première lettre doit correspondre à la première lettre du nom, en majuscule et " +
                    "la deuxième lettre doit correspondre à la première lettre du prénom, en majuscule.";
        }

        protected static String formatNom() {
            return "Le format du nom est invalide, ceci doit être une chaîne de caractères.";
        }

        protected static String formatPrenom() {
            return "Le format du prénom est invalide, ceci doit être une chaîne de caractères.";
        }

        protected static String sexeInvalide() {
            return "Le champ sexe est invalide ceci doit être 0 pour inconnu, 1 pour masculin ou 2 pour féminin.";
        }

        protected static String hrsCoursRequiseInvalideGeologue() {
            return "Il manque [ " + (22 - heuresCours) + " HEURES ] de formation dans la catégorie Cours.";
        }
        protected static String hrsRequiseCoursInvalidePsychologue() {
            return "Il manque [ " + (25 - heuresCours) + " HEURES ] de formation dans la catégorie Cours.";
        }
        protected static String hrsCoursRequiseInvalidePodiatre() {
            return "Il manque [ " + (22 - heuresCours) + " HEURES ] de formation dans la catégorie Cours.";
        }
        protected static String hrsProjetDeRechRequiseInvalide() {
            return "Il manque [ " + (3 - heuresProjetRech) + " HEURES ] de formation dans la catégorie Projet de recherche.";
        }
    }

    static class journalErreurConsole {
        static DateTimeFormatter fdh = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SS");
        static LocalDateTime dateAujourdhui = LocalDateTime.now();
        static String dateEtHeureActuelle = fdh.format(dateAujourdhui);
        static DateTimeFormatter dateHeurePourRapport = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS");
        static String fdhPourRapport = dateHeurePourRapport.format(dateAujourdhui);
        private final ObjectMapper om = new ObjectMapper();
        private final Reader permisLecteur = newBufferedReader(Paths.get("./src/main/java/schema.json"));
        private final JsonObject jsonObj = (JsonObject) JsonParser.parseReader(permisLecteur);
        private final String gson = new Gson().toJson(jsonObj);
        private final Reader permisLecteurNouveauxOrdres = newBufferedReader(Paths.get("./src/main/java/schema_nouveaux_ordres.json"));
        private final JsonObject jsonObjNouveauxOrdres = (JsonObject) JsonParser.parseReader(permisLecteurNouveauxOrdres);
        private final String gsonNouveauxOrdres = new Gson().toJson(jsonObjNouveauxOrdres);
        private final JsonSchemaFactory generateur = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012);

        protected static void codeDeSorties(String message) throws Exception {
            String messageFinal = message.replace("$.", "").replace("_", " ");
            String s = messageFinal.substring(0, 1).toUpperCase() + (messageFinal.substring(1));
            err.append(fdh.format(dateAujourdhui)).append(" - ").append(s).append("\n");
            out.println("Le fichier d'entrée n'est pas valide pour la raison suivante:\n " + s);
            selectionDeCodeDeSortie(message);
        }

        protected static void selectionDeCodeDeSortie(String message) throws Exception {
            codeDeSortieSelonErreur(-30, message, "numero_de_permis");
            codeDeSortieSelonErreur(-31, message, "nom", "prenom", "sexe");
            codeDeSortieSelonErreur(-32, message, "ordre");
            codeDeSortieSelonErreur(-33, message, "cycle");
            codeDeSortieSelonErreur(-34, message, "activites");
        }

        private static void codeDeSortieSelonErreur(int code, String message, String... terme) throws Exception {
            for (String s : terme) {
                if (message.contains(s)) {
                    producteurErreurs(true);
                    exit(code);
                }
            }
        }

        public journalErreurConsole() throws IOException {
            String chemin = "./src/main/java/schema_error_" + fdhPourRapport + ".txt";
            FileWriter fichierSortieErreursConsole = new FileWriter(chemin);
            fichierSortieErreursConsole.close();
            setErr(new PrintStream(chemin));
        }

        public static void argumentManquant() {
            err.println(fdh.format(dateAujourdhui) + " - " + "Il manque un/des argument(s) dans votre requête." + "\n" +
            "Veuillez vous assurer que vous avez entré un fichier d'entrée et de sortie.");
        }

        public Boolean estArchitecte(JsonNode jn){
            return (jn.has("ordre".toLowerCase()) &&
                    jn.get(("ordre").toLowerCase()).asText()
                            .equals(("architectes").toLowerCase()));
        }

        public Boolean verifierEstValide() throws Exception {
            JsonReader lect = new JsonReader(newBufferedReader(cheminFichierEntre));
            lect.setLenient(true);
            JsonNode jsonNode = om.readTree(new Gson().toJson(JsonParser.parseReader(lect)));
            Set<ValidationMessage> erreurs = generateur.getSchema(gson).validate(jsonNode);
            if (!estArchitecte(jsonNode)){
                erreurs = generateur.getSchema(gsonNouveauxOrdres).validate(jsonNode);
            }
            if (erreurs.size() >= 1) {
                JSON(erreurs);
                Permis.tabErreurs.add("La structure du fichier n'est pas respectée.");
                statistique.ajusterDeclarationsTraitees();
                statistique.ajusterDeclarationsIncompletesOuInvalides();
                producteurErreurs(false);
                return false;
            }
            return true;
        }
    }
}
