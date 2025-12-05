package com.absolut.view;

import com.absolut.components.CinemaButton;
import com.absolut.model.FreeUser;
import com.absolut.model.PremiumUser;
import com.absolut.model.User;
import com.absolut.database.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PaymentPage extends JPanel {

    private User currentUser; 
    private String username;
    private String password;
    private boolean isUpgradeMode = false; 

    private JLabel lblTimer;
    private boolean isPaymentActive = true;

    // Constructor 1: Register Baru
    public PaymentPage(String username, String password) {
        this.username = username;
        this.password = password;
        this.isUpgradeMode = false;
        initUI();
    }

    // Constructor 2: Upgrade
    public PaymentPage(User user) {
        this.currentUser = user;
        this.username = user.getUsername();
        this.isUpgradeMode = true;
        initUI();
    }

    // Helper untuk Mengambil data user terbaru (Free/Premium) untuk dilempar ke Dashboard
    private User getUserForDashboard(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String uName = rs.getString("username");
                String pass = rs.getString("password");
                String type = rs.getString("subscription_type");
                
                if ("PREMIUM".equalsIgnoreCase(type)) {
                    return new PremiumUser(userId, uName, pass);
                } else {
                    return new FreeUser(userId, uName, pass);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        JPanel center = new JPanel();
        center.setBackground(Color.BLACK);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        center.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitle = new JLabel(isUpgradeMode ? "UPGRADE PREMIUM" : "AKTIVASI AKUN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblPrice = new JLabel("Rp 50.000 (Lifetime)");
        lblPrice.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblPrice.setForeground(new Color(242, 201, 76));
        lblPrice.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblQr = new JLabel();
        lblQr.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icon = new ImageIcon("resources/images/qrcode.jpeg");
            Image scaled = icon.getImage().getScaledInstance(160, 160, Image.SCALE_SMOOTH);
            lblQr.setIcon(new ImageIcon(scaled));
        } catch (Exception e) {
            lblQr.setText("[QR]");
            lblQr.setForeground(Color.WHITE);
        }

        lblTimer = new JLabel("Waktu: 60 detik");
        lblTimer.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTimer.setForeground(new Color(255, 80, 80));
        lblTimer.setAlignmentX(Component.CENTER_ALIGNMENT);

        CinemaButton btnConfirm = new CinemaButton("SUDAH BAYAR");
        btnConfirm.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConfirm.setMaximumSize(new Dimension(220, 40));

        JButton btnCancel = new JButton("Batalkan");
        btnCancel.setForeground(Color.LIGHT_GRAY);
        btnCancel.setContentAreaFilled(false);
        btnCancel.setBorderPainted(false);
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(lblTitle);
        center.add(Box.createVerticalStrut(12));
        center.add(lblPrice);
        center.add(Box.createVerticalStrut(18));
        center.add(lblQr);
        center.add(Box.createVerticalStrut(18));
        center.add(lblTimer);
        center.add(Box.createVerticalStrut(18));
        center.add(btnConfirm);
        center.add(Box.createVerticalStrut(10));
        center.add(btnCancel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(center, gbc);

        // events
        // TOMBOL KONFIRMASI (BAYAR)
        btnConfirm.addActionListener(e -> {
            isPaymentActive = false; 
            int existingId = getUserIdByUsername(username);
            boolean success = false;

            if (existingId != -1) {
                success = User.upgradeToPremium(existingId);
            } else {
                success = User.register(username, password, "PREMIUM");
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Pembayaran Berhasil! Akun Anda kini PREMIUM.");
                
                User freshUser = getUserForDashboard(existingId != -1 ? existingId : getUserIdByUsername(username));
                
                if (isUpgradeMode) {
                    MainFrame.getInstance().showDashboard(freshUser);
                } else {
                    MainFrame.getInstance().showLogin();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memproses akun.", "Error", JOptionPane.ERROR_MESSAGE);
                isPaymentActive = true; 
            }
        });

        btnCancel.addActionListener(e -> handleCancel());
        startCountdown();
    }

    private void handleCancel() {
        isPaymentActive = false;
        if (isUpgradeMode) {
            MainFrame.getInstance().showDashboard(currentUser);
        } else {
            MainFrame.getInstance().showRegister();
        }
    }

    private int getUserIdByUsername(String username) {
        String sql = "SELECT user_id FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("user_id");
        } catch (Exception e) {}
        return -1;
    }

    private void startCountdown() {
        new Thread(() -> {
            int t = 60;
            while (t > 0 && isPaymentActive) {
                try {
                    Thread.sleep(1000);
                    t--;
                    final int time = t;
                    SwingUtilities.invokeLater(() -> lblTimer.setText("Waktu: " + time + "s"));
                } catch (InterruptedException e) {}
            }
            if (t == 0 && isPaymentActive) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Waktu Habis!");
                    handleCancel();
                });
            }
        }).start();
    }
}