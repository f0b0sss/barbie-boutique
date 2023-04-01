package com.barbieboutique.bucket.entity;

import java.time.LocalDateTime;

public enum OrderStatus {
    NEW, CREATED, APPROVED, SENT, CANCELED, COMPLETE;

    private LocalDateTime date;

    OrderStatus() {
        this.date = LocalDateTime.now();
    }

    public LocalDateTime getDate() {
        return date;
    }
}
