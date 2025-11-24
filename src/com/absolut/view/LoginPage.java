package com.absolut.view;

import com.absolut.components.CinemaButton;
import com.absolut.model.User;
import javax.swing.*;
import java.awt.*;

public class LoginPage extends JFrame {

    // Komponen GUI
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private CinemaButton btnLogin;

    public LoginPage() {
        // 1. Setup Frame Utama
        setTitle("AbsolutCinema - Login");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Biar muncul di tengah layar
        setLayout(new BorderLayout());

        // 2. Panel Kiri (Gambar/Logo)
        JPanel panelKiri = new JPanel();
        panelKiri.setBackground(new Color(18, 18, 18)); // Gelap
        panelKiri.setPreferredSize(new Dimension(400, 600));
        panelKiri.setLayout(new GridBagLayout());
        
        JLabel lblLogo = new JLabel("<html><div style='text-align: center;'>ABSOLUT<br><span style='color:#F2C94C'>CINEMA</span></div></html>");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 48));
        lblLogo.setForeground(Color.WHITE);
        panelKiri.add(lblLogo);

        // 3. Panel Kanan (Form Input)
        JPanel panelKanan = new JPanel();
        panelKanan.setBackground(new Color(30, 30, 30)); // Abu Gelap
        panelKanan.setLayout(new GridBagLayout()); // Biar form di tengah vertikal
        
        // Wadah Form (Biar rapi)
        JPanel formBox = new JPanel();
        formBox.setLayout(new GridLayout(5, 1, 10, 10)); // 5 Baris, jarak 10px
        formBox.setBackground(new Color(30, 30, 30));
        formBox.setPreferredSize(new Dimension(300, 250));

        // Komponen Form
        JLabel lblTitle = new JLabel("Welcome Back");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        txtUsername = new JTextField();
        txtUsername.putClientProperty("JTextField.placeholderText", "Username"); // *Butuh FlatLaf sebenernya, tapi gpp
        styleField(txtUsername);

        txtPassword = new JPasswordField();
        styleField(txtPassword);

        btnLogin = new CinemaButton("SIGN IN");

        // Masukkan ke FormBox
        formBox.add(lblTitle);
        formBox.add(new JLabel("Username:", SwingConstants.LEFT) {{ setForeground(Color.GRAY); }});
        formBox.add(txtUsername);
        formBox.add(new JLabel("Password:", SwingConstants.LEFT) {{ setForeground(Color.GRAY); }});
        formBox.add(txtPassword);
        
        // Tambah tombol terpisah biar ada jarak
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        panelKanan.add(formBox, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0); // Jarak atas
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelKanan.add(btnLogin, gbc);

        // 4. Gabungkan Panel
        add(panelKiri, BorderLayout.WEST);
        add(panelKanan, BorderLayout.CENTER);

        // 5. Event Handling (Logika Klik Tombol)
        btnLogin.addActionListener(e -> handleLogin());
    }

    // Method styling biar text field keliatan modern
    private void styleField(JTextField field) {
        field.setBackground(new Color(50, 50, 50));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    // Method saat tombol diklik
    private void handleLogin() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());

        // Panggil Logika Backend yang sudah kita tes tadi!
        User loggedInUser = User.login(user, pass);

    if (loggedInUser != null) {
        // HAPUS JOptionPane (Popup) kalau mau langsung masuk
        // JOptionPane.showMessageDialog(...) <-- Boleh dihapus atau dikomen
        
        // BUKA DASHBOARD
        DashboardPage dashboard = new DashboardPage(loggedInUser);
        dashboard.setVisible(true);
        
        // TUTUP LOGIN
        this.dispose();
            
        } else {
            JOptionPane.showMessageDialog(this, "Username atau Password Salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
        }
    }
}