package com.absolut.model;

import com.absolut.database.DBConnection;
import java.sql.*;

public abstract class User implements SubscriptionPlan{
    protected int id;
    protected String username;
    protected String password;

    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // METHOD ABSTRAK 
    public abstract boolean canAddToWatchlist(int currentWatchlistSize);
    public abstract String getUserType();

    @Override
    public abstract String getPlanName();

    // GETTER
    public int getId() { return id; }
    public String getUsername() { return username; }

    // FITUR LOGIN
    public static User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("user_id");
                String type = rs.getString("subscription_type");
                
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

    // FITUR REGISTER
    public static boolean register(String username, String password, String type) {
        String sql = "INSERT INTO users (username, password, subscription_type) VALUES (?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, type.toUpperCase());
            
            return ps.executeUpdate() > 0; // Return true jika berhasil
        } catch (SQLException e) {
            System.out.println("Register Gagal: " + e.getMessage());
            return false;
        }
    }

    // FITUR UPGRADE
    public static boolean upgradeToPremium(int userId) {
        String sql = "UPDATE users SET subscription_type = 'PREMIUM' WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            return ps.executeUpdate() > 0; // True jika berhasil update
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}