package com.absolut.view;

import com.absolut.components.CinemaButton;
import com.absolut.model.User;
import javax.swing.*;
import java.awt.*;

public class LoginPage extends JPanel {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private CinemaButton btnLogin;
    private JButton btnRegisterLink;
    
    // Gambar Background Kiri
    private Image bgImage;

    public LoginPage() {
        setLayout(new GridLayout(1, 2));
        setBackground(Color.BLACK);

        // Load Gambar Background
        try {
            ImageIcon icon = new ImageIcon("resources/images/login_bg.jpg");
            bgImage = icon.getImage();
        } catch (Exception e) {
            bgImage = null;
        }

        // PANEL KIRI (HANYA GAMBAR)
        JPanel panelKiri = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                if (bgImage != null) {
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g2.setColor(Color.BLACK);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };

        // PANEL KANAN (FORM INPUT)
        JPanel panelKanan = new JPanel();
        panelKanan.setBackground(Color.BLACK);
        panelKanan.setLayout(new GridBagLayout()); 
        
        // Wadah Form (Box)
        JPanel formBox = new JPanel();
        formBox.setLayout(new GridLayout(5, 1, 10, 15)); 
        formBox.setBackground(Color.BLACK);
        formBox.setPreferredSize(new Dimension(350, 300)); 

        // Komponen Form
        JLabel lblTitle = new JLabel("Welcome Back");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        txtUsername = new JTextField();
        txtUsername.putClientProperty("JTextField.placeholderText", "Username");
        styleField(txtUsername);

        txtPassword = new JPasswordField();
        styleField(txtPassword);

        btnLogin = new CinemaButton("SIGN IN");

        // Masukkan ke FormBox
        formBox.add(lblTitle);
        formBox.add(new JLabel("Username:", SwingConstants.LEFT) {{ 
            setForeground(Color.GRAY); 
            setFont(new Font("Segoe UI", Font.BOLD, 12)); 
        }});
        formBox.add(txtUsername);
        formBox.add(new JLabel("Password:", SwingConstants.LEFT) {{ 
            setForeground(Color.GRAY); 
            setFont(new Font("Segoe UI", Font.BOLD, 12));
        }});
        formBox.add(txtPassword);
        
        // Layout Manager
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        panelKanan.add(formBox, gbc);
        
        // Posisi Tombol Login
        gbc.gridy = 1;
        gbc.insets = new Insets(30, 0, 0, 0); 
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        panelKanan.add(btnLogin, gbc);

        // Link ke Register
        btnRegisterLink = new JButton("Don't have an account? Sign Up");
        btnRegisterLink.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnRegisterLink.setBorderPainted(false);
        btnRegisterLink.setContentAreaFilled(false);
        btnRegisterLink.setForeground(Color.LIGHT_GRAY);
        btnRegisterLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridy = 2;
        gbc.insets = new Insets(15, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE; // Jangan di-stretch
        panelKanan.add(btnRegisterLink, gbc);

        // Gabungkan Panel
        add(panelKiri);
        add(panelKanan);

        // Event Handling
        btnLogin.addActionListener(e -> {
            String user = txtUsername.getText();
            String pass = new String(txtPassword.getPassword());
            User loggedInUser = User.login(user, pass);
            if (loggedInUser != null) {
                MainFrame.getInstance().showDashboard(loggedInUser);
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password Salah!", "Login Gagal", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRegisterLink.addActionListener(e -> MainFrame.getInstance().showRegister());
    }

    private void styleField(JTextField field) {
        field.setBackground(new Color(40, 40, 40)); // Warnanya pake Abu Gelap biar kontras dikit dari background
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16)); 
    }
}