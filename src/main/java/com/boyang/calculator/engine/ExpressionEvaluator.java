package com.boyang.calculator.engine;

import com.boyang.calculator.util.BigNumberUtil;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * BigDecimal 表达式求值器，支持四则、取余、括号和整数指数。
 */
public class ExpressionEvaluator {

    /**
     * 计算表达式并返回完整十进制字符串。
     */
    public String evaluate(String expression) {
        if (expression == null || expression.isBlank()) {
            return "0";
        }

        try {
            Deque<BigDecimal> values = new ArrayDeque<>();
            Deque<Character> operators = new ArrayDeque<>();
            String normalized = expression.replace("×", "*").replace("÷", "/");

            for (int i = 0; i < normalized.length(); i++) {
                char ch = normalized.charAt(i);
                if (Character.isWhitespace(ch)) {
                    continue;
                }

                if (isNumberStart(normalized, i)) {
                    int start = i;
                    i++;
                    while (i < normalized.length() && isNumberPart(normalized.charAt(i))) {
                        i++;
                    }
                    values.push(new BigDecimal(normalized.substring(start, i)));
                    i--;
                } else if (ch == '(') {
                    operators.push(ch);
                } else if (ch == ')') {
                    while (!operators.isEmpty() && operators.peek() != '(') {
                        applyTopOperator(values, operators.pop());
                    }
                    if (operators.isEmpty() || operators.pop() != '(') {
                        return "Error: invalid expression";
                    }
                } else if (isOperator(ch)) {
                    while (!operators.isEmpty()
                            && operators.peek() != '('
                            && shouldApplyBefore(operators.peek(), ch)) {
                        applyTopOperator(values, operators.pop());
                    }
                    operators.push(ch);
                } else {
                    return "Error: invalid expression";
                }
            }

            while (!operators.isEmpty()) {
                char operator = operators.pop();
                if (operator == '(') {
                    return "Error: invalid expression";
                }
                applyTopOperator(values, operator);
            }

            if (values.size() != 1) {
                return "Error: invalid expression";
            }
            return BigNumberUtil.formatDecimal(values.pop());
        } catch (ArithmeticException exception) {
            return "Error: division by zero";
        } catch (RuntimeException exception) {
            return "Error: invalid expression";
        }
    }

    private boolean isNumberStart(String expression, int index) {
        char ch = expression.charAt(index);
        if (Character.isDigit(ch) || ch == '.') {
            return true;
        }
        if (ch != '-') {
            return false;
        }
        if (index + 1 >= expression.length()) {
            return false;
        }
        char next = expression.charAt(index + 1);
        if (!Character.isDigit(next) && next != '.') {
            return false;
        }
        int previous = index - 1;
        while (previous >= 0 && Character.isWhitespace(expression.charAt(previous))) {
            previous--;
        }
        return previous < 0 || expression.charAt(previous) == '(' || isOperator(expression.charAt(previous));
    }

    private boolean isNumberPart(char ch) {
        return Character.isDigit(ch) || ch == '.';
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '%' || ch == '^';
    }

    private boolean shouldApplyBefore(char existing, char incoming) {
        if (incoming == '^') {
            return precedence(existing) > precedence(incoming);
        }
        return precedence(existing) >= precedence(incoming);
    }

    private int precedence(char operator) {
        return switch (operator) {
            case '+', '-' -> 1;
            case '*', '/', '%' -> 2;
            case '^' -> 3;
            default -> 0;
        };
    }

    private void applyTopOperator(Deque<BigDecimal> values, char operator) {
        if (values.size() < 2) {
            throw new IllegalArgumentException("Missing operand.");
        }
        BigDecimal right = values.pop();
        BigDecimal left = values.pop();
        BigDecimal result = switch (operator) {
            case '+' -> left.add(right);
            case '-' -> left.subtract(right);
            case '*' -> left.multiply(right);
            case '/' -> BigNumberUtil.safeDivide(left, right);
            case '%' -> left.remainder(right);
            case '^' -> pow(left, right);
            default -> throw new IllegalArgumentException("Unsupported operator.");
        };
        values.push(result);
    }

    private BigDecimal pow(BigDecimal base, BigDecimal exponent) {
        if (!BigNumberUtil.isInteger(exponent)) {
            throw new IllegalArgumentException("Exponent must be an integer.");
        }
        int power = exponent.intValueExact();
        if (power < 0) {
            return BigNumberUtil.safeDivide(BigDecimal.ONE, base.pow(Math.abs(power), BigNumberUtil.MC));
        }
        return base.pow(power);
    }
}
