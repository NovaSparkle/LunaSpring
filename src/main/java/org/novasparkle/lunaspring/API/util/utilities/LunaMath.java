package org.novasparkle.lunaspring.API.util.utilities;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class LunaMath {

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

    public float toFloat(String text, float defaultNum) {
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return defaultNum;
        }
    }

    public float toFloat(String text) {
        return toFloat(text, 0f);
    }

    public boolean isEven(int num) {
        return num % 2 == 0;
    }

    public int getRandomInt(int minValue, int maxValue) {
        if (minValue >= maxValue) return maxValue;
        return ThreadLocalRandom.current().nextInt(maxValue - minValue) + minValue;
    }

    public int getRandomInt(String numerical, String splitRegex) {
        String[] split = numerical.split(splitRegex);
        if (split.length < 2) return toInt(split[0]);
        return getRandomInt(toInt(split[0]), toInt(split[1]));
    }

    public int getRandomInt(String numerical) {
        return getRandomInt(numerical, "-");
    }

    public double getRandomDouble(double minValue, double maxValue) {
        if (minValue >= maxValue) return maxValue;
        return minValue + Math.random() * (maxValue - minValue);
    }

    public double getRandomDouble(String numerical, String splitRegex) {
        String[] split = numerical.split(splitRegex);
        if (split.length < 2) return toDouble(split[0]);
        return getRandomDouble(toDouble(split[0]), toDouble(split[1]));
    }

    public double getRandomDouble(String numerical) {
        return getRandomDouble(numerical, "-");
    }

    public @Nullable <T> T getRandom(@Nullable List<T> collection) {
        if (collection == null || collection.isEmpty()) return null;
        if (collection.size() == 1) return collection.get(0);
        return collection.get(getRandomInt(0, collection.size()));
    }

    public @NotNull <T> T getRandomIfPresent(@Nullable List<T> collection, @NotNull Returner<T> returner) {
        T t = getRandom(collection);
        return t == null ? returner.apply() : t;
    }

    public double round(double notRoundedNum, int roundLength) {
        if (roundLength < 0) roundLength = 0;
        return BigDecimal.valueOf(notRoundedNum)
                .setScale(roundLength, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
