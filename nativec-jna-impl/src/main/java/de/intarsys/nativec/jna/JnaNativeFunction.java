/*-
 * #%L
 * Nazgul Project: nativec-jna-impl
 * %%
 * Copyright (C) 2013 - 2022 Intarsys Consulting GmbH
 * %%
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
 * #L%
 */
package de.intarsys.nativec.jna;

import com.sun.jna.Function;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import de.intarsys.nativec.api.CLong;
import de.intarsys.nativec.api.CWideString;
import de.intarsys.nativec.api.INativeFunction;
import de.intarsys.nativec.api.INativeHandle;
import de.intarsys.nativec.type.INativeObject;
import de.intarsys.nativec.type.INativeType;
import de.intarsys.nativec.type.NativeObject;
import de.intarsys.nativec.type.NativeType;

public class JnaNativeFunction implements INativeFunction {

  private Function function;

  public JnaNativeFunction(Function function) {
    this.function = function;
  }

  protected Function getFunction() {
    return function;
  }

  public <T> T invoke(Class<T> returnType, Object... objects) {
    for (int i = 0; i < objects.length; i++) {
      Object object = objects[i];
      if (object instanceof INativeObject) {
        JnaNativeHandle handle = (JnaNativeHandle) ((INativeObject) object)
          .getNativeHandle();
        if (handle == null) {
          objects[i] = null;
        } else {
          objects[i] = handle.getPointer();
        }
      } else if (object instanceof INativeHandle) {
        JnaNativeHandle handle = (JnaNativeHandle) object;
        objects[i] = handle.getPointer();
      } else if (object instanceof CWideString) {
        objects[i] = new WString(((CWideString) object).getString());
      } else if (object instanceof CLong) {
        objects[i] = new NativeLong(((CLong) object).longValue());
      }
    }
    // if (Log.isLoggable(Level.FINE)) {
    // Log.log(Level.FINE, "jna invoke " + function.getName());
    // }
    T result;
    if (CWideString.class.isAssignableFrom(returnType)) {
      WString wstring = (WString) function.invoke(WString.class, objects);
      result = (T) new CWideString(wstring.toString());
    } else if (CLong.class.isAssignableFrom(returnType)) {
      NativeLong nativeLong = (NativeLong) function.invoke(
        NativeLong.class, objects);
      result = (T) new CLong(nativeLong.longValue());
    } else if (NativeObject.class.isAssignableFrom(returnType)) {
      Pointer pointer = (Pointer) function.invoke(Pointer.class, objects);
      if (pointer == null) {
        result = null;
      } else {
        INativeHandle handle = new JnaNativeHandle(pointer);
        INativeType type = NativeType.lookup(returnType);
        if (type == null) {
          throw new IllegalArgumentException("no type for '"
                                             + returnType + "'");
        }
        result = (T) type.createNative(handle);
      }
    } else if (INativeHandle.class.isAssignableFrom(returnType)) {
      Pointer pointer = (Pointer) function.invoke(Pointer.class, objects);
      if (pointer == null) {
        result = null;
      } else {
        result = (T) new JnaNativeHandle(pointer);
      }
    } else {
      result = (T) function.invoke(returnType, objects);
    }
    return result;
  }
}
