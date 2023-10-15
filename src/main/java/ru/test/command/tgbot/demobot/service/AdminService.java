package ru.test.command.tgbot.demobot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.test.command.tgbot.demobot.entity.MathOperation;
import ru.test.command.tgbot.demobot.repository.AdminRepository;
import ru.test.command.tgbot.demobot.repository.MathOperationRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private MathOperationRepository mathOperationRepository;

    @Value("#{'${botadmins}'.split(', ')}")
    private List<String> listOfAdmins;

    public SendMessage enterAdminId(Long userId, Long chatId) {
        if (isAdmin(userId)) {
            return message("Введите ID нового админа", chatId);
        }
        return wrongMessage(chatId);
    }

    public SendMessage saveAdmin(Long userId, Long chatId, String newAdminId) {
        if (isAdmin(userId)) {
            adminRepository.save(newAdminId);
            return message("OK", chatId);
        }
        return wrongMessage(chatId);
    }

    public SendMessage findAllAdmins(Long userId, Long chatId) {
        if (isAdmin(userId)) {
            String text = String.join("\n", adminRepository.findAll());
            return message(text, chatId);
        }
        return wrongMessage(chatId);
    }

    public SendMessage findLastUsersAndLastOperations(Long userId, Long chatId) {
        if (isAdmin(userId)) {
            Map<Long, List<MathOperation>> fromRepo = mathOperationRepository.findLast5UsersAnd5LastMathOperations();
            StringBuilder text = new StringBuilder();
            for (var pair : fromRepo.entrySet()) {
                text.append(pair.getKey())
                        .append(":\n")
                        .append(pair.getValue().stream().map(Objects::toString).collect(Collectors.joining("\n")));
            }
            return message(text.toString(), chatId);
        }
        return null;
    }

    private SendMessage message(String text, Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);
        return sendMessage;
    }

    private SendMessage wrongMessage(Long chatId) {
        return message("произошла ошибка", chatId);
    }

    private boolean isAdmin(Long userId) {
        return listOfAdmins.contains(userId.toString()) || adminRepository.findAll().contains(userId.toString());
    }
}
