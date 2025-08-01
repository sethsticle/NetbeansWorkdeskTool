package UI;

import java.awt.*;
import javax.swing.*;

public class RoundedButton extends JButton {
    public RoundedButton(String text) {
        super(text);
        setOpaque(false); // Make background transparent for custom painting
        setContentAreaFilled(false); // Remove default background fill
        setFocusPainted(false); // Remove focus outline
        setBorderPainted(false); // Remove default border
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Button background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // Rounded corners
        
        super.paintComponent(g);
        g2.dispose();
    }
}
