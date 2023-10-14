package ru.test.command.tgbot.demobot.util;

public class ParseNumber {
    public static Double parseNumber(String a) throws Exception {
        try {
            return Double.parseDouble(a);
        } catch (NumberFormatException e) {
            throw new Exception(e);
        }
    }
}
