package com.absolut.view;

import com.absolut.components.FilmCard;
import com.absolut.database.DBConnection;
import com.absolut.model.Film;
import com.absolut.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class DashboardPage extends JPanel {

    private User currentUser;
    private JPanel gridPanel;
    
    // Komponen Kontrol
    private JComboBox<String> cbGenre;
    private JComboBox<String> cbSort;
    private JTextField txtSearch;
    
    // Aset Banner
    private Image bannerImage;

    public DashboardPage(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout());
        setBackground(new Color(18,18,18));

        // Load Gambar Banner
        try {
            ImageIcon icon = new ImageIcon("resources/images/banner.png");
            bannerImage = icon.getImage();
        } catch (Exception e) {
            bannerImage = null;
        }

        // HEADER BAR
        JPanel headerBar = new JPanel(new BorderLayout());
        headerBar.setBackground(new Color(18, 18, 18));
        headerBar.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        // Garis bawah tipis
        headerBar.setBorder(BorderFactory.createCompoundBorder(
            headerBar.getBorder(),
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(50, 50, 50))
        ));

        // KIRI: Logo
        JLabel lblBrand = new JLabel("ABSOLUTE CINEMA");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblBrand.setForeground(Color.WHITE);

        // TENGAH: Search dan Filter
        JPanel centerTools = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        centerTools.setOpaque(false);

        txtSearch = new JTextField(20);
        txtSearch.putClientProperty("JTextField.placeholderText", "Cari judul film...");
        styleField(txtSearch);
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) loadFilms();
            }
        });

        JButton btnSearch = new JButton("Cari");
        styleButton(btnSearch, new Color(50, 50, 50), Color.WHITE);
        btnSearch.addActionListener(e -> loadFilms());

        String[] genres = {"All Genre", "Action", "Drama", "Sci-Fi", "Horror", "Comedy", "Crime", "Biography", "Adventure"};
        cbGenre = new JComboBox<>(genres);
        styleComboBox(cbGenre);
        cbGenre.addActionListener(e -> loadFilms());

        String[] sorts = {"A-Z", "Rating", "Newest"};
        cbSort = new JComboBox<>(sorts);
        styleComboBox(cbSort);
        cbSort.addActionListener(e -> loadFilms());

        centerTools.add(txtSearch);
        centerTools.add(btnSearch);
        centerTools.add(cbGenre);
        centerTools.add(cbSort);

        // KANAN: User Info
        JPanel rightTools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightTools.setOpaque(false);

        JLabel lblUser = new JLabel("Hi, " + user.getUsername());
        lblUser.setForeground(Color.LIGHT_GRAY);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JButton btnLibrary = new JButton("My Library");
        styleButton(btnLibrary, new Color(255, 0, 110), Color.WHITE);
        btnLibrary.addActionListener(e -> MainFrame.getInstance().showLibrary(currentUser));

        JButton btnLogout = new JButton("Logout");
        styleButton(btnLogout, new Color(255, 0, 0), Color.WHITE); //tombol logout merah terang
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Logout?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                MainFrame.getInstance().showLanding();
            }
        });

        rightTools.add(lblUser);
        rightTools.add(btnLibrary);
        rightTools.add(btnLogout);

        headerBar.add(lblBrand, BorderLayout.WEST);
        headerBar.add(centerTools, BorderLayout.CENTER);
        headerBar.add(rightTools, BorderLayout.EAST);

        add(headerBar, BorderLayout.NORTH);

        // KONTEN UTAMA
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(new Color(18, 18, 18));

        // BANNER PANEL
        JPanel bannerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                int w = getWidth();
                int h = getHeight();

                if (bannerImage != null) {
                    // Gambar banner memenuhi lebar panel
                    g2.drawImage(bannerImage, 0, 0, w, h, this);
                    
                    GradientPaint gp = new GradientPaint(
                        0, h - 100, new Color(18, 18, 18, 0), // Mulai transparan
                        0, h, new Color(18, 18, 18, 255)      // Berakhir hitam pekat di bawah
                    );
                    g2.setPaint(gp);
                    g2.fillRect(0, h - 100, w, 100);
                }
            }
        };
        // Tinggi banner
        bannerPanel.setPreferredSize(new Dimension(1200, 400));
        bannerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400)); // Biar bisa melebar full
        bannerPanel.setBackground(new Color(18, 18, 18));

        // GRID FILM PANEL
        gridPanel = new JPanel();
        gridPanel.setBackground(new Color(18, 18, 18));
        
        // Responsive Columns
        int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int columns = Math.max(4, screenWidth / 220); 
        
        gridPanel.setLayout(new GridLayout(0, columns, 20, 20)); 
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 40, 40)); // Margin

        // Masukkan Banner dan Grid ke Main Content
        mainContent.add(bannerPanel);
        mainContent.add(gridPanel);

        // Bungkus Main Content dengan ScrollPane
        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        //menyembyngkan scroll Vertical
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        
        add(scrollPane, BorderLayout.CENTER);

        loadFilms();
    }

    // LOGIC LOAD DATA 
    private void loadFilms() {
        gridPanel.removeAll();

        String keyword = txtSearch.getText().trim();
        String selectedGenre = (String) cbGenre.getSelectedItem();
        String selectedSort = (String) cbSort.getSelectedItem();

        StringBuilder sql = new StringBuilder("SELECT * FROM films WHERE 1=1");

        if (!keyword.isEmpty()) {
            sql.append(" AND title LIKE '%").append(keyword).append("%'");
        }
        if (!"All Genre".equals(selectedGenre)) {
            sql.append(" AND genre = '").append(selectedGenre).append("'");
        }

        if ("Rating".equals(selectedSort)) {
            sql.append(" ORDER BY rating DESC");
        } else if ("Newest".equals(selectedSort)) {
            sql.append(" ORDER BY release_year DESC");
        } else {
            sql.append(" ORDER BY title ASC");
        }

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql.toString())) {

            boolean adaData = false;
            while (rs.next()) {
                adaData = true;
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
                gridPanel.add(new FilmCard(film, currentUser));
            }

            if (!adaData) {
                JLabel lblEmpty = new JLabel("Film tidak ditemukan...", SwingConstants.CENTER);
                lblEmpty.setForeground(Color.GRAY);
                lblEmpty.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                gridPanel.add(lblEmpty); 
            }

            gridPanel.revalidate();
            gridPanel.repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void styleButton(JButton btn, Color bg, Color fg) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60))); // border gelap bukan putih
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 35));
    }

    private void styleField(JTextField field) {
        field.setOpaque(true);
        field.setBackground(new Color(50, 50, 50));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        // pakai line border gelap supaya tidak muncul garis putih dari LAF
        field.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60), 1));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(250, 35));
    }

    private void styleComboBox(JComboBox<String> box) {
        box.setOpaque(true);
        box.setBackground(new Color(50, 50, 50));
        box.setForeground(Color.WHITE);
        box.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        box.setPreferredSize(new Dimension(120, 35));
        box.setFocusable(false);
    }
}