package org.novasparkle.lunaspring.API.util.utilities;

import lombok.Getter;


@Getter
public record Color(String abbr, String variable) {

    @Override
    public String toString() {
        return "Color{" +
                "abbr='" + abbr + '\'' +
                ", variable='" + variable + '\'' +
                '}';
    }

    public String toHex() {
        if (this.isLegacy())
            return this.variable.replaceAll("&", "").replace("x", "#");
        return "";
    }

    public boolean isLegacy() {
        return this.variable.matches("^&x(&[0-9A-Fa-f]){6}$");
    }

    public String toLegacy() {
        if (this.variable.length() == 7) {
            String newString = this.variable.replace("#", "");
            StringBuilder builder = new StringBuilder("&x");
            for (char i : newString.toCharArray()) {
                builder.append('&').append(i);
            }
            return builder.toString();

        }
        return "";
    }
}
