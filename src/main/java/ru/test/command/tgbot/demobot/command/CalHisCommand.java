package ru.test.command.tgbot.demobot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.test.command.tgbot.demobot.repository.MathOperationRepository;
import ru.test.command.tgbot.demobot.service.AdminService;
import ru.wdeath.managerbot.lib.bot.TelegramLongPollingEngine;
import ru.wdeath.managerbot.lib.bot.annotations.CommandFirst;
import ru.wdeath.managerbot.lib.bot.annotations.CommandNames;
import ru.wdeath.managerbot.lib.bot.annotations.ParamName;

import java.util.Objects;
import java.util.stream.Collectors;

@CommandNames("/cal_his")
@Service
public class CalHisCommand {
    @Autowired
    private MathOperationRepository repository;

    @Autowired
    private AdminService adminService;

    @CommandFirst
    public void response(TelegramLongPollingEngine engine,
                         @ParamName("chatId") Long chatId,
                         @ParamName("userId") Long userId) {
        SendMessage sendMessage = adminService.findLastUsersAndLastOperations(userId, chatId);
        if (sendMessage == null) {
            sendMessage.setChatId(chatId);
            String text = repository.findAll(userId).stream().map(Objects::toString).collect(Collectors.joining("\n"));
            sendMessage.setText(text);
            sendMessage.setChatId(chatId);
        }
        try {
            engine.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
