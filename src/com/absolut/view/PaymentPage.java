package com.absolut.view;

import com.absolut.components.CinemaButton;
import com.absolut.model.User;
import javax.swing.*;
import java.awt.*;

public class PaymentPage extends JFrame {

    public PaymentPage(String username, String password) {

        setTitle("Premium Payment");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // === PANEL UTAMA ===
        JPanel panel = new JPanel();
        panel.setBackground(new Color(25, 25, 25));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // === JUDUL ===
        JLabel lbl = new JLabel("Premium Payment");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbl.setForeground(Color.WHITE);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        // === HARGA ===
        JLabel price = new JLabel("Rp 50.000 / Lifetime");
        price.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        price.setForeground(new Color(242, 201, 76));
        price.setAlignmentX(Component.CENTER_ALIGNMENT);

        // === LOAD GAMBAR QR ===
        ImageIcon icon = new ImageIcon("resources/images/qrcode.jpeg");
        Image scaled = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        JLabel qrLabel = new JLabel(new ImageIcon(scaled));
        qrLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // === NOTE ===
        JLabel note = new JLabel("Scan QR untuk menyelesaikan pembayaran");
        note.setForeground(Color.GRAY);
        note.setAlignmentX(Component.CENTER_ALIGNMENT);

        // === TOMBOL KONFIRMASI BAYAR ===
        CinemaButton btnConfirm = new CinemaButton("KONFIRMASI");
        btnConfirm.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnConfirm.addActionListener(e -> {
            boolean success = User.register(username, password, "PREMIUM");

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Pembayaran berhasil diverifikasi!\nAkun Premium berhasil dibuat.");

                new LoginPage().setVisible(true);
                this.dispose();

            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal membuat akun!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // === TOMBOL KEMBALI (BATALKAN PEMBAYARAN) ===
        CinemaButton btnBack = new CinemaButton("BATALKAN");
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnBack.addActionListener(e -> {
            new RegisterPage().setVisible(true);   // arahkan ke halaman sebelum PaymentPage
            this.dispose();
        });

        // === TAMBAHKAN KE PANEL ===
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(price);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(qrLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(note);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnConfirm);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnBack);

        add(panel);
    }
}
