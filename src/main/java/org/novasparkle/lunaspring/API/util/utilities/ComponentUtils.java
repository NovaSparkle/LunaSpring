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

        int mode = 0;
        String buttonText = null;

        for (String part : parts) {
            switch (mode) {
                case 0:
                    builder.append(TextComponent.fromLegacyText(part));
                    mode = 1;
                    break;

                case 1:
                    buttonText = part;
                    mode = 2;
                    break;

                case 2:
                    if (buttonText != null) {
                        for (BaseComponent baseComponent : TextComponent.fromLegacyText(buttonText)) {
                            baseComponent.setClickEvent(new ClickEvent(action, part));
                            builder.append(baseComponent);
                        }
                    }
                    buttonText = null;
                    mode = 0;
                    break;
            }
        }

        return builder.create();
    }
    public BaseComponent[] createHoverableText(String line) {

        String message = ColorManager.color(line);

        ComponentBuilder builder = new ComponentBuilder();

        String[] parts = message.split("\\*%\\*", -1);

        int mode = 0;
        String buttonText = null;

        for (String part : parts) {
            switch (mode) {
                case 0:
                    builder.append(TextComponent.fromLegacyText(part));
                    mode = 1;
                    break;

                case 1:
                    buttonText = part;
                    mode = 2;
                    break;

                case 2:
                    if (buttonText != null) {
                        for (BaseComponent baseComponent : TextComponent.fromLegacyText(buttonText)) {
                            baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(part)));
                            builder.append(baseComponent);
                        }
                    }
                    buttonText = null;
                    mode = 0;
                    break;
            }
        }
        return builder.create();
    }
}
