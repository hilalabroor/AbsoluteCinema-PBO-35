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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(18, 18, 18));

        // 2. Header Sederhana
        JLabel lblHeader = new JLabel("MY COLLECTION", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblHeader.setForeground(new Color(242, 201, 76));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblHeader, BorderLayout.NORTH);

        // 3. Tabbed Pane (Menu Tab)
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(new Color(30, 30, 30));
        tabbedPane.setForeground(Color.WHITE);

        // --- TAB 1: WATCHLIST ---
        watchlistPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        watchlistPanel.setBackground(new Color(30, 30, 30));
        watchlistPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane scrollWatchlist = new JScrollPane(watchlistPanel);
        scrollWatchlist.setBorder(null);
        scrollWatchlist.getVerticalScrollBar().setUnitIncrement(16);

        tabbedPane.addTab("  ⭐ MY WATCHLIST  ", scrollWatchlist);

        // --- TAB 2: HISTORY ---
        historyPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        historyPanel.setBackground(new Color(30, 30, 30));
        historyPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane scrollHistory = new JScrollPane(historyPanel);
        scrollHistory.setBorder(null);
        scrollHistory.getVerticalScrollBar().setUnitIncrement(16);

        tabbedPane.addTab("  🕒 HISTORY  ", scrollHistory);

        add(tabbedPane, BorderLayout.CENTER);

        // 4. Tombol Kembali (Footer)
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(18, 18, 18));

        JButton btnBack = new JButton("Kembali ke Dashboard");
        btnBack.setBackground(new Color(50, 50, 50));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.addActionListener(e -> this.dispose());

        footerPanel.add(btnBack);
        add(footerPanel, BorderLayout.SOUTH);

        // 5. Ambil Data dari Database
        loadWatchlistData();
        loadHistoryData();
    }

    private void loadWatchlistData() {
        watchlistPanel.removeAll();

        List<Film> myFilms = LibraryDAO.getUserWatchlist(user.getId());

        if (myFilms.isEmpty()) {
            // Gunakan GridBagLayout agar placeholder selalu di tengah
            watchlistPanel.setLayout(new GridBagLayout());
            JLabel lblEmpty = new JLabel("Belum ada film di Watchlist Anda.", SwingConstants.CENTER);
            lblEmpty.setForeground(Color.GRAY);
            lblEmpty.setFont(new Font("Segoe UI", Font.PLAIN, 18));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.anchor = GridBagConstraints.CENTER;

            watchlistPanel.add(lblEmpty, gbc);
        } else {
            watchlistPanel.setLayout(new GridLayout(0, 4, 15, 15));
            for (Film film : myFilms) {
                FilmCard card = new FilmCard(film, user);
                watchlistPanel.add(card);
            }
        }

        watchlistPanel.revalidate();
        watchlistPanel.repaint();
    }

    private void loadHistoryData() {
        historyPanel.removeAll();

        List<Film> watchedFilms = LibraryDAO.getUserHistory(user.getId());

        if (watchedFilms.isEmpty()) {
            historyPanel.setLayout(new GridBagLayout());
            JLabel lblEmpty = new JLabel("Belum ada riwayat tontonan.", SwingConstants.CENTER);
            lblEmpty.setForeground(Color.GRAY);
            lblEmpty.setFont(new Font("Segoe UI", Font.PLAIN, 18));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.anchor = GridBagConstraints.CENTER;

            historyPanel.add(lblEmpty, gbc);
        } else {
            historyPanel.setLayout(new GridLayout(0, 4, 15, 15));
            for (Film film : watchedFilms) {
                FilmCard card = new FilmCard(film, user);
                historyPanel.add(card);
            }
        }

        historyPanel.revalidate();
        historyPanel.repaint();
    }

    public void reloadWatchlist() {
        loadWatchlistData();
    }

    public void reloadHistory() {
        loadHistoryData();
    }
}