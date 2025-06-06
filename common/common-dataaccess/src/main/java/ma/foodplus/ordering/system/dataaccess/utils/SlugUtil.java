package ma.foodplus.ordering.system.dataaccess.utils;

import java.util.Locale;

public class SlugUtil {
    public static String toSlug(String input) {
        return input
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }
}
