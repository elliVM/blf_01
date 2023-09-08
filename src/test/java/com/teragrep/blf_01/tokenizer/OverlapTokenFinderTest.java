package com.teragrep.blf_01.tokenizer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OverlapTokenFinderTest {

    @Test
    public void findTokensTest() {
        OverlapTokenFinder finder = new OverlapTokenFinder();

        String input = "%20_afs.%25260/df%3A\"";

        Set<String> tokenSet = new HashSet<>(Arrays.asList(finder.find(input)));
        HashSet<String> expectedSet = new HashSet<>();
        expectedSet.add("%20");
        expectedSet.add("20");
        expectedSet.add("%3A");
        expectedSet.add("3A");
        expectedSet.add("%2526");
        expectedSet.add("2526");
        expectedSet.add(".");
        expectedSet.add("_");
        expectedSet.add("/");
        expectedSet.add("%");

        assertEquals(expectedSet, tokenSet);

    }
}
