package ru.test.command.tgbot.demobot.entity;

import lombok.Data;
import ru.test.command.tgbot.demobot.util.EvaluateExpression;

@Data
public class MathOperation {
    private Double a;
    private Double b;
    private String operation;
    private Double result;

    public void evaluateExpression() {
        try {
            result = EvaluateExpression.evaluate(a, b, operation);
        } catch (Exception e) {
            result = null;
        }
    }

    @Override
    public String toString() {
        return a + " " + operation + " " + b + " = " +
                (result == null ? "неопределенное значение" : result);
    }
}
