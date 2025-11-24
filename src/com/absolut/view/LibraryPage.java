package com.absolut.view;

import com.absolut.components.FilmCard;
import com.absolut.model.Film;
import com.absolut.model.LibraryDAO;
import com.absolut.model.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LibraryPage extends JFrame {

    private User user;
    private JPanel watchlistPanel;
    private JPanel historyPanel;

    public LibraryPage(User user) {
        this.user = user;

        // 1. Setup Frame
        setTitle("My Personal Library - " + user.getUsername());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // PENTING: Pakai DISPOSE biar Dashboard gak ikutan ketutup
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(18, 18, 18));

        // 2. Header Sederhana
        JLabel lblHeader = new JLabel("MY COLLECTION", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblHeader.setForeground(new Color(242, 201, 76)); // Kuning
        lblHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblHeader, BorderLayout.NORTH);

        // 3. Tabbed Pane (Menu Tab)
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(30, 30, 30));
        tabbedPane.setForeground(Color.WHITE);

        // --- TAB 1: WATCHLIST ---
        watchlistPanel = new JPanel(new GridLayout(0, 4, 15, 15)); // Grid 4 Kolom
        watchlistPanel.setBackground(new Color(30, 30, 30));
        watchlistPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Agar bisa discroll
        JScrollPane scrollWatchlist = new JScrollPane(watchlistPanel);
        scrollWatchlist.setBorder(null);
        scrollWatchlist.getVerticalScrollBar().setUnitIncrement(16);

        // --- TAB 2: HISTORY ---
        historyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        historyPanel.setBackground(new Color(30, 30, 30));
        JLabel lblHistory = new JLabel("Riwayat tontonan akan muncul di sini.");
        lblHistory.setForeground(Color.GRAY);
        lblHistory.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        historyPanel.add(lblHistory);

        // Masukkan Tab
        tabbedPane.addTab("  ⭐ MY WATCHLIST  ", scrollWatchlist);
        tabbedPane.addTab("  🕒 HISTORY  ", historyPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // 4. Tombol Kembali (Footer)
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(18, 18, 18));
        
        JButton btnBack = new JButton("Kembali ke Dashboard");
        btnBack.setBackground(new Color(50, 50, 50));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> this.dispose()); // Tutup jendela ini
        
        footerPanel.add(btnBack);
        add(footerPanel, BorderLayout.SOUTH);

        // 5. Ambil Data dari Database
        loadWatchlistData();
    }

    private void loadWatchlistData() {
        watchlistPanel.removeAll(); // Bersihkan panel dulu

        // Panggil LibraryDAO untuk mengambil data asli dari tabel user_films
        List<Film> myFilms = LibraryDAO.getUserWatchlist(user.getId());

        if (myFilms.isEmpty()) {
            // Tampilan jika kosong
            JLabel lblEmpty = new JLabel("Belum ada film di Watchlist Anda.", SwingConstants.CENTER);
            lblEmpty.setForeground(Color.GRAY);
            lblEmpty.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            // Kita ubah layout jadi CardLayout atau FlowLayout sementara untuk pesan ini
            watchlistPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 50));
            watchlistPanel.add(lblEmpty);
        } else {
            // Reset Layout ke Grid
            watchlistPanel.setLayout(new GridLayout(0, 4, 15, 15));
            
            for (Film film : myFilms) {
                // Reuse komponen FilmCard yang sudah kita buat!
                // Kita kirim 'user' juga agar FilmCard tahu siapa yang login
                FilmCard card = new FilmCard(film, user);
                watchlistPanel.add(card);
            }
        }

        // Refresh Tampilan
        watchlistPanel.revalidate();
        watchlistPanel.repaint();
    }
}