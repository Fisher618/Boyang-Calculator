package com.boyang.calculator.engine;

import com.boyang.calculator.util.BigNumberUtil;

import java.math.BigDecimal;

/**
 * 科学计算引擎。四则、幂、平方和开方优先使用 BigDecimal。
 * 三角函数和对数函数暂时使用 Math 库近似计算。
 */
public class ScientificCalculatorEngine {

    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();

    /**
     * 使用 BigDecimal 表达式求值器计算表达式。
     */
    public String evaluateExpression(String expression) {
        return evaluator.evaluate(expression);
    }

    /**
     * 计算平方。
     */
    public String square(String value) {
        BigDecimal number = new BigDecimal(value);
        return BigNumberUtil.formatDecimal(number.multiply(number, BigNumberUtil.MC));
    }

    /**
     * 使用牛顿迭代计算 BigDecimal 平方根。
     */
    public String sqrt(String value) {
        BigDecimal number = new BigDecimal(value);
        if (number.signum() < 0) {
            return "Error: negative sqrt";
        }
        if (number.signum() == 0) {
            return "0";
        }

        BigDecimal two = new BigDecimal("2");
        BigDecimal guess = number.divide(two, BigNumberUtil.MC);
        for (int i = 0; i < 80; i++) {
            guess = guess.add(number.divide(guess, BigNumberUtil.MC), BigNumberUtil.MC)
                    .divide(two, BigNumberUtil.MC);
        }
        return BigNumberUtil.formatDecimal(guess);
    }

    /**
     * 三角和对数函数暂时使用 Math 库近似计算，不作为超大数高精度路径。
     */
    public String approximateFunction(String functionName, String value) {
        double number = new BigDecimal(value).doubleValue();
        double result = switch (functionName) {
            case "sin" -> Math.sin(number);
            case "cos" -> Math.cos(number);
            case "tan" -> Math.tan(number);
            case "ln" -> Math.log(number);
            case "log" -> Math.log10(number);
            default -> throw new IllegalArgumentException("Unsupported function: " + functionName);
        };
        return BigNumberUtil.formatDecimal(BigDecimal.valueOf(result));
    }
}
