package org.novasparkle.lunaspring.API.Util.utilities;

import lombok.Getter;
import org.novasparkle.lunaspring.self.LSCommand;

@Getter
public final class Color {
    private final String abbr;
    @Getter
    private final String variable;
    public Color(String abbr, String variable) {
        this.abbr = abbr;
        this.variable = variable;
    }

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
