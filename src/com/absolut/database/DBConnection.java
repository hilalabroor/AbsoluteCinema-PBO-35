package com.absolut.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    // Konfigurasi Database
    // Pastikan XAMPP MySQL sudah di-START
    private static final String URL = "jdbc:mysql://localhost:3306/db_absolut_cinema";
    private static final String USER = "root"; 
    private static final String PASSWORD = ""; 
    
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // Ini baris yang butuh file .jar tadi
                Class.forName("com.mysql.cj.jdbc.Driver");

                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Sukses terkoneksi ke Database!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Error: Driver JDBC tidak ditemukan!");
            System.out.println("Pastikan file mysql-connector.jar sudah masuk 'Referenced Libraries'");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Error: Gagal koneksi ke Database!");
            System.out.println("Pastikan XAMPP MySQL sudah di-Start.");
            e.printStackTrace();
        }
        return connection;
    }
    
    // Main method untuk ngetes doang
    //  public static void main(String[] args) {
    //    getConnection();
    // }
}