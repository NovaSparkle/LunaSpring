package org.novasparkle.lunaspring.Util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LunaMath {
    public int min(int value, int minValue) {
        return Math.max(value, minValue);
    }

    public int max(int value, int maxValue) {
        return Math.min(value, maxValue);
    }
}
