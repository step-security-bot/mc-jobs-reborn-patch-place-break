/*
 * The MIT License
 * Copyright © 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.djaytan.mc.jrppb.api.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class TagTest {

  @Test
  void whenInstantiatingWithNominalValues_shouldMatchExpectedValues() {
    // Given
    String worldName = "world";
    int x = 52;
    int y = 68;
    int z = 1254;
    BlockLocation blockLocation = new BlockLocation(worldName, x, y, z);

    boolean isEphemeral = true;
    LocalDateTime localDateTime = LocalDateTime.now();

    // When
    Tag tag = new Tag(blockLocation, isEphemeral, localDateTime);

    // Then
    assertAll(
        () -> assertThat(tag.createdAt()).isEqualTo(localDateTime),
        () -> assertThat(tag.isEphemeral()).isEqualTo(isEphemeral),
        () -> assertThat(tag.blockLocation()).isEqualTo(blockLocation));
  }
}
