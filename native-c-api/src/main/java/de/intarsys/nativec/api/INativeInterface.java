/*
 * Copyright (c) 2008, intarsys consulting GmbH
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * - Neither the name of intarsys nor the names of its contributors may be used
 *   to endorse or promote products derived from this software without specific
 *   prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package de.intarsys.nativec.api;

/**
 * The abstraction of a generic interface to c native code.
 */
public interface INativeInterface {

  /**
   * Add a directory to the search path.
   *
   * @param path The path to be added;
   */
  void addSearchPath(String path);

  /**
   * Allocate c memory and return the respective {@link INativeHandle}.
   *
   * @param size The size in bytes.
   *
   * @return The new allocated {@link INativeHandle}
   */
  INativeHandle allocate(int size);

  INativeCallback createCallback(ICallback callback);

  /**
   * Create an {@link INativeFunction} from a function pointer.
   * <p>
   * There is no special handling for the 0 address!
   *
   * @param address The function pointer.
   *
   * @return The function object.
   */
  INativeFunction createFunction(long address);

  INativeFunction createFunction(long address, Object callingConvention);

  /**
   * Create a void {@link INativeHandle} to a memory address.
   * <p>
   * There is no special handling for the 0 address!
   *
   * @param address The memory address.
   *
   * @return The handle to the memory address.
   */
  INativeHandle createHandle(long address);

  /**
   * Load a new {@link INativeLibrary}.
   *
   * @param name The name of the library to load.
   *
   * @return The new {@link INativeLibrary}
   */
  INativeLibrary createLibrary(String name);

  /**
   * Load a new {@link INativeLibrary}.
   *
   * @param name              The name of the library to load.
   * @param callingConvention The calling convention to use as default for functions in this
   *                          library.
   *
   * @return The new {@link INativeLibrary}
   */
  INativeLibrary createLibrary(String name, Object callingConvention);

  /**
   * The platform long size.
   *
   * @return The platform long size.
   */
  int longSize();

  /**
   * The platform pointer size.
   *
   * @return The platform pointer size.
   */
  int pointerSize();

  /**
   * The platform wide char size.
   *
   * @return The platform wide char size.
   */
  int wideCharSize();
}
