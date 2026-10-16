import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class JeuDevineGUI extends JFrame {

    private static final int MIN = 1;
    private static final int MAX = 100;

    private final Random random = new Random();
    private int nombreSecret;
    private int nombreEssais;

    private final JLabel labelConsigne = new JLabel(
            "J'ai choisi un nombre entre " + MIN + " et " + MAX + ".", SwingConstants.CENTER);
    private final JLabel labelMessage = new JLabel("À toi de jouer !", SwingConstants.CENTER);
    private final JLabel labelEssais = new JLabel("Essais : 0");
    private final JTextField champSaisie = new JTextField(8);
    private final JButton boutonValider = new JButton("Valider");
    private final JButton boutonRejouer = new JButton("Rejouer");

    public JeuDevineGUI() {
        setTitle("Devine le nombre");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel haut = new JPanel(new GridLayout(2, 1, 5, 5));
        labelMessage.setFont(new Font("Arial", Font.BOLD, 16));
        haut.add(labelConsigne);
        haut.add(labelMessage);

        JPanel centre = new JPanel(new FlowLayout());
        centre.add(champSaisie);
        centre.add(boutonValider);

        JPanel bas = new JPanel(new BorderLayout());
        bas.add(labelEssais, BorderLayout.WEST);
        bas.add(boutonRejouer, BorderLayout.EAST);

        principal.add(haut, BorderLayout.NORTH);
        principal.add(centre, BorderLayout.CENTER);
        principal.add(bas, BorderLayout.SOUTH);
        add(principal);

        // Actions
        boutonValider.addActionListener(e -> verifierProposition());
        champSaisie.addActionListener(e -> verifierProposition()); // touche Entrée
        boutonRejouer.addActionListener(e -> nouvellePartie());

        nouvellePartie();
    }

    private void nouvellePartie() {
        nombreSecret = random.nextInt(MAX - MIN + 1) + MIN;
        nombreEssais = 0;

        labelMessage.setText("À toi de jouer !");
        labelEssais.setText("Essais : 0");
        champSaisie.setText("");
        champSaisie.setEnabled(true);
        boutonValider.setEnabled(true);
        boutonRejouer.setEnabled(false);
        champSaisie.requestFocusInWindow();
    }

    private void verifierProposition() {
        int proposition;
        try {
            proposition = Integer.parseInt(champSaisie.getText().trim());
        } catch (NumberFormatException ex) {
            labelMessage.setText("Saisis un nombre entier valide.");
            champSaisie.selectAll();
            return;
        }

        nombreEssais++;
        labelEssais.setText("Essais : " + nombreEssais);

        if (proposition < nombreSecret) {
            labelMessage.setText("C'est plus grand !");
        } else if (proposition > nombreSecret) {
            labelMessage.setText("C'est plus petit !");
        } else {
            labelMessage.setText("Bravo ! Trouvé en " + nombreEssais + " essai(s).");
            champSaisie.setEnabled(false);
            boutonValider.setEnabled(false);
            boutonRejouer.setEnabled(true);
            return;
        }

        champSaisie.setText("");
        champSaisie.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JeuDevineGUI().setVisible(true));
    }
}