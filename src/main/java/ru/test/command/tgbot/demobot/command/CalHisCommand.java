package ru.test.command.tgbot.demobot.command;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.test.command.tgbot.demobot.repository.Repository;
import ru.wdeath.managerbot.lib.bot.TelegramLongPollingEngine;
import ru.wdeath.managerbot.lib.bot.annotations.CommandFirst;
import ru.wdeath.managerbot.lib.bot.annotations.CommandNames;
import ru.wdeath.managerbot.lib.bot.annotations.ParamName;

import java.util.Objects;
import java.util.stream.Collectors;

@CommandNames("/cal_his")
@Service
public class CalHisCommand {

    @CommandFirst
    public void response(TelegramLongPollingEngine engine, @ParamName("chatId") Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        String text = Repository.repository.stream().map(Objects::toString).collect(Collectors.joining("\n"));
        sendMessage.setText(text);
        sendMessage.setChatId(chatId);

        try {
            engine.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
