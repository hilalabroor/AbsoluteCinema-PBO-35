package com.absolut.model;

import com.absolut.database.DBConnection;
import java.sql.*;

// Abstract Class: Tidak bisa dibuat objeknya langsung (new User() -> Error)
public abstract class User {
    protected int id;
    protected String username;
    protected String password;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // --- METHOD ABSTRAK (Polymorphism) ---
    // Setiap anak WAJIB punya aturan sendiri soal ini
    public abstract boolean canAddToWatchlist(int currentWatchlistSize);
    public abstract String getUserType();

    // --- GETTER ---
    public int getId() { return id; }
    public String getUsername() { return username; }

    // --- FITUR LOGIN (STATIC) ---
    // Method ini nge-cek DB, lalu mengembalikan objek FreeUser atau PremiumUser
    public static User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("user_id");
                String type = rs.getString("subscription_type"); // 'FREE' atau 'PREMIUM'
                
                // DISINI KEAJAIBAN POLYMORPHISM TERJADI
                if ("PREMIUM".equalsIgnoreCase(type)) {
                    return new PremiumUser(id, username, password);
                } else {
                    return new FreeUser(id, username, password);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Login gagal
    }

    // --- FITUR REGISTER (STATIC) ---
    public static boolean register(String username, String password, String type) {
        String sql = "INSERT INTO users (username, password, subscription_type) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, type.toUpperCase()); // Pastikan kapital (FREE/PREMIUM)
            
            return ps.executeUpdate() > 0; // Return true jika berhasil
        } catch (SQLException e) {
            System.out.println("Register Gagal: " + e.getMessage());
            return false;
        }
    }
}