package com.absolut.view;

import com.absolut.components.CinemaButton;
import javax.swing.*;
import java.awt.*;

public class LandingPage extends JPanel {
    private Image backgroundImage;

    public LandingPage() {
        setLayout(new BorderLayout());
        setBackground(new Color(18, 18, 18));

        try {
            ImageIcon icon = new ImageIcon("resources/images/landing_bg.jpg"); 
            backgroundImage = icon.getImage();
        } catch (Exception e) {
            System.out.println("Gambar background tidak ditemukan.");
        }

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                // Lukis Gambar
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Kalo gambar ga ada, pakai warna hitam aja
                    g.setColor(new Color(18, 18, 18));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }

                g.setColor(new Color(0, 0, 0, 180));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        mainPanel.setLayout(new GridBagLayout()); // Layout untuk menengahkan konten

        // Konten Landing
        JPanel contentBox = new JPanel();
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setOpaque(false);

        // Logo
        JLabel lblBrand = new JLabel("ABSOLUTE CINEMA");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 56));
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tagline
        JLabel lblTagline = new JLabel("Your Ultimate Personal Film Library Hub");
        lblTagline.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblTagline.setForeground(new Color(200, 200, 200));
        lblTagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Spasi
        Component spacer = Box.createVerticalStrut(40);

        // Tombol Aksi
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        CinemaButton btnStart = new CinemaButton("GET STARTED");
        btnStart.setPreferredSize(new Dimension(180, 50));
        
        JButton btnLogin = new JButton("I have an account");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        buttonPanel.add(btnStart);
        buttonPanel.add(btnLogin);
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Masukkan ke ContentBox
        contentBox.add(lblBrand);
        contentBox.add(Box.createVerticalStrut(10));
        contentBox.add(lblTagline);
        contentBox.add(spacer);
        contentBox.add(buttonPanel);

        // Masukkan ContentBox ke MainPanel
        mainPanel.add(contentBox);

        // Navigasi
        btnStart.addActionListener(e -> MainFrame.getInstance().showRegister());
        btnLogin.addActionListener(e -> MainFrame.getInstance().showLogin());

        // Tambahin komponen ke panel ini
        add(mainPanel, BorderLayout.CENTER);
    }
}