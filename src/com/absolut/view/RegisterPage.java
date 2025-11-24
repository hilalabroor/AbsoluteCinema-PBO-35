package com.absolut.view;

import com.absolut.components.CinemaButton;
import com.absolut.model.User;
import javax.swing.*;
import java.awt.*;

public class RegisterPage extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JRadioButton rbFree, rbPremium;
    private CinemaButton btnRegister;
    private JButton btnLoginLink;

    public RegisterPage() {
        // 1. Setup Frame
        setTitle("AbsolutCinema - Create Account");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 2. Panel Kiri (Visual)
        JPanel panelKiri = new JPanel(new GridBagLayout());
        panelKiri.setBackground(new Color(18, 18, 18));
        panelKiri.setPreferredSize(new Dimension(400, 600));
        
        JLabel lblQuote = new JLabel("<html><div style='text-align:center; color:gray;'>Join the Ultimate<br>Film Database</div></html>");
        lblQuote.setFont(new Font("Segoe UI", Font.ITALIC, 24));
        panelKiri.add(lblQuote);

        // 3. Panel Kanan (Form)
        JPanel panelKanan = new JPanel(new GridBagLayout());
        panelKanan.setBackground(new Color(30, 30, 30));

        // Wadah Form
        JPanel formBox = new JPanel(new GridLayout(7, 1, 10, 10)); // 7 Baris
        formBox.setBackground(new Color(30, 30, 30));
        formBox.setPreferredSize(new Dimension(320, 350));

        // Judul
        JLabel lblTitle = new JLabel("Sign Up");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        // Input Fields
        txtUsername = new JTextField();
        styleField(txtUsername);
        txtPassword = new JPasswordField();
        styleField(txtPassword);

        // Radio Button (Pilihan Akun)
        rbFree = new JRadioButton("Free Account (Limit 5 Films)");
        rbFree.setBackground(new Color(30, 30, 30));
        rbFree.setForeground(Color.WHITE);
        rbFree.setFocusPainted(false);
        rbFree.setSelected(true); // Default

        rbPremium = new JRadioButton("Premium (Unlimited)");
        rbPremium.setBackground(new Color(30, 30, 30));
        rbPremium.setForeground(new Color(242, 201, 76)); // Kuning
        rbPremium.setFocusPainted(false);

        // Grouping Radio Button (Biar cuma bisa pilih satu)
        ButtonGroup group = new ButtonGroup();
        group.add(rbFree);
        group.add(rbPremium);

        // Tombol Register
        btnRegister = new CinemaButton("CREATE ACCOUNT");
        
        // Link ke Login
        btnLoginLink = new JButton("Already have an account? Sign In");
        btnLoginLink.setBorderPainted(false);
        btnLoginLink.setContentAreaFilled(false);
        btnLoginLink.setForeground(Color.GRAY);
        btnLoginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Susun Form
        formBox.add(lblTitle);
        formBox.add(new JLabel("Username:", SwingConstants.LEFT) {{ setForeground(Color.GRAY); }});
        formBox.add(txtUsername);
        formBox.add(new JLabel("Password:", SwingConstants.LEFT) {{ setForeground(Color.GRAY); }});
        formBox.add(txtPassword);
        formBox.add(rbFree);
        formBox.add(rbPremium);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        panelKanan.add(formBox, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(20, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelKanan.add(btnRegister, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        panelKanan.add(btnLoginLink, gbc);

        // 4. Gabungkan
        add(panelKiri, BorderLayout.WEST);
        add(panelKanan, BorderLayout.CENTER);

        // 5. Logika Tombol
        btnRegister.addActionListener(e -> handleRegister());
        
        btnLoginLink.addActionListener(e -> {
            new LoginPage().setVisible(true);
            this.dispose();
        });
    }

    private void styleField(JTextField field) {
        field.setBackground(new Color(50, 50, 50));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void handleRegister() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());
        String type = rbPremium.isSelected() ? "PREMIUM" : "FREE";

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data tidak boleh kosong!");
            return;
        }

        // Panggil Logika Backend User.java
        boolean success = User.register(user, pass, type);

        if (success) {
            JOptionPane.showMessageDialog(this, "Akun Berhasil Dibuat!\nSilakan Login.");
            new LoginPage().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal! Username mungkin sudah dipakai.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}