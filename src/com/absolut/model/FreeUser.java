package com.absolut.model;

public class FreeUser extends User {

    public FreeUser(int id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public boolean canAddToWatchlist(int currentWatchlistSize) {
        // LOGIKA BISNIS: Dibatasi cuma 5 film
        if (currentWatchlistSize >= 5) {
            System.out.println("Ups! User Gratis cuma boleh simpan 5 film.");
            return false; 
        }
        return true;
    }

    @Override
    public String getUserType() {
        return "FREE";
    }
}