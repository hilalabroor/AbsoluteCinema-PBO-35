package com.absolut.view;

import com.absolut.components.CinemaButton;
import com.absolut.model.User;
import javax.swing.*;
import java.awt.*;

public class RegisterPage extends JPanel {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JRadioButton rbFree, rbPremium;
    private CinemaButton btnRegister;
    private JButton btnLoginLink;
    private Image bgImage;

    public RegisterPage() {
        setLayout(new GridLayout(1, 2));
        setBackground(Color.BLACK);
        
        try {
            ImageIcon icon = new ImageIcon("resources/images/register_bg.jpg"); 
            bgImage = icon.getImage();
        } catch (Exception e) {
            bgImage = null;
        }

        // PANEL KIRI
        JPanel panelKiri = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                
                // Gambar Background
                if (bgImage != null) {
                    // Rendering halus agar tidak pecah
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    // Kalau gambar tidak ada, hitam polos
                    g2.setColor(Color.BLACK);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
       
        // PANEL KANAN: FORM INPUT
        JPanel panelKanan = new JPanel(new GridBagLayout()); 
        panelKanan.setBackground(Color.BLACK);

        // Wadah Form
        JPanel formBox = new JPanel(new GridLayout(7, 1, 10, 12)); 
        formBox.setBackground(Color.BLACK);
        formBox.setPreferredSize(new Dimension(350, 400)); 

        // Judul
        JLabel lblTitle = new JLabel("Sign Up");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        // Input Fields
        txtUsername = new JTextField();
        styleField(txtUsername);
        txtPassword = new JPasswordField();
        styleField(txtPassword);

        // Radio Button Styling
        rbFree = new JRadioButton("Free Account (Limit 5 Films)");
        styleRadio(rbFree);
        rbFree.setSelected(true);

        rbPremium = new JRadioButton("Premium (Unlimited)");
        styleRadio(rbPremium);
        rbPremium.setForeground(new Color(255, 0, 110)); // Pink

        ButtonGroup group = new ButtonGroup();
        group.add(rbFree);
        group.add(rbPremium);

        // Tombol Register
        btnRegister = new CinemaButton("CREATE ACCOUNT");
        
        // Link Login
        btnLoginLink = new JButton("Already have an account? Sign In");
        btnLoginLink.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnLoginLink.setBorderPainted(false);
        btnLoginLink.setContentAreaFilled(false);
        btnLoginLink.setForeground(Color.LIGHT_GRAY);
        btnLoginLink.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Susun Form Box
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
        formBox.add(rbFree);
        formBox.add(rbPremium);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        panelKanan.add(formBox, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(30, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelKanan.add(btnRegister, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(15, 0, 0, 0);
        panelKanan.add(btnLoginLink, gbc);

        // Gabungkan
        add(panelKiri);
        add(panelKanan);

        // Logika Tombol
        btnRegister.addActionListener(e -> handleRegister());
        
        btnLoginLink.addActionListener(e -> MainFrame.getInstance().showLogin());
    }

    private void styleField(JTextField field) {
        field.setBackground(new Color(40, 40, 40)); // Abu Gelap biar kontras dikit dari background
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60))); // Border tipis
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    }

    private void styleRadio(JRadioButton rb) {
        rb.setBackground(Color.BLACK);
        rb.setForeground(Color.WHITE);
        rb.setFocusPainted(false);
        rb.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private void handleRegister() {
        String user = txtUsername.getText();
        String pass = new String(txtPassword.getPassword());
        String type = rbPremium.isSelected() ? "PREMIUM" : "FREE";

        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Data tidak boleh kosong!");
            return;
        }

        if (type.equals("FREE")) {
            boolean success = User.register(user, pass, "FREE");
            if (success) {
                JOptionPane.showMessageDialog(this, "Akun Free Berhasil Dibuat!\nSilakan Login.");
                MainFrame.getInstance().showLogin();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal! Username mungkin sudah dipakai.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        MainFrame.getInstance().showPayment(user, pass);
    }
}