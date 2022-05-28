/*-
 * #%L
 * Nazgul Project: nativec-api
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
package de.intarsys.nativec.type;

import de.intarsys.nativec.api.INativeFunction;
import de.intarsys.nativec.api.INativeHandle;
import de.intarsys.nativec.api.NativeInterface;

public class NativeFunction extends NativeVoid {

  public static final NativeFunctionType META = new NativeFunctionType();
  private Object callingConvention;
  private INativeFunction function;
  protected NativeFunction(INativeHandle handle) {
    super(handle);
    callingConvention = INativeFunction.CallingConventionCdecl;
  }

  public Object getCallingConvention() {
    return callingConvention;
  }

  public void setCallingConvention(Object callingConvention) {
    if (function != null) {
      throw new IllegalStateException();
    }
    this.callingConvention = callingConvention;
  }

  public INativeFunction getFunction() {
    // TODO do we want to check if handle has changed?
    if (function == null) {
      function = NativeInterface.get().createFunction(
        getNativeHandle().getAddress(), callingConvention);
    }
    return function;
  }

  public static class NativeFunctionType extends NativeVoidType {

    @Override
    public INativeObject createNative(INativeHandle handle) {
      return new NativeFunction(handle);
    }
  }
}
