package com.absolut.model;

public interface SubscriptionPlan {
    boolean canAddToWatchlist(int currentWatchlistSize);
    String getPlanName();
}