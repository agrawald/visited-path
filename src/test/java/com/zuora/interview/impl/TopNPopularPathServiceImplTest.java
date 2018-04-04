package com.zuora.interview.impl;

import com.zuora.interview.TopNPopularPathService;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by agrawald on 4/4/18.
 */
public class TopNPopularPathServiceImplTest {
    private TopNPopularPathService service;

    @Test
    public void testSetup() throws Exception {
        service = new TopNPopularPathServiceImpl();
        service.setup(new String[][]{
                {"U1", "/"},
                {"U1", "login"},
                {"U1", "subscriber"},
                {"U2", "/"},
                {"U2", "login"},
                {"U2", "subscriber"},
                {"U3", "/"},
                {"U3", "login"},
                {"U3", "product"},
                {"U1", "/"},
                {"U4", "/"},
                {"U4", "login"},
                {"U4", "product"},
                {"U5", "/"},
                {"U5", "login"},
                {"U5", "subscriber"}
        });
        final Map<String, Integer> pathCounts = ((TopNPopularPathServiceImpl) service).getPathCounts();
        assertNotNull(pathCounts);
        assertEquals(3, pathCounts.size());
        assertEquals(Integer.valueOf(3), pathCounts.get("/->login->subscriber"));
        assertEquals(Integer.valueOf(2), pathCounts.get("/->login->product"));
        assertEquals(Integer.valueOf(1), pathCounts.get("login->subscriber->/"));

    }

    @Test
    public void testAmbiguousData() throws Exception {
        service = new TopNPopularPathServiceImpl();
        service.setup(new String[][]{
                {"U1", "/"},
                {"U1", "login"},
                {"U1", "subscriber"},
                {"U2", "/"},
                {"U2", "login"},
                {"U2", "subscriber"},
                {"U3", "/"},
                {"U3", "login"},
                {"U3", "product"},
                {"U1", null},
                {"", "/"},
                null,
                {"U1"},
                {"U4", "/"},
                {"U4", "login"},
                {"U4", "product"},
                {"U5", "/"},
                {"U5", "login"},
                {"U5", "subscriber"}
        });
        final Map<String, Integer> pathCounts = ((TopNPopularPathServiceImpl) service).getPathCounts();
        assertNotNull(pathCounts);
        assertEquals(2, pathCounts.size());
        assertEquals(Integer.valueOf(3), pathCounts.get("/->login->subscriber"));
        assertEquals(Integer.valueOf(2), pathCounts.get("/->login->product"));

    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyData() throws Exception {
        service = new TopNPopularPathServiceImpl();
        service.setup(null);
    }

    @Test
    public void testGetTop10PopularPaths() throws Exception {
        testSetup();
        final String[] counts = service.getTopNPopularPaths(10);
        assertNotNull(counts);
        assertEquals(3, counts.length);
        assertEquals("/->login->subscriber:3", counts[0]);
        assertEquals("/->login->product:2", counts[1]);
        assertEquals("login->subscriber->/:1", counts[2]);

    }

    @Test
    public void testGetTop2PopularPaths() throws Exception {
        testSetup();
        final String[] counts = service.getTopNPopularPaths(2);
        assertNotNull(counts);
        assertEquals(2, counts.length);
        assertEquals("/->login->subscriber:3", counts[0]);
        assertEquals("/->login->product:2", counts[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTop0PopularPaths() throws Exception {
        testSetup();
        service.getTopNPopularPaths(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTopMinus1PopularPaths() throws Exception {
        testSetup();
        service.getTopNPopularPaths(-1);
    }

}