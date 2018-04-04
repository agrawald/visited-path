package com.zuora.interview.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Utils interface for all the common business logic and utility methods and functions.
 * Created by agrawald on 4/4/18.
 */
public interface Utils {
    /**
     * {@link Comparator} for the Map based on value
     */
    Comparator<Map.Entry<String, Integer>> VALUE_COMPARATOR = Comparator.comparing(Map.Entry::getValue);
    /**
     * {@link Predicate} to check if a given User Path array is empty or not
     */
    Predicate<String[]> IS_USER_PATH_BLANK = userPath -> userPath != null && userPath.length == 2
            && userPath[0] != null && !userPath[0].isEmpty()
            && userPath[1] != null && !userPath[1].isEmpty();
    /**
     * {@link Function} to convert and entry values to an array of paths
     */
    Function<Map.Entry<String, List<String>>, List<String>> ENTRY_TO_PATH_STRING = (entry) -> {
        final List<String> paths = new ArrayList<>();
        int loop = (entry.getValue().size() > 3 ? Math.floorMod(entry.getValue().size(), 3) : 0);
        for (int i = 0; i <= loop; i++) {
            String path = "";
            for (int j = i; j < i + 3; j++) {
                if (j != i) {
                    path += "->";
                }
                path += entry.getValue().get(j);
            }
            paths.add(path);
        }
        return paths;
    };

}
