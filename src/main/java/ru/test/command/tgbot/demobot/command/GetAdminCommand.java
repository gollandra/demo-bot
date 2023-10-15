package ru.test.command.tgbot.demobot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.test.command.tgbot.demobot.service.AdminService;
import ru.wdeath.managerbot.lib.bot.TelegramLongPollingEngine;
import ru.wdeath.managerbot.lib.bot.annotations.CommandFirst;
import ru.wdeath.managerbot.lib.bot.annotations.CommandNames;
import ru.wdeath.managerbot.lib.bot.annotations.ParamName;

@CommandNames("/get_admin")
@Service
public class GetAdminCommand {
    @Autowired
    private AdminService adminService;

    @CommandFirst
    public void findAllAdmins(TelegramLongPollingEngine engine,
                              @ParamName("chatId") Long chatId,
                              @ParamName("userId") Long userId) {
        SendMessage sendMessage = adminService.findAllAdmins(userId, chatId);
        try {
            engine.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
