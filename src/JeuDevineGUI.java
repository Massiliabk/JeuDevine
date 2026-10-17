import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class JeuDevineGUI extends JFrame {

    private final GameLogic jeu = new GameLogic();

    
    private final CardLayout pages = new CardLayout();
    private final JPanel racine = new JPanel(pages);
    private final PageAccueil accueil;

    
    private final JPanel conversation = new JPanel();
    private final JScrollPane defilement = new JScrollPane();
    private final JLabel labelStatut = new JLabel("● en ligne");
    private final JLabel labelEssais = new JLabel();
    private final JProgressBar progression = new JProgressBar(0, GameLogic.MAX_ESSAIS);
    private final JTextField champSaisie = new JTextField();
    private final JButton boutonEnvoyer = new JButton("Envoyer");
    private final JButton boutonRejouer = new JButton("Rejouer");
    private final JButton boutonMenu = new JButton("Accueil");
    private final CardLayout cartes = new CardLayout();
    private final JPanel barreBas = new JPanel(cartes);

    public JeuDevineGUI() {
        setTitle("Devine le nombre");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 720);
        setMinimumSize(new Dimension(420, 560));
        setLocationRelativeTo(null);

        JPanel pageJeu = new JPanel(new BorderLayout());
        pageJeu.add(construireEntete(), BorderLayout.NORTH);
        pageJeu.add(construireConversation(), BorderLayout.CENTER);
        pageJeu.add(construireBarreBas(), BorderLayout.SOUTH);

        accueil = new PageAccueil(this::demarrer, () -> System.exit(0));
        racine.add(accueil, "accueil");
        racine.add(pageJeu, "jeu");
        setContentPane(racine);

        pages.show(racine, "accueil");
        getRootPane().setDefaultButton(accueil.getBoutonJouer());

        boutonEnvoyer.addActionListener(e -> envoyer());
        champSaisie.addActionListener(e -> envoyer());
        boutonRejouer.addActionListener(e -> nouvellePartie());
        boutonMenu.addActionListener(e -> retourAccueil());
    }

    // ------------------------------------------------------------ navigation

    private void demarrer() {
        pages.show(racine, "jeu");
        nouvellePartie();
    }

    private void retourAccueil() {
        pages.show(racine, "accueil");
        getRootPane().setDefaultButton(accueil.getBoutonJouer());
    }

    // ------------------------------------------------------------ construction

    private JComponent construireEntete() {
        Composants.Avatar avatar = new Composants.Avatar("M", Theme.ACCENT, Theme.ACCENT_2, 46);

        JLabel nom = new JLabel("Le Maître du jeu");
        nom.setFont(Theme.police(Font.BOLD, 16f));
        nom.setForeground(Theme.TEXTE);
        labelStatut.setFont(Theme.police(Font.PLAIN, 12f));
        labelStatut.setForeground(Theme.SUCCES);

        JPanel textes = new JPanel(new GridLayout(2, 1));
        textes.setOpaque(false);
        textes.add(nom);
        textes.add(labelStatut);

        labelEssais.setFont(Theme.police(Font.BOLD, 13f));
        labelEssais.setForeground(Theme.TEXTE_DISCRET);

        JPanel bandeau = new JPanel(new BorderLayout(12, 0));
        bandeau.setBackground(Theme.ENTETE);
        bandeau.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        bandeau.add(avatar, BorderLayout.WEST);
        bandeau.add(textes, BorderLayout.CENTER);
        bandeau.add(labelEssais, BorderLayout.EAST);

        progression.setBorderPainted(false);
        progression.setPreferredSize(new Dimension(10, 4));

        JPanel haut = new JPanel(new BorderLayout());
        haut.add(bandeau, BorderLayout.CENTER);
        haut.add(progression, BorderLayout.SOUTH);
        return haut;
    }

    private JComponent construireConversation() {
        conversation.setLayout(new BoxLayout(conversation, BoxLayout.Y_AXIS));
        conversation.setOpaque(false);
        conversation.setBorder(BorderFactory.createEmptyBorder(14, 12, 14, 12));

        JPanel support = new JPanel(new BorderLayout());
        support.setBackground(Theme.FOND);
        support.add(conversation, BorderLayout.NORTH);

        defilement.setViewportView(support);
        defilement.setBorder(null);
        defilement.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        defilement.getViewport().setBackground(Theme.FOND);
        defilement.getVerticalScrollBar().setUnitIncrement(16);
        return defilement;
    }

    private JComponent construireBarreBas() {
        champSaisie.setFont(Theme.police(Font.PLAIN, 16f));
        champSaisie.putClientProperty("JTextField.placeholderText", "Écris un nombre…");
        champSaisie.putClientProperty("JComponent.minimumHeight", 44);
        boutonEnvoyer.putClientProperty("JComponent.minimumHeight", 44);
        boutonEnvoyer.putClientProperty("JComponent.minimumWidth", 100);
        boutonEnvoyer.setFont(Theme.police(Font.BOLD, 14f));

        JPanel saisie = new JPanel(new BorderLayout(10, 0));
        saisie.setOpaque(false);
        saisie.add(champSaisie, BorderLayout.CENTER);
        saisie.add(boutonEnvoyer, BorderLayout.EAST);

        boutonRejouer.putClientProperty("JComponent.minimumHeight", 44);
        boutonRejouer.putClientProperty("JComponent.minimumWidth", 160);
        boutonRejouer.setFont(Theme.police(Font.BOLD, 14f));
        boutonMenu.putClientProperty("JComponent.minimumHeight", 44);
        boutonMenu.putClientProperty("JComponent.minimumWidth", 120);
        boutonMenu.setFont(Theme.police(Font.PLAIN, 14f));

        JPanel fin = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        fin.setOpaque(false);
        fin.add(boutonRejouer);
        fin.add(boutonMenu);

        barreBas.setBackground(Theme.ENTETE);
        barreBas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDURE),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)));
        barreBas.add(saisie, "saisie");
        barreBas.add(fin, "fin");
        return barreBas;
    }

    // ------------------------------------------------------------ déroulement

    private void nouvellePartie() {
        jeu.nouvellePartie();
        conversation.removeAll();
        conversation.revalidate();
        conversation.repaint();
        majEssais();
        cartes.show(barreBas, "saisie");
        getRootPane().setDefaultButton(boutonEnvoyer);

        maitreDit(() -> activerSaisie(true),
                Dialogue.accueil(GameLogic.MIN, GameLogic.MAX, GameLogic.MAX_ESSAIS));
    }

    private void envoyer() {
        String texte = champSaisie.getText().trim();
        if (texte.isEmpty()) return;

        activerSaisie(false);
        champSaisie.setText("");
        ajouterBulle(texte, true);

        int proposition;
        try {
            proposition = Integer.parseInt(texte);
        } catch (NumberFormatException ex) {
            maitreDit(() -> activerSaisie(true), Dialogue.nonNombre());
            return;
        }
        if (proposition < GameLogic.MIN || proposition > GameLogic.MAX) {
            maitreDit(() -> activerSaisie(true), Dialogue.horsLimites(GameLogic.MIN, GameLogic.MAX));
            return;
        }

        GameLogic.Resultat resultat = jeu.proposer(proposition);
        majEssais();

        if (resultat == GameLogic.Resultat.GAGNE) {
            maitreDit(this::finDePartie, Dialogue.victoire(jeu.getSecret(), jeu.getEssais()));
        } else if (jeu.estTerminee()) {
            maitreDit(this::finDePartie, Dialogue.defaite(jeu.getSecret()));
        } else {
            int distance = Math.abs(proposition - jeu.getSecret());
            int restants = GameLogic.MAX_ESSAIS - jeu.getEssais();
            maitreDit(() -> activerSaisie(true),
                    Dialogue.indice(resultat == GameLogic.Resultat.PLUS_GRAND, distance, restants));
        }
    }

    private void finDePartie() {
        cartes.show(barreBas, "fin");
        getRootPane().setDefaultButton(boutonRejouer);
        boutonRejouer.requestFocusInWindow();
    }

    private void activerSaisie(boolean actif) {
        champSaisie.setEnabled(actif);
        boutonEnvoyer.setEnabled(actif);
        if (actif) champSaisie.requestFocusInWindow();
    }

    private void majEssais() {
        int e = jeu.getEssais();
        labelEssais.setText("Essais " + e + " / " + GameLogic.MAX_ESSAIS);
        progression.setValue(e);
        progression.setForeground(e >= 9 ? Theme.ERREUR : e >= 7 ? Theme.ATTENTION : Theme.ACCENT);
    }

    // ------------------------------------------------------------ conversation

    /** Le Maître écrit ses messages un par un, avec l'animation « écrit… ». */
    private void maitreDit(Runnable apres, String... messages) {
        activerSaisie(false);
        labelStatut.setText("écrit…");
        jouerSuivant(new ArrayDeque<>(List.of(messages)), apres);
    }

    private void jouerSuivant(Deque<String> file, Runnable apres) {
        if (file.isEmpty()) {
            labelStatut.setText("● en ligne");
            if (apres != null) apres.run();
            return;
        }
        String message = file.poll();
        Composants.IndicateurSaisie indicateur = new Composants.IndicateurSaisie();
        JPanel ligne = ajouterLigne(indicateur, false);

        int delai = Math.min(450 + message.length() * 22, 1400);
        Timer timer = new Timer(delai, e -> {
            indicateur.arreter();
            conversation.remove(ligne);
            ajouterBulle(message, false);
            jouerSuivant(file, apres);
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void ajouterBulle(String texte, boolean moi) {
        ajouterLigne(new Composants.Bulle(texte, moi), moi);
    }

    private JPanel ajouterLigne(JComponent contenu, boolean moi) {
        JPanel ligne = new JPanel(new FlowLayout(moi ? FlowLayout.RIGHT : FlowLayout.LEFT, 8, 4));
        ligne.setOpaque(false);

        Composants.Avatar avatar = moi
                ? new Composants.Avatar("T", Theme.ACCENT_2, Theme.ACCENT, 34)
                : new Composants.Avatar("M", Theme.ACCENT, Theme.ACCENT_2, 34);
        if (moi) {
            ligne.add(contenu);
            ligne.add(avatar);
        } else {
            ligne.add(avatar);
            ligne.add(contenu);
        }

        ligne.setAlignmentX(Component.LEFT_ALIGNMENT);
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, ligne.getPreferredSize().height));
        conversation.add(ligne);
        conversation.revalidate();
        conversation.repaint();
        defilerEnBas();
        return ligne;
    }

    private void defilerEnBas() {
        SwingUtilities.invokeLater(() -> {
            JScrollBar barre = defilement.getVerticalScrollBar();
            barre.setValue(barre.getMaximum());
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Theme.installer();
            new JeuDevineGUI().setVisible(true);
        });
    }
}