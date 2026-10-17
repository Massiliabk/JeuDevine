import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

public final class Composants {

    private Composants() {}

    private static Graphics2D qualite(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        return g2;
    }

    
    private static void fondBulle(Graphics2D g2, int w, int h, boolean moi) {
        if (moi) {
            g2.setPaint(new GradientPaint(0, 0, Theme.ACCENT, w, h, Theme.ACCENT_FONCE));
        } else {
            g2.setColor(Theme.BULLE_MAITRE);
        }
        g2.fillRoundRect(0, 0, w, h, 22, 22);
        int c = 10;
        if (moi) g2.fillRect(w - c, h - c, c, c);
        else g2.fillRect(0, h - c, c, c);
    }

    
    public static class Avatar extends JComponent {
        private final String lettre;
        private final Color c1, c2;
        private final int taille;

        public Avatar(String lettre, Color c1, Color c2, int taille) {
            this.lettre = lettre;
            this.c1 = c1;
            this.c2 = c2;
            this.taille = taille;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(taille, taille);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = qualite(g);
            g2.setPaint(new GradientPaint(0, 0, c1, taille, taille, c2));
            g2.fillOval(0, 0, taille - 1, taille - 1);
            g2.setColor(Color.WHITE);
            g2.setFont(Theme.police(Font.BOLD, taille * 0.45f));
            FontMetrics fm = g2.getFontMetrics();
            int x = (taille - fm.stringWidth(lettre)) / 2;
            int y = (taille - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(lettre, x, y);
            g2.dispose();
        }
    }

    
    public static class Bulle extends JComponent {
        private static final int LARGEUR_MAX = 240;
        private static final int MARGE_X = 14;
        private static final int MARGE_Y = 10;
        private static final FontRenderContext FRC = new FontRenderContext(null, true, true);

        private final boolean moi;
        private final List<TextLayout> lignes = new ArrayList<>();
        private float largeurTexte;
        private float hauteurTexte;

        public Bulle(String texte, boolean moi) {
            this.moi = moi;
            setOpaque(false);

            AttributedString attribue = new AttributedString(texte);
            attribue.addAttribute(TextAttribute.FONT, Theme.police(Font.PLAIN, 15f));
            AttributedCharacterIterator it = attribue.getIterator();
            LineBreakMeasurer mesureur = new LineBreakMeasurer(it, FRC);

            while (mesureur.getPosition() < it.getEndIndex()) {
                TextLayout ligne = mesureur.nextLayout(LARGEUR_MAX);
                lignes.add(ligne);
                largeurTexte = Math.max(largeurTexte, ligne.getAdvance());
                hauteurTexte += ligne.getAscent() + ligne.getDescent() + ligne.getLeading();
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(
                    (int) Math.ceil(largeurTexte) + 2 * MARGE_X,
                    (int) Math.ceil(hauteurTexte) + 2 * MARGE_Y);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = qualite(g);
            fondBulle(g2, getWidth(), getHeight(), moi);

            g2.setColor(Theme.TEXTE);
            float y = MARGE_Y;
            for (TextLayout ligne : lignes) {
                y += ligne.getAscent();
                ligne.draw(g2, MARGE_X, y);
                y += ligne.getDescent() + ligne.getLeading();
            }
            g2.dispose();
        }
    }

    
    public static class IndicateurSaisie extends JPanel {
        private int phase;
        private final Timer timer;

        public IndicateurSaisie() {
            setOpaque(false);
            setPreferredSize(new Dimension(64, 38));
            timer = new Timer(280, e -> {
                phase = (phase + 1) % 3;
                repaint();
            });
            timer.start();
        }

        public void arreter() {
            timer.stop();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = qualite(g);
            fondBulle(g2, getWidth(), getHeight(), false);
            int d = 8, ecart = 8;
            int total = 3 * d + 2 * ecart;
            int x = (getWidth() - total) / 2;
            for (int i = 0; i < 3; i++) {
                boolean actif = (i == phase);
                g2.setColor(actif ? Theme.TEXTE : Theme.TEXTE_DISCRET);
                int y = (getHeight() - d) / 2 - (actif ? 3 : 0);
                g2.fillOval(x, y, d, d);
                x += d + ecart;
            }
            g2.dispose();
        }
    }
}