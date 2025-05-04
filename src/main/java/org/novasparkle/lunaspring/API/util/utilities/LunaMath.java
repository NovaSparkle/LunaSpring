package org.novasparkle.lunaspring.API.util.utilities;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@UtilityClass
public class LunaMath {
    @Getter
    private final Random random = new Random();

    public int getIndex(int row, int col) {
        return (row - 1) * 9 + col - 1;
    }

    public int toInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int toInt(double num) {
        return (int) num;
    }

    public double toDouble(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0;
        }
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
        if (split.length < 2) split = new String[]{"0", split[0]};
        return getRandomInt(toInt(split[0]), toInt(split[1]));
    }

    public double round(double notRoundedNum, int roundLength) {
        return BigDecimal.valueOf(notRoundedNum)
                .setScale(roundLength, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
