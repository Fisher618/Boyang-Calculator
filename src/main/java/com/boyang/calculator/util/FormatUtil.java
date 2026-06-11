package com.boyang.calculator.util;

import java.math.BigDecimal;

/**
 * 结果格式化工具类，兼容旧入口并委托给大数格式化逻辑。
 */
public final class FormatUtil {

    private FormatUtil() {
    }

    /**
     * 格式化 BigDecimal，避免科学计数法。
     */
    public static String formatDecimal(BigDecimal value) {
        return BigNumberUtil.formatDecimal(value);
    }
}
