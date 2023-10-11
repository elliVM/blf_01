/*
 * Teragrep Bloom Filter Library BLF-01
 * Copyright (C) 2019, 2020, 2021, 2022, 2023 Suomen Kanuuna Oy
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

package com.teragrep.blf_01;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class PerformanceTest {

    @Test
    public void testAll() throws IOException {

        Instant start = Instant.now();
        TokenScan majorTokenScan = new TokenScan(new MajorDelimiters());

        FileInputStream bais = new FileInputStream("src/test/resources/base64.txt");
        Stream stream = new Stream();
        stream.setInputStream(bais);

        ArrayList<Token> majorTokens = majorTokenScan.findBy(stream);

        ArrayList<Token> allTokens = new ArrayList<>(majorTokens);
        Delimiters minorDelimiters = new MinorDelimiters();

        TokenScan minorTokenScan = new TokenScan(minorDelimiters);
        Entanglement entanglement = new Entanglement();
        for (Token token : majorTokens) {
            ByteArrayInputStream tokenBais = new ByteArrayInputStream(token.bytes);

            stream.setInputStream(tokenBais);

            ArrayList<Token> minorTokens = minorTokenScan.findBy(stream);


            allTokens.addAll(entanglement.entangle(minorTokens));
        }
        Instant end = Instant.now();
        float duration = (float) ChronoUnit.MILLIS.between(start, end)/1000;
        System.out.println("Time taken: " + duration + " seconds");
        System.out.println("Tokens: " + allTokens.size() + " (" + allTokens.size()/duration + "/s)");
        System.out.println("Data size: " + bais.getChannel().size() + " (" + bais.getChannel().size()/duration + "/s)");
    }

    @Test
    public void testAllBig() throws IOException {

        Instant start = Instant.now();
        TokenScan majorTokenScan = new TokenScan(new MajorDelimiters());

        FileInputStream bais = new FileInputStream("src/test/resources/base64-8m.txt");
        Stream stream = new Stream();
        stream.setInputStream(bais);

        ArrayList<Token> majorTokens = majorTokenScan.findBy(stream);

        ArrayList<Token> allTokens = new ArrayList<>(majorTokens);
        Delimiters minorDelimiters = new MinorDelimiters();
        TokenScan minorTokenScan = new TokenScan(minorDelimiters);
        Entanglement entanglement = new Entanglement();
        for (Token token : majorTokens) {
            ByteArrayInputStream tokenBais = new ByteArrayInputStream(token.bytes);

            stream.setInputStream(tokenBais);


            ArrayList<Token> minorTokens = minorTokenScan.findBy(stream);


            allTokens.addAll(entanglement.entangle(minorTokens));
        }
        Instant end = Instant.now();
        float duration = (float) ChronoUnit.MILLIS.between(start, end)/1000;
        System.out.println("Time taken: " + duration + " seconds");
        System.out.println("Tokens: " + allTokens.size() + " (" + allTokens.size()/duration + "/s)");
        System.out.println("Data size: " + bais.getChannel().size() + " (" + bais.getChannel().size()/duration + "/s)");
    }

    @Test
    public void testSmallDelimiters() {

        Instant start = Instant.now();
        String input = new String(new char[64]).replace("\0", "#");
        TokenScan majorTokenScan = new TokenScan(new MajorDelimiters());

        Stream stream = new Stream();
        stream.setInputStream(new ByteArrayInputStream(input.getBytes()));

        ArrayList<Token> majorTokens = majorTokenScan.findBy(stream);

        ArrayList<Token> allTokens = new ArrayList<>(majorTokens);
        Entanglement entanglement = new Entanglement();

        for (Token token : majorTokens) {
            ByteArrayInputStream tokenBais = new ByteArrayInputStream(token.bytes);

            stream.setInputStream(tokenBais);

            TokenScan minorTokenScan = new TokenScan(new MinorDelimiters());

            ArrayList<Token> minorTokens = minorTokenScan.findBy(stream);


            ArrayList<Token> tokenized = entanglement.entangle(minorTokens);
            allTokens.addAll(tokenized);
        }
        Instant end = Instant.now();
        float duration = (float) ChronoUnit.MICROS.between(start, end)/1_000_000;
        System.out.println("Time taken: " + duration + " seconds");
        System.out.println("Tokens: " + allTokens.size() + " (" + allTokens.size()/duration + "/s)");
        System.out.println("Data size: " + input.length() + " (" + input.length()/duration + "/s)");
    }

    @Test
    public void testSmallCharacters() {

        String input = new String(new char[128*1024]).replace("\0", "X");
        Instant start = Instant.now();
        TokenScan majorTokenScan = new TokenScan(new MajorDelimiters());

        Stream stream = new Stream();
        stream.setInputStream(new ByteArrayInputStream(input.getBytes()));

        ArrayList<Token> majorTokens = majorTokenScan.findBy(stream);

        ArrayList<Token> allTokens = new ArrayList<>(majorTokens);
        Entanglement entanglement = new Entanglement();
        for (Token token : majorTokens) {
            ByteArrayInputStream tokenBais = new ByteArrayInputStream(token.bytes);

            stream.setInputStream(tokenBais);

            TokenScan minorTokenScan = new TokenScan(new MinorDelimiters());

            ArrayList<Token> minorTokens = minorTokenScan.findBy(stream);

            ArrayList<Token> tokenized = entanglement.entangle(minorTokens);
            allTokens.addAll(tokenized);
        }
        Instant end = Instant.now();
        float duration = (float) ChronoUnit.MICROS.between(start, end)/1_000_000;
        System.out.println("Time taken: " + duration + " seconds");
        System.out.println("Tokens: " + allTokens.size() + " (" + allTokens.size()/duration + "/s)");
        System.out.println("Data size: " + input.length() + " (" + input.length()/duration + "/s)");
    }
}
