package ru.test.command.tgbot.demobot.command;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.test.command.tgbot.demobot.entity.MathOperation;
import ru.test.command.tgbot.demobot.repository.Repository;
import ru.test.command.tgbot.demobot.util.ParseNumber;
import ru.wdeath.managerbot.lib.bot.TelegramLongPollingEngine;
import ru.wdeath.managerbot.lib.bot.annotations.CommandFirst;
import ru.wdeath.managerbot.lib.bot.annotations.CommandNames;
import ru.wdeath.managerbot.lib.bot.annotations.CommandOther;
import ru.wdeath.managerbot.lib.bot.annotations.ParamName;
import ru.wdeath.managerbot.lib.bot.session.UserBotSession;

import java.util.HashMap;
import java.util.Map;

@CommandNames("/cal")
@Service
public class CalCommand {
    public static Map<Long, String> operations = new HashMap<>();

    @CommandFirst
    public void cal(TelegramLongPollingEngine engine, Message message, @ParamName("chatId") Long chatId, UserBotSession userBotSession) {
        userBotSession.setData(new MathOperation());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Введите первое число");
        sendMessage.setChatId(chatId);
        operations.put(chatId, "first number");
        try {
            engine.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @CommandOther
    public void other(TelegramLongPollingEngine engine, Message message, @ParamName("chatId") Long chatId, UserBotSession userBotSession) {
        String text;
        MathOperation mathOperation = (MathOperation) userBotSession.getData();
        if (operations.get(chatId).equals("operator")) {
            mathOperation.setOperation(message.getText());
            mathOperation.evaluateExpression();
            Repository.repository.add(mathOperation);
            text = "Ваш результат: " + mathOperation.getResult();
        } else if (operations.get(chatId).equals("first number")) {
            try {
                Double number = ParseNumber.parseNumber(message.getText());
                mathOperation.setA(number);
                text = "Введите второе число";
                operations.put(chatId, "second number");
            } catch (Exception e) {
                text = "Это не число\nВведите первое число";
            }
        } else {
            try {
                Double number = ParseNumber.parseNumber(message.getText());
                mathOperation.setB(number);
                text = "Введите знак операции";
                operations.put(chatId, "operator");
            } catch (Exception e) {
                text = "Это не число\nВведите второе число";
            }
        }

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);

        try {
            engine.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
