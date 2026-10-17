import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class PageAccueil extends JPanel {

    private static final Color FOND_HAUT = new Color(0x0B0B1E);
    private static final Color FOND_BAS = new Color(0x1E1B4B);

   
    private static class Chiffre {
        float x, y, vitesse;
        String texte;
        int taille, opacite;
    }

    private final JButton boutonJouer = new JButton("Jouer");
    private final JButton boutonQuitter = new JButton("Quitter");
    private final List<Chiffre> chiffres = new ArrayList<>();
    private final Random random = new Random();
    private final Timer animation;
    private float pulse;

    public PageAccueil(Runnable surJouer, Runnable surQuitter) {
        setLayout(new GridBagLayout());

        JPanel contenu = new JPanel();
        contenu.setOpaque(false);
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));

        
        boutonJouer.setFont(Theme.police(Font.BOLD, 18f));
        boutonJouer.setPreferredSize(new Dimension(260, 54));
        boutonQuitter.setFont(Theme.police(Font.PLAIN, 15f));
        boutonQuitter.setPreferredSize(new Dimension(260, 44));
        boutonJouer.addActionListener(e -> surJouer.run());
        boutonQuitter.addActionListener(e -> surQuitter.run());

        
        JPanel infos = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        infos.setOpaque(false);
        infos.add(new InfoCarte(GameLogic.MIN + "–" + GameLogic.MAX, "Intervalle"));
        infos.add(new InfoCarte(String.valueOf(GameLogic.MAX_ESSAIS), "Essais"));
        infos.add(new InfoCarte("↑ ↓", "Indices"));

        contenu.add(centrer(new HaloAvatar()));
        contenu.add(Box.createVerticalStrut(4));
        contenu.add(centrer(new TitreDegrade("DEVINE", "LE NOMBRE")));
        contenu.add(Box.createVerticalStrut(16));
        contenu.add(centrer(new Composants.Bulle("Salut ! Prêt à relever le défi ? ", false)));
        contenu.add(Box.createVerticalStrut(30));
        contenu.add(centrer(infos));
        contenu.add(Box.createVerticalStrut(34));
        contenu.add(centrer(boutonJouer));
        contenu.add(Box.createVerticalStrut(12));
        contenu.add(centrer(boutonQuitter));
        add(contenu);

        
        animation = new Timer(33, e -> {
            pulse += 0.06f;
            for (Chiffre c : chiffres) {
                c.y -= c.vitesse;
                if (c.y < -50) replacer(c, false);
            }
            repaint();
        });
        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                if (isShowing()) animation.start();
                else animation.stop();
            }
        });
    }

    public JButton getBoutonJouer() {
        return boutonJouer;
    }

    

    private static <T extends JComponent> T centrer(T c) {
        c.setAlignmentX(Component.CENTER_ALIGNMENT);
        c.setMaximumSize(c.getPreferredSize());
        return c;
    }

    private static Graphics2D qualite(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        return g2;
    }

    private static Color alpha(Color c, int a) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), a);
    }

    private void replacer(Chiffre c, boolean nimporteOu) {
        int w = Math.max(getWidth(), 1);
        int h = Math.max(getHeight(), 1);
        c.x = random.nextInt(w);
        c.y = nimporteOu ? random.nextInt(h) : h + 40;
        c.vitesse = 0.3f + random.nextFloat() * 0.9f;
        c.texte = String.valueOf(GameLogic.MIN + random.nextInt(GameLogic.MAX));
        c.taille = 16 + random.nextInt(28);
        c.opacite = 14 + random.nextInt(30);
    }

    private void halo(Graphics2D g2, float cx, float cy, float rayon, Color c, int a) {
        g2.setPaint(new RadialGradientPaint(new Point2D.Float(cx, cy), rayon,
                new float[]{0f, 1f}, new Color[]{alpha(c, a), alpha(c, 0)}));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }

    // ------------------------------------------------------------ fond

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = qualite(g);
        int w = getWidth(), h = getHeight();

        g2.setPaint(new GradientPaint(0, 0, FOND_HAUT, w, h, FOND_BAS));
        g2.fillRect(0, 0, w, h);
        halo(g2, w * 0.2f, h * 0.1f, w * 0.8f, Theme.ACCENT, 90);
        halo(g2, w * 0.9f, h * 0.9f, w * 0.8f, Theme.ACCENT_2, 60);

        if (chiffres.isEmpty() && w > 0) {
            for (int i = 0; i < 24; i++) {
                Chiffre c = new Chiffre();
                replacer(c, true);
                chiffres.add(c);
            }
        }
        for (Chiffre c : chiffres) {
            g2.setFont(Theme.police(Font.BOLD, c.taille));
            g2.setColor(alpha(Color.WHITE, c.opacite));
            g2.drawString(c.texte, c.x, c.y);
        }
        g2.dispose();
    }

    

    
    private class HaloAvatar extends JComponent {
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(190, 190);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = qualite(g);
            float cx = getWidth() / 2f, cy = getHeight() / 2f;

            float rayon = 80 + (float) Math.sin(pulse) * 8;
            g2.setPaint(new RadialGradientPaint(new Point2D.Float(cx, cy), rayon,
                    new float[]{0.35f, 1f},
                    new Color[]{alpha(Theme.ACCENT, 150), alpha(Theme.ACCENT, 0)}));
            g2.fillRect(0, 0, getWidth(), getHeight());

            int d = 108;
            int x = (int) (cx - d / 2f), y = (int) (cy - d / 2f);
            g2.setPaint(new GradientPaint(x, y, Theme.ACCENT, x + d, y + d, Theme.ACCENT_2));
            g2.fillOval(x, y, d, d);

            g2.setColor(Color.WHITE);
            g2.setFont(Theme.police(Font.BOLD, 50f));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString("M", cx - fm.stringWidth("M") / 2f, cy - fm.getHeight() / 2f + fm.getAscent());
            g2.dispose();
        }
    }

   
    private static class TitreDegrade extends JComponent {
        private final String[] lignes;
        private final Font police = Theme.police(Font.BOLD, 44f);

        TitreDegrade(String... lignes) {
            this.lignes = lignes;
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics fm = getFontMetrics(police);
            int largeur = 0;
            for (String l : lignes) largeur = Math.max(largeur, fm.stringWidth(l));
            return new Dimension(largeur + 20, fm.getHeight() * lignes.length);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = qualite(g);
            g2.setFont(police);
            FontMetrics fm = g2.getFontMetrics();
            int y = fm.getAscent();
            for (int i = 0; i < lignes.length; i++) {
                int largeur = fm.stringWidth(lignes[i]);
                int x = (getWidth() - largeur) / 2;
                if (i == 0) {
                    g2.setColor(Theme.TEXTE);
                } else {
                    g2.setPaint(new GradientPaint(x, 0, Theme.ACCENT, x + largeur, 0, Theme.ACCENT_2));
                }
                g2.drawString(lignes[i], x, y);
                y += fm.getHeight();
            }
            g2.dispose();
        }
    }

   
    private static class InfoCarte extends JComponent {
        private final String valeur, libelle;

        InfoCarte(String valeur, String libelle) {
            this.valeur = valeur;
            this.libelle = libelle;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(118, 78);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = qualite(g);
            int w = getWidth(), h = getHeight();
            g2.setColor(new Color(255, 255, 255, 16));
            g2.fillRoundRect(0, 0, w - 1, h - 1, 22, 22);
            g2.setColor(Theme.BORDURE);
            g2.drawRoundRect(0, 0, w - 1, h - 1, 22, 22);

            g2.setFont(Theme.police(Font.BOLD, 22f));
            g2.setColor(Theme.TEXTE);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(valeur, (w - fm.stringWidth(valeur)) / 2, 38);

            g2.setFont(Theme.police(Font.PLAIN, 12f));
            g2.setColor(Theme.TEXTE_DISCRET);
            fm = g2.getFontMetrics();
            g2.drawString(libelle, (w - fm.stringWidth(libelle)) / 2, 60);
            g2.dispose();
        }
    }
}