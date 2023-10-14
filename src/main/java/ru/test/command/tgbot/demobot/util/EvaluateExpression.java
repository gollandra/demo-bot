package ru.test.command.tgbot.demobot.util;

public class EvaluateExpression {
    public static Double evaluate(Double a, Double b, String operation) throws Exception {
        try {
            switch (operation) {
                case "+":
                    return a + b;
                case "-":
                    return a - b;
                case "*":
                    return a * b;
                case "/":
                    return a / b;
            }
        } catch (ArithmeticException e) {
            throw new Exception(e);
        }
        return null;
    }
}
