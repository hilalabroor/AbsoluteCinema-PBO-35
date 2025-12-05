package com.absolut.components;

import com.absolut.model.Film;
import com.absolut.model.User;
import com.absolut.view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FilmCard extends JPanel {

    private Image posterImage;

    public FilmCard(Film film, User currentUser) {
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));
        setPreferredSize(new Dimension(200, 300)); 
        setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50), 1));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Load Gambar
        try {
            ImageIcon icon = new ImageIcon("resources/posters/" + film.getPosterPath());
            if (icon.getIconWidth() > 0) {
                posterImage = icon.getImage();
            }
        } catch (Exception e) {
            posterImage = null;
        }

        // PANEL GAMBAR
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                int panelW = getWidth();
                int panelH = getHeight();

                if (posterImage != null) {
                    int imgW = posterImage.getWidth(null);
                    int imgH = posterImage.getHeight(null);

                    double scaleX = (double) panelW / imgW;
                    double scaleY = (double) panelH / imgH;
                    double scale = Math.max(scaleX, scaleY); 

                    int newW = (int) (imgW * scale);
                    int newH = (int) (imgH * scale);
                    int x = (panelW - newW) / 2;
                    int y = (panelH - newH) / 2;

                    g2.drawImage(posterImage, x, y, newW, newH, null);
                } else {
                    g2.setColor(Color.DARK_GRAY);
                    g2.fillRect(0, 0, panelW, panelH);
                    g2.setColor(Color.WHITE);
                    g2.drawString(film.getTitle(), 10, panelH / 2);
                }
            }
        };
        add(imagePanel, BorderLayout.CENTER);

        // INFO BAWAH
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(new Color(30, 30, 30));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JLabel lblTitle = new JLabel(film.getTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        //lblTitle.setForeground(new Color(242, 201, 76)); // Kuning
        lblTitle.setForeground(Color.WHITE); // Putih

        JLabel lblRating = new JLabel("R " + film.getRating() + " | " + film.getGenre());
        lblRating.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblRating.setForeground(Color.LIGHT_GRAY);

        infoPanel.add(lblTitle);
        infoPanel.add(lblRating);
        add(infoPanel, BorderLayout.SOUTH);

        // INTERAKSI MOUSE
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Saat Hover: Border Cyan Tebal (Efek Glow)
                setBorder(BorderFactory.createLineBorder(new Color(0, 243, 255), 2));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                // Normal: Border Abu/Ungu Gelap Tipis
                setBorder(BorderFactory.createLineBorder(new Color(60, 40, 80), 1));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                // Navigasi ke Detail Film
                MainFrame.getInstance().showDetail(film, currentUser);
            }
        });
    }
}