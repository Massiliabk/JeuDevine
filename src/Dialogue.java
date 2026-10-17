import java.util.Random;

public final class Dialogue {

    private static final Random R = new Random();

    private Dialogue() {}

    private static String choisir(String... options) {
        return options[R.nextInt(options.length)];
    }

    public static String[] accueil(int min, int max, int essais) {
        return new String[]{
                "Salut ! 👋",
                "J'ai pensé à un nombre entre " + min + " et " + max + ".",
                "Tu as " + essais + " essais pour le deviner. À toi de jouer !"
        };
    }

    public static String[] indice(boolean plusGrand, int distance, int restants) {
        String base = plusGrand
                ? choisir("C'est plus grand ↑", "Vise plus haut ↑", "Non, c'est plus grand ↑", "Trop bas, monte un peu ↑")
                : choisir("C'est plus petit ↓", "Vise plus bas ↓", "Non, c'est plus petit ↓", "Trop haut, descends un peu ↓");

        String chaleur = "";
        if (distance <= 3) chaleur = " 🔥 Tu brûles !";
        else if (distance <= 10) chaleur = " Tu te rapproches…";
        else if (distance >= 40) chaleur = " Tu es encore très loin.";

        if (restants == 1) {
            return new String[]{base + chaleur, "Attention, c'est ton dernier essai !"};
        }
        return new String[]{base + chaleur};
    }

    public static String[] victoire(int secret, int essais) {
        String duree = "Trouvé en " + essais + " essai" + (essais > 1 ? "s" : "")
                + (essais <= 4 ? " — impressionnant !" : ".");
        return new String[]{
                essais == 1 ? "Du premier coup ?! Incroyable ! 😱" : "Bravo ! 🎉 C'était bien " + secret + ".",
                duree,
                "On refait une partie ?"
        };
    }

    public static String[] defaite(int secret) {
        return new String[]{
                "Aïe, plus d'essais… 😅",
                "Le nombre était " + secret + ".",
                "Tu veux ta revanche ?"
        };
    }

    public static String[] nonNombre() {
        return new String[]{choisir(
                "Hmm, ça ne ressemble pas à un nombre entier 🤔",
                "Je ne comprends pas… donne-moi un nombre entier.")};
    }

    public static String[] horsLimites(int min, int max) {
        return new String[]{"Ouh là ! Je t'ai dit entre " + min + " et " + max + " 😄 (ça ne compte pas comme un essai)"};
    }
}