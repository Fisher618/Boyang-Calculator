package com.boyang.calculator.engine;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExpressionEvaluatorTest {

    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();

    @Test
    void evaluatesSquareRootInsideLargerExpression() {
        BigDecimal result = new BigDecimal(evaluator.evaluate("8 * sqrt(3)"));

        assertTrue(result.subtract(new BigDecimal("13.856406460551018"))
                .abs()
                .compareTo(new BigDecimal("0.000000000000001")) < 0);
    }

    @Test
    void evaluatesNestedScientificFunctions() {
        assertEquals("3", evaluator.evaluate("sqrt(1 + 8)"));
        assertEquals("1", evaluator.evaluate("sqrt(9)^2 / 9"));
    }

    @Test
    void keepsExponentRightAssociative() {
        assertEquals("512", evaluator.evaluate("2^3^2"));
    }

    @Test
    void rejectsIncompleteFunctionExpression() {
        assertEquals("Error: invalid expression", evaluator.evaluate("8 * sqrt("));
    }
}
