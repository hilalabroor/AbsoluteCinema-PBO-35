package com.absolut.view;

import com.absolut.components.CinemaButton;
import javax.swing.*;
import java.awt.*;

public class LandingPage extends JFrame {

    // Simpan gambar di memori biar cepat load-nya
    private Image backgroundImage;

    public LandingPage() {
        // 1. Setup Frame
        setTitle("Welcome to AbsolutCinema");
        setSize(1000, 650);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- LOAD GAMBAR BACKGROUND ---
        try {
            // Pastikan nama file sesuai dengan yang ada di folder resources/images/
            ImageIcon icon = new ImageIcon("resources/images/landing_bg.jpg"); 
            backgroundImage = icon.getImage();
        } catch (Exception e) {
            System.out.println("Gambar background tidak ditemukan.");
        }

        // 2. Background Utama (CUSTOM PAINT)
        // Kita ubah JPanel biasa menjadi panel yang bisa melukis gambar
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                // A. Lukis Gambar (Stretch memenuhi layar)
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Kalau gambar gak ada, pakai warna hitam aja
                    g.setColor(new Color(18, 18, 18));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }

                // B. Lukis Lapisan Gelap Transparan (OVERLAY)
                // Ini penting biar teks tetap terbaca walau gambarnya terang
                g.setColor(new Color(0, 0, 0, 180)); // Hitam dengan transparansi (0-255)
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        mainPanel.setLayout(new GridBagLayout()); // Layout untuk menengahkan konten

        // 3. Konten Landing
        JPanel contentBox = new JPanel();
        contentBox.setLayout(new BoxLayout(contentBox, BoxLayout.Y_AXIS));
        contentBox.setOpaque(false); // PENTING: Transparan biar background kelihatan!

        // -- Logo --
        JLabel lblBrand = new JLabel("<html>ABSOLUT<span style='color:#F2C94C'>CINEMA</span></html>");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 56));
        lblBrand.setForeground(Color.WHITE);
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

        // -- Tagline --
        JLabel lblTagline = new JLabel("Your Ultimate Personal Film Library Hub");
        lblTagline.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblTagline.setForeground(new Color(200, 200, 200)); // Abu terang
        lblTagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        // -- Spasi --
        Component spacer = Box.createVerticalStrut(40);

        // -- Tombol Aksi --
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false); // Transparan juga

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
        add(mainPanel, BorderLayout.CENTER);

        // 4. Footer Sederhana
        JLabel lblFooter = new JLabel("© 2025 AbsolutCinema Project", SwingConstants.CENTER);
        lblFooter.setForeground(new Color(150, 150, 150));
        lblFooter.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Kita taruh footer di atas mainPanel (SOUTH frame)
        // Dan set background footer transparan atau hitam pekat
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(10, 10, 10)); 
        footerPanel.add(lblFooter);
        add(footerPanel, BorderLayout.SOUTH);

        // 5. Navigasi
        btnStart.addActionListener(e -> {
            new RegisterPage().setVisible(true);
            this.dispose();
        });

        btnLogin.addActionListener(e -> {
            new LoginPage().setVisible(true);
            this.dispose();
        });
    }
}