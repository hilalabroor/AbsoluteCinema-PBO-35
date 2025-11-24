package com.absolut.view;

import com.absolut.components.CinemaButton;
import com.absolut.model.Film;
import com.absolut.model.User;

import javax.swing.*;
import javax.sound.sampled.*; // Library untuk Suara
import java.awt.*;
import java.io.File;

public class DetailPage extends JFrame {

    private Film film;
    private User user;
    
    // Komponen yang perlu diakses global di class ini
    private JLabel lblPoster;
    private JProgressBar progressBar;
    private CinemaButton btnPlay;
    private Clip audioClip; // Pemutar kaset (Clip)
    private boolean isPreviewRunning = false; // Penanda status

    public DetailPage(Film film, User user) {
        this.film = film;
        this.user = user;

        setTitle(film.getTitle() + " - Detail");
        setSize(800, 600); // Sedikit dipertinggi buat progress bar
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Saat jendela ditutup, pastikan suara mati!
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                stopAudio();
            }
        });

        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(18, 18, 18));

        // --- PANEL KIRI (POSTER) ---
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(18, 18, 18));
        leftPanel.setPreferredSize(new Dimension(300, 500));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        lblPoster = new JLabel();
        lblPoster.setHorizontalAlignment(SwingConstants.CENTER);
        lblPoster.setBorder(BorderFactory.createLineBorder(new Color(50, 50, 50), 2));
        loadStaticPoster(); // Method helper (lihat bawah)
        
        leftPanel.add(lblPoster);
        add(leftPanel, BorderLayout.WEST);

        // --- PANEL KANAN ---
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(new Color(18, 18, 18));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 30));

        // Komponen Info
        JLabel lblTitle = new JLabel(film.getTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(new Color(242, 201, 76));

        JLabel lblMeta = new JLabel("Genre: " + film.getGenre() + "  |  Rating: ⭐ " + film.getRating());
        lblMeta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMeta.setForeground(Color.LIGHT_GRAY);

        JTextArea txtSynopsis = new JTextArea(film.getSynopsis());
        txtSynopsis.setWrapStyleWord(true);
        txtSynopsis.setLineWrap(true);
        txtSynopsis.setEditable(false);
        txtSynopsis.setFocusable(false);
        txtSynopsis.setBackground(new Color(18, 18, 18));
        txtSynopsis.setForeground(Color.WHITE);
        txtSynopsis.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSynopsis.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // --- AREA BUTTON & PROGRESS BAR ---
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(new Color(18, 18, 18));

        // 1. Progress Bar (Awalnya sembunyi/kosong)
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(300, 10));
        progressBar.setForeground(new Color(242, 201, 76)); // Warna Kuning
        progressBar.setBackground(new Color(50, 50, 50));
        progressBar.setBorderPainted(false);
        progressBar.setValue(0);
        progressBar.setVisible(false); // Sembunyi dulu

        // 2. Panel Tombol
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        btnPanel.setBackground(new Color(18, 18, 18));

        btnPlay = new CinemaButton("▶ PLAY PREVIEW");
        btnPlay.setPreferredSize(new Dimension(160, 40));
        
        JButton btnWatchlist = new JButton("+ Watchlist");
        btnWatchlist.setBackground(new Color(50, 50, 50));
        btnWatchlist.setForeground(Color.WHITE);
        btnWatchlist.setPreferredSize(new Dimension(160, 40));
        btnWatchlist.setFocusPainted(false);

        btnPanel.add(btnPlay);
        btnPanel.add(Box.createHorizontalStrut(15));
        btnPanel.add(btnWatchlist);

        actionPanel.add(progressBar); // Progress bar di atas tombol
        actionPanel.add(btnPanel);

        // Masukkan ke Panel Kanan
        rightPanel.add(lblTitle);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(lblMeta);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(txtSynopsis);
        rightPanel.add(Box.createVerticalGlue());
        rightPanel.add(actionPanel);

        add(rightPanel, BorderLayout.CENTER);

        // --- EVENTS ---
        
        // Event Klik Play (MULTITHREADING TRIGGER)
        btnPlay.addActionListener(e -> {
            if (!isPreviewRunning) {
                startPreviewThread();
            }
        });

        btnWatchlist.addActionListener(e -> {
            if (user.canAddToWatchlist(0)) {
                JOptionPane.showMessageDialog(this, "Film berhasil ditambahkan!");
            } else {
                JOptionPane.showMessageDialog(this, "Limit tercapai! Upgrade Premium.", "Gagal", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    // ========================================================
    // 🎬 BAGIAN UTAMA: MULTITHREADING LOGIC
    // ========================================================

    private void startPreviewThread() {
        // 1. Buat Thread Baru (Anonymous Class)
        Thread previewThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // A. Persiapan UI (Running di Thread terpisah)
                    isPreviewRunning = true;
                    SwingUtilities.invokeLater(() -> {
                        btnPlay.setEnabled(false);
                        btnPlay.setText("PLAYING...");
                        progressBar.setVisible(true);
                        progressBar.setValue(0);
                    });

                    // B. Ganti Gambar ke GIF
                    loadGifPreview();

                    // C. Putar Audio (.wav)
                    playAudio(film.getAudioPath());

                    // D. Jalankan Timer Progress Bar (Durasi 10 Detik)
                    // Loop 100 kali, setiap kali tidur 100ms = Total 10000ms (10 detik)
                    for (int i = 0; i <= 100; i++) {
                        final int progress = i;
                        
                        // Update UI harus via invokeLater agar aman
                        SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
                        
                        Thread.sleep(100); // Thread 'Tidur' 0.1 detik
                    }

                } catch (InterruptedException ex) {
                    System.out.println("Preview Interrupted");
                } finally {
                    // E. Selesai (Reset Semuanya)
                    stopAudio();
                    isPreviewRunning = false;
                    
                    SwingUtilities.invokeLater(() -> {
                        loadStaticPoster(); // Balikin gambar poster
                        btnPlay.setText("▶ PLAY PREVIEW");
                        btnPlay.setEnabled(true);
                        progressBar.setVisible(false);
                    });
                }
            }
        });

        // 2. Jalankan Thread
        previewThread.start(); 
    }

    // --- Helper: Load Gambar Statis ---
    private void loadStaticPoster() {
        try {
            ImageIcon icon = new ImageIcon("../../assets/posters/" + film.getPosterPath());
            Image img = icon.getImage().getScaledInstance(260, 380, Image.SCALE_SMOOTH);
            lblPoster.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblPoster.setText("NO IMAGE");
        }
    }

    // --- Helper: Load GIF Animasi ---
    private void loadGifPreview() {
        try {
            // GIF tidak perlu di-scale smooth, langsung saja biar gerak
            ImageIcon icon = new ImageIcon("resources/gifs/" + film.getTrailerGifPath());
            // Scaling GIF agak tricky di Swing, kita pakai simple scaling
            Image img = icon.getImage().getScaledInstance(260, 380, Image.SCALE_DEFAULT); 
            lblPoster.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            System.out.println("GIF tidak ditemukan: " + e.getMessage());
        }
    }

    // --- Helper: Play Audio ---
    private void playAudio(String filename) {
        try {
            File soundFile = new File("resources/audio/" + filename);
            if (soundFile.exists()) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
                audioClip = AudioSystem.getClip();
                audioClip.open(audioIn);
                audioClip.start();
            } else {
                System.out.println("File audio tidak ada: " + filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Helper: Stop Audio ---
    private void stopAudio() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
            audioClip.close();
        }
    }
}