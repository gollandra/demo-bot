package ru.test.command.tgbot.demobot.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.test.command.tgbot.demobot.entity.MathOperation;
import ru.test.command.tgbot.demobot.repository.MathOperationRepository;
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

    @Autowired
    private MathOperationRepository repository;

    @CommandFirst
    public void cal(TelegramLongPollingEngine engine, @ParamName("chatId") Long chatId, UserBotSession userBotSession) {
        userBotSession.setData(new MathOperation());
        operations.put(chatId, "first number");
        send(engine, "Введите первое число", chatId);
    }

    @CommandOther
    public void other(TelegramLongPollingEngine engine, Message message, @ParamName("chatId") Long chatId, @ParamName("userId") Long userId, UserBotSession userBotSession) {
        String text;
        MathOperation mathOperation = (MathOperation) userBotSession.getData();
        if (operations.get(chatId).equals("operator")) {
            mathOperation.setOperation(message.getText());
            mathOperation.evaluateExpression();
            repository.save(userId, mathOperation);
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

        send(engine, text, chatId);
    }

    private void send(TelegramLongPollingEngine engine, String text, Long chatId) {
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
