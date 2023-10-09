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

package com.teragrep.blf_01.tokenizer;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;

public class TokenizeFunction implements BiFunction<Stream, ByteBuffer, Set<Token>> {
    private final Set<Token> tokens = new HashSet<>();
    private ByteArrayOutputStream multiBuffer = new ByteArrayOutputStream();
    boolean countingMulti = false;


    @Override
    public Set<Token> apply(Stream stream, ByteBuffer buffer) {

        if (!stream.next()) {
            throw new RuntimeException("Empty stream");
        }

        byte b;
        int remaining = buffer.capacity();

        b = stream.get();

        while (remaining > 0) {

            if (countingMulti) {
                if (SplitterMatcher.multiMatch(multiBuffer.toByteArray())) {
                    writeBufferAsToken(buffer,0);
                    countingMulti = false;
                    tokens.add(new Token(ByteBuffer.wrap(multiBuffer.toByteArray())));
                    multiBuffer = new ByteArrayOutputStream();
                }

                else if (multiBuffer.size() < 5 && countingMulti) {
                    multiBuffer.write(b);
                } else {
                    buffer.put(multiBuffer.toByteArray());
                    countingMulti = false;
                    multiBuffer = new ByteArrayOutputStream();

                }
            }

            // % or -
            if (b == 37 || b == 45 && !countingMulti) {
                countingMulti = true;
                multiBuffer.write(b);
            }

            if (SplitterMatcher.singleMatch(b) && !countingMulti) {
                Token token = new Token(ByteBuffer.wrap(new byte[]{b}));
                tokens.add(token);
                writeBufferAsToken(buffer, -1);
            }

            if (!countingMulti) {
                buffer.put(b);
            }

            stream.next();

            b = stream.get();
            remaining--;
        }

        writeBufferAsToken(buffer, 0);

        return tokens;
    }

    private void writeBufferAsToken(ByteBuffer buffer, int offset) {
        buffer.flip();

        if (buffer.limit() - buffer.position() + offset < 1) {
            return;
        }

        byte[] data = new byte[buffer.limit()-buffer.position()+offset];


        buffer.get(data, 0, buffer.limit() - buffer.position() + offset);
        buffer.clear();
        tokens.add(new Token(ByteBuffer.wrap(data)));

    }

    /*
    public void checkOverlappingTokens(Stream stream, byte b, ByteBuffer buffer) {
        // stream.mark();
        multiTokenBuilder.write(b);

        for (int i = 1; i <= 5; i++) {

            if (SplitterMatcher.multiMatch(multiTokenBuilder.toByteArray())) {
                addBytesFromSplitterMark(buffer, -1);
                tokens.add(new Token(ByteBuffer.wrap(multiTokenBuilder.toByteArray())));
                // stream.reset();
                // stream.skip(i);
                splitterMark = splitterMark+i-1;
                break;
            }

            if (!stream.next()) {
                break;
            }

            b = stream.get();
            multiTokenBuilder.write(b);

        }

        multiTokenBuilder.reset();
        // stream.reset();
    }
     */
}
