package org.novasparkle.lunaspring.API.util.utilities;

import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;

public class UtilsObjects {
    public final Pattern PLACEHOLDER_BRACKET_PATTERN = Pattern.compile("\\{([^}]+)}");
    public final Collection<Character> colorCollection = Set.of('&', '§');
}
