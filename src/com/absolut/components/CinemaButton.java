package com.absolut.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CinemaButton extends JButton {
    
    private Color normalColor = new Color(242, 201, 76); // Kuning Emas
    private Color hoverColor = new Color(255, 220, 100); // Kuning Terang
    private Color pressedColor = new Color(200, 160, 50); // Kuning Gelap

    public CinemaButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.BLACK); // Teks Hitam
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Animasi Hover sederhana
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColor);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(pressedColor);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                setBackground(hoverColor);
            }
        });
        
        setBackground(normalColor); // Warna awal
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Gambar tombol melengkung (Radius 30)
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        // Gambar Teks di tengah
        FontMetrics fm = g2.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(getText())) / 2;
        int textY = (getHeight() + fm.getAscent()) / 2 - 4;
        
        g2.setColor(getForeground());
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }
}