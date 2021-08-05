package com.planner.planner.models.registration;

public interface EmailSender {
    void send(String to, String email);
}
