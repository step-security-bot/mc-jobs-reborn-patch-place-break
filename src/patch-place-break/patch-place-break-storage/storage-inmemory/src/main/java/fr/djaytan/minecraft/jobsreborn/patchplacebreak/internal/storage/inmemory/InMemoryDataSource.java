/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.inmemory;

import javax.inject.Singleton;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.DataSource;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.internal.storage.api.StorageException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class InMemoryDataSource implements DataSource {

  @Override
  public void connect() throws StorageException {
    log.atInfo().log("Nothing to do when connecting to the InMemory datasource.");
  }

  @Override
  public void disconnect() throws StorageException {
    log.atInfo().log("Nothing to do when disconnecting from the InMemory datasource.");
  }
}
