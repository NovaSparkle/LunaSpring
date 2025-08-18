package org.novasparkle.lunaspring.API.util.utilities;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ComponentUtils {
    public BaseComponent[] createClickableText(String line, ClickEvent.Action action) {

        // {S}Игрок {M}[player] {S}пригласил вас в клан [clanPrefix]{S}. *%*{M}Принять*%*/clan accept [clanTag]*%*

        String message = ColorManager.color(line);

        ComponentBuilder builder = new ComponentBuilder();

        Pattern hexPattern = Pattern.compile("(?i)(§x(§[0-9a-fA-F]){6})");
        Matcher hexMatcher = hexPattern.matcher(message);


        String[] parts = message.split("\\*%\\*", -1);

        for (int i = 0; i < parts.length; i++) {
            if (i % 2 == 0) {
                builder.append(parse(hexMatcher, parts[i], null, null, null));

            } else if (i + 1 < parts.length) {
                String clickText = parts[i];
                String command = parts[i + 1];
                i += 2;
                builder.append(parse(hexMatcher, clickText, action, null, command));
            }
        }
        return builder.create();
    }
    public BaseComponent[] createHoverableText(String line) {

        // {S}Игрок {M}[player] {S}пригласил вас в клан [clanPrefix]{S}. *%*{M}Принять*%*/clan accept [clanTag]*%*

        String message = ColorManager.color(line);

        ComponentBuilder builder = new ComponentBuilder();

        Pattern hexPattern = Pattern.compile("(?i)(§x(§[0-9a-fA-F]){6})");
        Matcher hexMatcher = hexPattern.matcher(message);


        String[] parts = message.split("\\*%\\*", -1);

        for (int i = 0; i < parts.length; i++) {
            if (i % 2 == 0) {
                builder.append(parse(hexMatcher, parts[i], null, null, null));

            } else if (i + 1 < parts.length) {
                String clickText = parts[i];
                String command = parts[i + 1];
                i += 2;
                builder.append(parse(hexMatcher, clickText, null, HoverEvent.Action.SHOW_TEXT, command));
            }
        }
        return builder.create();
    }

    public static BaseComponent[] parse(Matcher hexMatcher, String input, ClickEvent.Action clickAction, HoverEvent.Action hoverAction, String value) {
        List<BaseComponent> components = new ArrayList<>();

        String translated = ChatColor.translateAlternateColorCodes('&', input);


        int lastPos = 0;

        while (hexMatcher.find()) {
            int hexStart = hexMatcher.start();

            if (hexStart > lastPos) {
                String beforeHex = translated.substring(lastPos, hexStart);
                appendStyledText(components, beforeHex);
            }

            String hexCode = hexMatcher.group()
                    .replace("§", "")
                    .replace("x", "#");

            int hexEnd = hexMatcher.end();
            int nextHexPos = translated.length();
            if (hexMatcher.find()) {
                nextHexPos = hexMatcher.start();
                hexMatcher.reset();
            }

            String afterHex = translated.substring(hexEnd, nextHexPos);

            TextComponent hexComponent = new TextComponent(afterHex);
            hexComponent.setColor(ChatColor.of(hexCode));
            if (clickAction != null) {
                hexComponent.setClickEvent(new ClickEvent(clickAction, value));
            }
            if (hoverAction != null) {
                hexComponent.setHoverEvent(new HoverEvent(hoverAction, new Text(value)));
            }
            components.add(hexComponent);

            lastPos = nextHexPos;
        }


        if (lastPos < translated.length()) {
            String remaining = translated.substring(lastPos);
            appendStyledText(components, remaining);
        }

        return components.toArray(new BaseComponent[0]);
    }


    private static void appendStyledText(List<BaseComponent> components, String text) {
        if (text.isEmpty()) return;

        TextComponent current = new TextComponent();
        current.setText(text);
        components.add(current);
    }
}
