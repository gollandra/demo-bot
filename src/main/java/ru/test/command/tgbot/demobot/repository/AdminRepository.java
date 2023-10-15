package ru.test.command.tgbot.demobot.repository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminRepository {

    private final List<String> admins;

    public AdminRepository() {
        this.admins = new ArrayList<>();
    }

    public List<String> findAll() {
        return admins;
    }

    public void save(String adminId) {
        this.admins.add(adminId);
    }
}
