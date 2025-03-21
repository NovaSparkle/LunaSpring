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

    public int min(int value, int minValue) {
        return Math.max(value, minValue);
    }

    public int max(int value, int maxValue) {
        return Math.min(value, maxValue);
    }
}
