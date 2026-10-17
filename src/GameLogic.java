import java.util.Random;

public class GameLogic {

    public static final int MIN = 1;
    public static final int MAX = 100;
    public static final int MAX_ESSAIS = 10;

    public enum Resultat { PLUS_GRAND, PLUS_PETIT, GAGNE }

    private final Random random = new Random();
    private int secret;
    private int essais;
    private boolean gagnee;

    public GameLogic() {
        nouvellePartie();
    }

    public void nouvellePartie() {
        secret = random.nextInt(MAX - MIN + 1) + MIN;
        essais = 0;
        gagnee = false;
    }

    public Resultat proposer(int proposition) {
        essais++;
        if (proposition == secret) {
            gagnee = true;
            return Resultat.GAGNE;
        }
        return proposition < secret ? Resultat.PLUS_GRAND : Resultat.PLUS_PETIT;
    }

    public boolean estTerminee() { return gagnee || essais >= MAX_ESSAIS; }
    public int getSecret()       { return secret; }
    public int getEssais()       { return essais; }
}