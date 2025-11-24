package com.absolut.model;

import com.absolut.database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryDAO {

    // 1. Hitung jumlah film di watchlist user (Untuk validasi Limit Free User)
    public static int getWatchlistCount(int userId) {
        String sql = "SELECT COUNT(*) FROM user_films WHERE user_id = ? AND list_type = 'WATCHLIST'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 2. Masukkan Film ke Watchlist (INSERT)
    public static boolean addToWatchlist(int userId, int filmId) {
        // Cek dulu biar gak duplikat (Kalau udah ada, return false)
        if (isInWatchlist(userId, filmId)) return false;

        String sql = "INSERT INTO user_films (user_id, film_id, list_type) VALUES (?, ?, 'WATCHLIST')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, filmId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Cek apakah film sudah ada di watchlist
    public static boolean isInWatchlist(int userId, int filmId) {
        String sql = "SELECT 1 FROM user_films WHERE user_id = ? AND film_id = ? AND list_type = 'WATCHLIST'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, filmId);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Return true jika ada datanya
        } catch (Exception e) {
            return false;
        }
    }

    // 4. Ambil SEMUA film milik user (JOIN QUERY)
    // Ini fitur paling mahal nilai Database-nya: Menggabungkan tabel films dan user_films
    public static List<Film> getUserWatchlist(int userId) {
        List<Film> list = new ArrayList<>();
        
        String sql = "SELECT f.* FROM films f " +
                     "JOIN user_films uf ON f.film_id = uf.film_id " +
                     "WHERE uf.user_id = ? AND uf.list_type = 'WATCHLIST'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Bungkus data hasil query menjadi Objek Film
                list.add(new Film(
                    rs.getInt("film_id"),
                    rs.getString("title"),
                    rs.getString("genre"),
                    rs.getDouble("rating"),
                    rs.getString("synopsis"),
                    rs.getString("poster_path"),
                    rs.getString("trailer_gif_path"),
                    rs.getString("audio_path")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // 5. Hapus dari Watchlist (Opsional, tapi bagus kalau ada)
    public static void removeFromWatchlist(int userId, int filmId) {
        String sql = "DELETE FROM user_films WHERE user_id = ? AND film_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, filmId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}