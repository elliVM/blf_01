package com.teragrep.blf_01.tokenizer;

/*
 * Teragrep Bloom Filter Library BLF-01
 * Copyright (C) 2019, 2020, 2021, 2022  Suomen Kanuuna Oy
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://github.com/teragrep/teragrep/blob/main/LICENSE>.
 *
 *
 * Additional permission under GNU Affero General Public License version 3
 * section 7
 *
 * If you modify this Program, or any covered work, by linking or combining it
 * with other code, such other code is not for that reason alone subject to any
 * of the requirements of the GNU Affero GPL version 3 as long as this Program
 * is the same Program as licensed from Suomen Kanuuna Oy without any additional
 * modifications.
 *
 * Supplemented terms under GNU Affero General Public License version 3
 * section 7
 *
 * Origin of the software must be attributed to Suomen Kanuuna Oy. Any modified
 * versions must be marked as "Modified version of" The Program.
 *
 * Names of the licensors and authors may not be used for publicity purposes.
 *
 * No rights are granted for use of trade names, trademarks, or service marks
 * which are in The Program if any.
 *
 * Licensee must indemnify licensors and authors for any liability that these
 * contractual assumptions impose on licensors and authors.
 *
 * To the extent this program is licensed as part of the Commercial versions of
 * Teragrep, the applicable Commercial License may apply to this file if you as
 * a licensee so wish it.
 */

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenizerTest {

    @Test
    @Disabled
    public void testTokenization() {
        Tokenizer tokenizer = new Tokenizer();
        String testString = "[20/Feb/2022:01:02:03.456]%20 https-in~ abcd_backend/<NOSRV> 0/-1/-1/-1/1 503 212 - - SCNN 2/2/0/0/0 0/0 \"GET /\"";
        HashSet<String> tokenizedSet = tokenizer.tokenize(testString);

        HashSet<String> expectedSet = new HashSet<>();
        expectedSet.add("");
        expectedSet.add("01");
        expectedSet.add("02");
        expectedSet.add("0/-1/-1/-1/1");
        expectedSet.add("03");
        expectedSet.add("NOSRV");
        expectedSet.add("0/0");
        expectedSet.add("212");
        expectedSet.add("456");
        expectedSet.add("abcd_backend/");
        expectedSet.add("backend");
        expectedSet.add("https");
        expectedSet.add("20/Feb/2022:01:02:03.456");
        expectedSet.add("2/2/0/0/0");
        expectedSet.add("Feb");
        expectedSet.add("https-in~");
        expectedSet.add("2022");
        expectedSet.add("-");
        expectedSet.add("/");
        expectedSet.add("abcd");
        expectedSet.add("0");
        expectedSet.add("1");
        expectedSet.add("2");
        expectedSet.add("SCNN");
        expectedSet.add("GET");
        expectedSet.add("503");
        expectedSet.add("in~");
        expectedSet.add("20");

        assertTrue(tokenizedSet.containsAll(expectedSet));
    }
    @Test
    public void testMoreTokenisation() {
        Tokenizer tokenizer = new Tokenizer();

        final String input = "127.0.0.1:12345 [24/Aug/2023:18:02:31.208] " +
                "httpfront_review.example.com_https~ " +
                "- " +
                "\"GET /changes/?O=a&q=project%3Atesting%20change%3AI6ba1429859a5d6d65a06ae8ae5c3a8f92b111239%20-change%3A8%20-is%3Aabandoned HTTP/1.1\"";

        HashSet<String> expectedSet = new HashSet<>();

        expectedSet.add("");
        expectedSet.add("127");
        expectedSet.add(".");
        expectedSet.add("0");
        expectedSet.add("1");
        expectedSet.add(":");
        expectedSet.add("12345");
        expectedSet.add(" ");
        expectedSet.add("[");
        expectedSet.add("24");
        expectedSet.add("/");
        expectedSet.add("Aug");
        expectedSet.add("2023");
        expectedSet.add("18");
        expectedSet.add("02");
        expectedSet.add("31");
        expectedSet.add("208");
        expectedSet.add("]");
        expectedSet.add("httpfront");
        expectedSet.add("_");
        expectedSet.add("review");
        expectedSet.add("example");
        expectedSet.add("com");
        expectedSet.add("https~");
        expectedSet.add("-");
        expectedSet.add("\"");
        expectedSet.add("GET");
        expectedSet.add("changes");
        expectedSet.add("?");
        expectedSet.add("=");
        expectedSet.add("a");
        expectedSet.add("&");
        expectedSet.add("q");
        expectedSet.add("O");
        expectedSet.add("project");
        expectedSet.add("%3A");
        expectedSet.add("%"); // part of %3A
        expectedSet.add("3A"); // part of %3A
        expectedSet.add("testing");
        expectedSet.add("%20"); // part of
        expectedSet.add("20"); // part of %20
        expectedSet.add("change");
        expectedSet.add("I6ba1429859a5d6d65a06ae8ae5c3a8f92b111239");
        expectedSet.add("8");
        expectedSet.add("is");
        expectedSet.add("abandoned");
        expectedSet.add("HTTP");

        HashSet<String> tokenizedSet = tokenizer.tokenize(input);

        assertEquals(expectedSet, tokenizedSet);

    }

}
