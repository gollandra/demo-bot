package ru.test.command.tgbot.demobot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.test.command.tgbot.demobot.service.AdminService;
import ru.wdeath.managerbot.lib.bot.TelegramLongPollingEngine;
import ru.wdeath.managerbot.lib.bot.annotations.CommandFirst;
import ru.wdeath.managerbot.lib.bot.annotations.CommandNames;
import ru.wdeath.managerbot.lib.bot.annotations.CommandOther;
import ru.wdeath.managerbot.lib.bot.annotations.ParamName;

@CommandNames("/add_admin")
@Service
public class AddAdminCommand {
    @Autowired
    private AdminService adminService;

    @CommandFirst
    public void call(TelegramLongPollingEngine engine,
                     @ParamName("chatId") Long chatId,
                     @ParamName("userId") Long userId) {
        send(engine, adminService.enterAdminId(userId, chatId));
    }

    @CommandOther
    public void save(TelegramLongPollingEngine engine,
                     Message message,
                     @ParamName("chatId") Long chatId,
                     @ParamName("userId") Long userId) {
        send(engine, adminService.saveAdmin(userId, chatId, message.getText()));
    }

    private void send(TelegramLongPollingEngine engine, SendMessage sendMessage) {
        try {
            engine.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
