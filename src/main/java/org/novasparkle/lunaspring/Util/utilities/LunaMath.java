package org.novasparkle.lunaspring.Util.utilities;

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

    public int getRandomInt(int minValue, int maxValue) {
        return random.nextInt(maxValue - minValue) + minValue;
    }

    public int getRandomInt(String numerical) {
        String[] split = numerical.split("-");
        if (split.length < 2) split = new String[]{"0", split[0]};
        return getRandomInt(toInt(split[0]), toInt(split[1]));
    }

    public double round(double notRoundedNum, int roundLength) {
        String pattern = "%." + roundLength + "f";
        return Double.parseDouble(String.format(pattern, notRoundedNum));
    }
}
