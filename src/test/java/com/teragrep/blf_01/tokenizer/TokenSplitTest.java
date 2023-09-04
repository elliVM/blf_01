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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenSplitTest {

    private final TokenSplit tokenSplit;

    public TokenSplitTest() {
        tokenSplit = new TokenSplit();
    }

    @Test
    @Disabled
    public void majorSimpleTest() {
        String[] tokens = tokenSplit.split("@s-d+b4s-");
        assertEquals("@s-d", tokens[0]);
        assertEquals("b4s-", tokens[1]);
    }

    @Test
    @Disabled
    public void majorMatchAndMoreTest() {
        String[] tokens = tokenSplit.split("-one--two---three-");
        assertEquals("-one", tokens[0]);
        assertEquals("two", tokens[1]);
        assertEquals("-three-", tokens[2]);
    }

    @Test
    @Disabled
    public void majorMatchLogTest() {
        String testString = "[20/Feb/2022:01:02:03.456] https-in~ abcd_backend/<NOSRV> 0/-1/-1/-1/1 503 212 - - SCNN 2/2/0/0/0 0/0 \"GET /\"";

        String[] tokens = tokenSplit.split(testString);
        assertEquals("", tokens[0]);
        assertEquals("20/Feb/2022:01:02:03.456", tokens[1]);
        assertEquals("", tokens[2]);
        assertEquals("https-in~", tokens[3]);
        assertEquals("abcd_backend/", tokens[4]);
        assertEquals("NOSRV", tokens[5]);
        assertEquals("", tokens[6]);
        assertEquals("0/-1/-1/-1/1", tokens[7]);
        assertEquals("503", tokens[8]);
        assertEquals("212", tokens[9]);
        assertEquals("-", tokens[10]);
        assertEquals("-", tokens[11]);
        assertEquals("SCNN", tokens[12]);
        assertEquals("2/2/0/0/0", tokens[13]);
        assertEquals("0/0", tokens[14]);
        assertEquals("", tokens[15]);
        assertEquals("GET", tokens[16]);
        assertEquals("/", tokens[17]);
    }
}
