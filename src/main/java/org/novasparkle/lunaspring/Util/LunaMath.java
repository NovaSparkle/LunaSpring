package org.novasparkle.lunaspring.Util;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class LunaMath {
    @Getter
    private final Random random = new Random();

    public int getIndex(int row, int col) {
        return (row - 1) * 9 + col - 1;
    }

    public int toInt(String text) {
        return Integer.parseInt(text);
    }

    public int toInt(double num) {
        return (int) num;
    }

    public int min(int value, int minValue) {
        return Math.max(value, minValue);
    }

    public int max(int value, int maxValue) {
        return Math.min(value, maxValue);
    }

    public int getRandomInt(int minValue, int maxValue) {
        return min(random.nextInt(maxValue - minValue) + minValue, minValue);
    }

    public int getRandomInt(String numerical) {
        String[] split = numerical.split("-");
        if (split.length < 2) split = new String[]{"0", split[0]};
        return getRandomInt(toInt(split[0]), toInt(split[1]));
    }

    public double round(double notRoundedNum, int roundLength) {
        String formater = "%." + roundLength + "f";
        return Double.parseDouble(String.format(formater, notRoundedNum));
    }
}
