package com.zuora.interview.impl;

import com.zuora.interview.TopNPopularPathService;
import com.zuora.interview.utils.Utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by agrawald on 4/4/18.
 */
public class TopNPopularPathServiceImpl implements TopNPopularPathService {
    private final Map<String, Integer> pathCounts = new HashMap<>();

    /**
     * Getter to check the path counts. NOTE: used in test cases
     * @return Map of unique paths as {@link String} and total number of time the path visited as {@link Integer}
     */
    Map<String, Integer> getPathCounts() {
        return pathCounts;
    }

    /**
     * Function to initialize the data.
     * This function does all the heavy lifting to parse and process all the data paths
     * @param data 2-D array of {@link String}
     */
    public void setup(String[][] data) {
        // if data is empty or null then do not proceed
        if(data==null || data.length == 0) {
            throw new IllegalArgumentException("data should not be null or empty");
        }

        final ConcurrentHashMap<String, List<String>> userPaths = new ConcurrentHashMap<>();
        //Lets collect all the possible paths for each user
        Arrays.stream(data)
                .filter(Utils.IS_USER_PATH_BLANK)
                .forEach(userPath -> {
                    final List<String> steps = new ArrayList<>();
                    if (userPaths.containsKey(userPath[0])) {
                        steps.addAll(userPaths.get(userPath[0]));
                    }
                    steps.add(userPath[1]);
                    userPaths.put(userPath[0], steps);
                });
        //Now for each user lets count all the unique paths and merge the counts
        userPaths.entrySet()
                .stream()
                .map(Utils.ENTRY_TO_PATH_STRING)
                .forEach(paths -> paths.forEach(path -> {
                    int count = 1;
                    if (pathCounts.containsKey(path)) {
                        count += pathCounts.get(path);
                    }
                    pathCounts.put(path, count);
                }));
    }

    /**
     * Function to get top N popular paths
     * @param n the number of path requested
     * @return Top n paths as an array of {@link String}
     */
    public String[] getTopNPopularPaths(int n) {
        if(n<=0) {
            throw new IllegalArgumentException("n should be greater than 0.");
        }

        return pathCounts.entrySet()
                .stream()
                .sorted(Utils.VALUE_COMPARATOR.reversed())
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.toList())
                .subList(0, (pathCounts.size() >= n) ? n : pathCounts.size())
                .toArray(new String[0]);
    }
}
