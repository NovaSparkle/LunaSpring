package org.novasparkle.lunaspring.API.util.utilities;

import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.novasparkle.lunaspring.API.util.service.managers.ColorManager;

@UtilityClass
public class ComponentUtils {
    public BaseComponent[] createClickableText(String line, ClickEvent.Action action) {
        String message = ColorManager.color(line);

        ComponentBuilder builder = new ComponentBuilder();

        String[] parts = message.split("\\*%\\*", -1);

        for (int i = 0; i < parts.length; i++) {
            String text = parts[i];
            if (i % 2 == 0) {
                builder.append(TextComponent.fromLegacyText(text));

            } else if (i + 1 < parts.length) {
                String command = parts[i + 1];
                i += 2;
                for (BaseComponent baseComponent : TextComponent.fromLegacyText(text)) {
                    baseComponent.setClickEvent(new ClickEvent(action, command));
                    builder.append(baseComponent);
                }
            }
        }
        return builder.create();
    }
    public BaseComponent[] createHoverableText(String line) {

        String message = ColorManager.color(line);

        ComponentBuilder builder = new ComponentBuilder();

        String[] parts = message.split("\\*%\\*", -1);

        for (int i = 0; i < parts.length; i++) {
            String text = parts[i];
            if (i % 2 == 0) {
                builder.append(TextComponent.fromLegacyText(text));

            } else if (i + 1 < parts.length) {
                String textToShow = parts[i + 1];
                i += 2;

                for (BaseComponent baseComponent : TextComponent.fromLegacyText(text)) {
                    baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(textToShow)));
                    builder.append(baseComponent);
                }
            }
        }
        return builder.create();
    }
}
