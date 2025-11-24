package com.absolut.model;

public class PremiumUser extends User {

    public PremiumUser(int id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public boolean canAddToWatchlist(int currentWatchlistSize) {
        // LOGIKA BISNIS: Sultan mah bebas!
        return true;
    }

    @Override
    public String getUserType() {
        return "PREMIUM";
    }
}