package org.novasparkle.lunaspring.API.util.utilities;

import lombok.Getter;



public record Color(@Getter String abbr, @Getter String variable) {

    @Override
    public String toString() {
        return "Color{" +
                "abbr='" + abbr + '\'' +
                ", variable='" + variable + '\'' +
                '}';
    }

    public String toHex() {
        if (this.variable.length() == 14)
            return this.variable.replaceAll("&", "").replace("x", "#");
        else throw new RuntimeException("Тип цветового кода не соответствует, нужный формат: &x&r&r&g&g&b&b");
    }

    public String toLegacy() {
        if (this.variable.length() == 7) {
            String newString = this.variable.replace("#", "");
            StringBuilder builder = new StringBuilder("&x");
            for (char i : newString.toCharArray()) {
                builder.append('&').append(i);
            }
            return builder.toString();

        } else throw new RuntimeException("Тип цветового кода не соответствует, нужный формат: #rrggbb");
    }
}
