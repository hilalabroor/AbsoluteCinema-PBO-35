package com.absolut.components;

import com.absolut.model.Film;
import com.absolut.view.DetailPage; // Nanti kita buat ini
import com.absolut.model.User;     // Butuh info user untuk watchlist

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class FilmCard extends JPanel {

    private Film filmData;

    // Constructor sekarang menerima objek Film dan User
    public FilmCard(Film film, User currentUser) {
        this.filmData = film;

        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30));
        setPreferredSize(new Dimension(180, 280));
        setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50), 1));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- GAMBAR ---
        JLabel lblImage = new JLabel();
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setBackground(Color.BLACK);
        lblImage.setOpaque(true);
        
        try {
            ImageIcon icon = new ImageIcon("resources/posters/" + film.getPosterPath());
            Image img = icon.getImage().getScaledInstance(180, 230, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblImage.setText(film.getTitle()); // Fallback text
            lblImage.setForeground(Color.WHITE);
        }
        add(lblImage, BorderLayout.CENTER);

        // --- INFO BAWAH ---
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(new Color(30, 30, 30));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel lblTitle = new JLabel(film.getTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(new Color(242, 201, 76)); // Kuning

        JLabel lblRating = new JLabel("⭐ " + film.getRating() + " | " + film.getGenre());
        lblRating.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblRating.setForeground(Color.LIGHT_GRAY);

        infoPanel.add(lblTitle);
        infoPanel.add(lblRating);
        add(infoPanel, BorderLayout.SOUTH);

        // --- INTERAKSI MOUSE ---
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(new Color(242, 201, 76), 2));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50), 1));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                // Buka Detail Page saat diklik
                new DetailPage(film, currentUser).setVisible(true);
            }
        });
    }
}