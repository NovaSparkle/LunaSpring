package org.novasparkle.lunaspring.API.util.utilities;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.function.Function;

@UtilityClass
public class LunaMath {
    @Getter
    private final Random random = new Random();

    public int getIndex(int row, int col) {
        return (row - 1) * 9 + col - 1;
    }

    public int toInt(String text) {
        return toInt(text, 0);
    }

    public int toInt(String text, int defaultNum) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return defaultNum;
        }
    }

    public int toInt(double num) {
        return (int) num;
    }

    public byte toByte(int num) {
        return (byte) num;
    }

    public double toDouble(String text) {
        return toDouble(text, 0);
    }

    public double toDouble(String text, double defaultNum) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return defaultNum;
        }
    }

    public long toLong(String text, long defaultNum) {
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            return defaultNum;
        }
    }

    public long toLong(String text) {
        return toLong(text, 0);
    }

    public byte toByte(String text, byte defaultNum) {
        try {
            return Byte.parseByte(text);
        } catch (NumberFormatException e) {
            return defaultNum;
        }
    }

    public byte toByte(String text) {
        return toByte(text, (byte) 0);
    }

    public boolean isEven(int num) {
        return num % 2 == 0;
    }

    public int getRandomInt(int minValue, int maxValue) {
        if (minValue >= maxValue) return maxValue;
        return random.nextInt(maxValue - minValue) + minValue;
    }

    public int getRandomInt(String numerical) {
        String[] split = numerical.split("-");
        if (split.length < 2) return toInt(split[0]);
        return getRandomInt(toInt(split[0]), toInt(split[1]));
    }

    public double round(double notRoundedNum, int roundLength) {
        return roundLength <= 0 ? toInt(notRoundedNum) : BigDecimal.valueOf(notRoundedNum)
                .setScale(roundLength, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
