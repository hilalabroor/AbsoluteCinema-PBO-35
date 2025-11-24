package com.absolut.view;

import com.absolut.components.FilmCard;
import com.absolut.database.DBConnection;
import com.absolut.model.Film;
import com.absolut.model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DashboardPage extends JFrame {

    private User currentUser;
    private JPanel gridPanel; // Tempat menaruh kartu film
    private JComboBox<String> cbGenre; // Dropdown Filter Genre
    private JComboBox<String> cbSort;  // Dropdown Sorting

    public DashboardPage(User user) {
        this.currentUser = user;

        // 1. Setup Frame Utama
        setTitle("AbsolutCinema - Dashboard");
        setSize(1200, 800); // Ukuran agak besar biar lega
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // =========================================
        // BAGIAN HEADER (ATAS)
        // =========================================
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(18, 18, 18));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        // Logo Kiri
        JLabel lblBrand = new JLabel("<html>ABSOLUT<span style='color:#F2C94C'>CINEMA</span></html>");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblBrand.setForeground(Color.WHITE);

        // Info User Kanan
        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightHeader.setOpaque(false);

        JLabel lblUser = new JLabel("Halo, " + user.getUsername() + " [" + user.getUserType() + "]");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Tombol ke Halaman Library
        JButton btnLibrary = new JButton("My Library");
        styleHeaderButton(btnLibrary, new Color(242, 201, 76), Color.BLACK);
        btnLibrary.addActionListener(e -> {
            new LibraryPage(currentUser).setVisible(true);
        });

        // Tombol Logout
        JButton btnLogout = new JButton("Logout");
        styleHeaderButton(btnLogout, new Color(200, 50, 50), Color.WHITE);
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginPage().setVisible(true);
                this.dispose();
            }
        });

        rightHeader.add(lblUser);
        rightHeader.add(btnLibrary);
        rightHeader.add(btnLogout);

        headerPanel.add(lblBrand, BorderLayout.WEST);
        headerPanel.add(rightHeader, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // =========================================
        // BAGIAN SIDEBAR (FILTER & SORT)
        // =========================================
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(25, 25, 25));
        sidebarPanel.setPreferredSize(new Dimension(200, 0)); // Lebar sidebar
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // Label Filter
        JLabel lblFilter = new JLabel("FILTER GENRE");
        lblFilter.setForeground(Color.GRAY);
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFilter.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Dropdown Genre
        String[] genres = {"All", "Action", "Drama", "Sci-Fi", "Horror", "Comedy", "Animation"};
        cbGenre = new JComboBox<>(genres);
        styleComboBox(cbGenre);
        
        // Label Sort
        JLabel lblSort = new JLabel("SORT BY");
        lblSort.setForeground(Color.GRAY);
        lblSort.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSort.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Dropdown Sorting
        String[] sorts = {"Title (A-Z)", "Rating (High-Low)", "Year (Newest)"};
        cbSort = new JComboBox<>(sorts);
        styleComboBox(cbSort);

        // Logic: Saat filter berubah, load ulang data!
        cbGenre.addActionListener(e -> loadFilms());
        cbSort.addActionListener(e -> loadFilms());

        // Masukkan ke Sidebar
        sidebarPanel.add(lblFilter);
        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(cbGenre);
        sidebarPanel.add(Box.createVerticalStrut(30));
        sidebarPanel.add(lblSort);
        sidebarPanel.add(Box.createVerticalStrut(10));
        sidebarPanel.add(cbSort);

        add(sidebarPanel, BorderLayout.WEST);

        // =========================================
        // BAGIAN UTAMA (GRID FILM)
        // =========================================
        gridPanel = new JPanel();
        gridPanel.setBackground(new Color(30, 30, 30));
        // Layout Grid: 4 Kolom, Baris otomatis, Jarak 20px
        gridPanel.setLayout(new GridLayout(0, 4, 20, 20)); 
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Agar bisa discroll jika film banyak
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Scroll speed
        
        add(scrollPane, BorderLayout.CENTER);

        // Load data pertama kali
        loadFilms();
    }

    // Method Helper untuk Style Button Header
    private void styleHeaderButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Method Helper untuk Style ComboBox
    private void styleComboBox(JComboBox box) {
        box.setMaximumSize(new Dimension(200, 35));
        box.setAlignmentX(Component.LEFT_ALIGNMENT);
        box.setBackground(Color.WHITE);
        box.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }

    // =========================================
    // LOGIC FILTERING & SORTING (DATABASE)
    // =========================================
    private void loadFilms() {
        gridPanel.removeAll(); // Bersihkan grid lama

        // 1. Ambil Value dari Dropdown
        String selectedGenre = (String) cbGenre.getSelectedItem();
        String selectedSort = (String) cbSort.getSelectedItem();

        // 2. Bangun Query SQL Dasar
        String sql = "SELECT * FROM films";

        // 3. Tambah Logika Filtering (WHERE)
        boolean hasWhere = false;
        if (!"All".equals(selectedGenre)) {
            sql += " WHERE genre = '" + selectedGenre + "'";
            hasWhere = true;
        }

        // 4. Tambah Logika Sorting (ORDER BY)
        if ("Rating (High-Low)".equals(selectedSort)) {
            sql += " ORDER BY rating DESC";
        } else if ("Year (Newest)".equals(selectedSort)) {
            sql += " ORDER BY release_year DESC";
        } else {
            sql += " ORDER BY title ASC"; // Default A-Z
        }

        // 5. Eksekusi Query
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            boolean adaData = false;
            while (rs.next()) {
                adaData = true;
                // Bungkus data ke Objek Film
                Film film = new Film(
                    rs.getInt("film_id"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getDouble("rating"),
                    rs.getString("synopsis"),
                    rs.getString("poster_path"),
                    rs.getString("trailer_gif_path"),
                    rs.getString("audio_path")
                );

                // Masukkan ke Grid (Passing currentUser juga)
                gridPanel.add(new FilmCard(film, currentUser));
            }

            if (!adaData) {
                JLabel lblEmpty = new JLabel("Tidak ada film ditemukan.");
                lblEmpty.setForeground(Color.WHITE);
                gridPanel.add(lblEmpty);
            }

            // Update tampilan Swing
            gridPanel.revalidate();
            gridPanel.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error load data: " + e.getMessage());
        }
    }
}