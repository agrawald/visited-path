package com.zuora.interview;

/**
 * Created by agrawald on 4/4/18.
 */
public interface TopNPopularPathService {
    void setup(String[][] data);

    String[] getTopNPopularPaths(int n);
}