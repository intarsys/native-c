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
package de.intarsys.nativec.jna;

import java.util.Iterator;
import java.util.Map;

import com.sun.jna.Function;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import de.intarsys.nativec.api.INativeFunction;
import de.intarsys.nativec.api.INativeHandle;
import de.intarsys.nativec.api.INativeLibrary;

public class JnaNativeLibrary implements INativeLibrary {

  final private NativeLibrary library;

  final private JnaNativeInterface nativeInterface;

  public JnaNativeLibrary(JnaNativeInterface nativeInterface, String name,
                          Map<String, Object> options) {
    this.nativeInterface = nativeInterface;
    // this is a kludge - but i can't ask for the searchpaths already
    // registered
    for (Iterator<String> it = nativeInterface.getSearchPaths().iterator(); it
      .hasNext(); ) {
      String path = it.next();
      NativeLibrary.addSearchPath(name, path);
    }
    library = NativeLibrary.getInstance(name, options);
  }

  @Override
  public INativeFunction getFunction(String name) {
    Function function = getLibrary().getFunction(name);
    return new JnaNativeFunction(function);
  }

  @Override
  public INativeHandle getGlobal(String symbolName) {
    Pointer pointer = getLibrary().getGlobalVariableAddress(symbolName);
    return new JnaNativeHandle(pointer);
  }

  protected NativeLibrary getLibrary() {
    return library;
  }

  protected JnaNativeInterface getNativeInterface() {
    return nativeInterface;
  }
}
