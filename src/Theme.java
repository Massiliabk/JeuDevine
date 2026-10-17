import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public final class Theme {

    private Theme() {}

    public static final Color FOND          = new Color(0x0F0E26);
    public static final Color ENTETE        = new Color(0x17153A);
    public static final Color BULLE_MAITRE  = new Color(0x26244F);
    public static final Color ACCENT        = new Color(0x8B5CF6);
    public static final Color ACCENT_FONCE  = new Color(0x6D28D9);
    public static final Color ACCENT_2      = new Color(0x06B6D4);
    public static final Color SUCCES        = new Color(0x22C55E);
    public static final Color ERREUR        = new Color(0xEF4444);
    public static final Color ATTENTION     = new Color(0xF59E0B);
    public static final Color TEXTE         = new Color(0xF8FAFC);
    public static final Color TEXTE_DISCRET = new Color(0x94A3B8);
    public static final Color BORDURE       = new Color(255, 255, 255, 30);

    public static Font police(int style, float taille) {
        return new Font("Segoe UI", Font.PLAIN, 12).deriveFont(style, taille);
    }

    public static void installer() {
        FlatDarkLaf.setup();
        UIManager.put("defaultFont", police(Font.PLAIN, 14f));

        UIManager.put("Button.arc", 999);
        UIManager.put("Component.arc", 999);
        UIManager.put("TextComponent.arc", 999);

        UIManager.put("Button.default.background", ACCENT);
        UIManager.put("Button.default.hoverBackground", ACCENT.brighter());
        UIManager.put("Button.default.pressedBackground", ACCENT.darker());
        UIManager.put("Button.default.foreground", Color.WHITE);

        UIManager.put("TextField.background", new Color(0x1F1D47));
        UIManager.put("Component.borderColor", new Color(0x3B3680));
        UIManager.put("Component.focusedBorderColor", ACCENT);
        UIManager.put("Component.focusColor", ACCENT);

        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.trackArc", 999);
        UIManager.put("ScrollBar.width", 10);
        UIManager.put("ProgressBar.background", new Color(0x2A2860));
    }
}