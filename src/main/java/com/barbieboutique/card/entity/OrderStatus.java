package com.barbieboutique.card.entity;

import java.time.LocalDateTime;

public enum OrderStatus {
    NEW, APPROVED, SENT, CANCELED, COMPLETE;

    private LocalDateTime date;

    OrderStatus() {
        this.date = LocalDateTime.now();
    }

    public LocalDateTime getDate() {
        return date;
    }
}
