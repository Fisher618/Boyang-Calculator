package com.boyang.calculator.engine;

import com.boyang.calculator.util.BigNumberUtil;

import java.math.BigDecimal;

/**
 * BigDecimal expression evaluator supporting arithmetic, parentheses and
 * scientific functions.
 */
public class ExpressionEvaluator {

    public String evaluate(String expression) {
        if (expression == null || expression.isBlank()) {
            return "0";
        }

        try {
            Parser parser = new Parser(expression.replace("×", "*").replace("÷", "/"));
            BigDecimal result = parser.parseExpression();
            parser.skipWhitespace();
            if (!parser.isAtEnd()) {
                throw new IllegalArgumentException("Unexpected input.");
            }
            return BigNumberUtil.formatDecimal(result);
        } catch (ArithmeticException exception) {
            return "Error: division by zero";
        } catch (RuntimeException exception) {
            return "Error: invalid expression";
        }
    }

    private static final class Parser {
        private final String expression;
        private int position;

        private Parser(String expression) {
            this.expression = expression;
        }

        private BigDecimal parseExpression() {
            BigDecimal value = parseTerm();
            while (true) {
                if (match('+')) {
                    value = value.add(parseTerm());
                } else if (match('-')) {
                    value = value.subtract(parseTerm());
                } else {
                    return value;
                }
            }
        }

        private BigDecimal parseTerm() {
            BigDecimal value = parseUnary();
            while (true) {
                if (match('*')) {
                    value = value.multiply(parseUnary());
                } else if (match('/')) {
                    value = BigNumberUtil.safeDivide(value, parseUnary());
                } else if (match('%')) {
                    value = value.remainder(parseUnary());
                } else {
                    return value;
                }
            }
        }

        private BigDecimal parseUnary() {
            if (match('+')) {
                return parseUnary();
            }
            if (match('-')) {
                return parseUnary().negate();
            }
            return parsePower();
        }

        private BigDecimal parsePower() {
            BigDecimal base = parsePrimary();
            if (match('^')) {
                return pow(base, parseUnary());
            }
            return base;
        }

        private BigDecimal parsePrimary() {
            skipWhitespace();
            if (match('(')) {
                BigDecimal value = parseExpression();
                require(')');
                return value;
            }
            if (position < expression.length() && Character.isLetter(expression.charAt(position))) {
                String functionName = parseIdentifier();
                require('(');
                BigDecimal argument = parseExpression();
                require(')');
                return applyFunction(functionName, argument);
            }
            return parseNumber();
        }

        private BigDecimal parseNumber() {
            skipWhitespace();
            int start = position;
            boolean decimalSeen = false;
            while (position < expression.length()) {
                char ch = expression.charAt(position);
                if (Character.isDigit(ch)) {
                    position++;
                } else if (ch == '.' && !decimalSeen) {
                    decimalSeen = true;
                    position++;
                } else {
                    break;
                }
            }
            if (start == position) {
                throw new IllegalArgumentException("Number expected.");
            }
            return new BigDecimal(expression.substring(start, position));
        }

        private String parseIdentifier() {
            int start = position;
            while (position < expression.length() && Character.isLetter(expression.charAt(position))) {
                position++;
            }
            return expression.substring(start, position);
        }

        private BigDecimal applyFunction(String name, BigDecimal argument) {
            if ("sqrt".equals(name)) {
                if (argument.signum() < 0) {
                    throw new IllegalArgumentException("Negative square root.");
                }
                return argument.sqrt(BigNumberUtil.MC);
            }

            double number = argument.doubleValue();
            double result = switch (name) {
                case "sin" -> Math.sin(number);
                case "cos" -> Math.cos(number);
                case "tan" -> Math.tan(number);
                case "ln" -> Math.log(number);
                case "log" -> Math.log10(number);
                default -> throw new IllegalArgumentException("Unsupported function.");
            };
            if (!Double.isFinite(result)) {
                throw new IllegalArgumentException("Function result is not finite.");
            }
            return BigDecimal.valueOf(result);
        }

        private BigDecimal pow(BigDecimal base, BigDecimal exponent) {
            if (!BigNumberUtil.isInteger(exponent)) {
                throw new IllegalArgumentException("Exponent must be an integer.");
            }
            int power = exponent.intValueExact();
            if (power < 0) {
                return BigNumberUtil.safeDivide(BigDecimal.ONE, base.pow(Math.abs(power), BigNumberUtil.MC));
            }
            return base.pow(power, BigNumberUtil.MC);
        }

        private boolean match(char expected) {
            skipWhitespace();
            if (position < expression.length() && expression.charAt(position) == expected) {
                position++;
                return true;
            }
            return false;
        }

        private void require(char expected) {
            if (!match(expected)) {
                throw new IllegalArgumentException("Missing " + expected);
            }
        }

        private void skipWhitespace() {
            while (position < expression.length() && Character.isWhitespace(expression.charAt(position))) {
                position++;
            }
        }

        private boolean isAtEnd() {
            return position >= expression.length();
        }
    }
}
