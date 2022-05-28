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

import de.intarsys.nativec.api.INativeHandle;
import de.intarsys.nativec.api.NativeTools;
public class NativeByte extends NativeNumber {

  /**
   * The meta class instance
   */
  public static final NativeByteType META = new NativeByteType();

  static {
    NativeType.register(NativeByte.class, META);
  }

  /**
   * Create a new wrapper
   */
  public NativeByte() {
    allocate();
  }

  /**
   * Create a new wrapper
   */
  public NativeByte(byte value) {
    allocate();
    setValue(value);
  }

  protected NativeByte(INativeHandle handle) {
    super(handle);
  }

  static public NativeByte createFromAddress(long address) {
    return (NativeByte) NativeByte.META.createNative(NativeTools
                                                       .toHandle(address));
  }

  @Override
  public byte byteValue() {
    return handle.getByte(0);
  }

  @Override
  public INativeType getNativeType() {
    return META;
  }

  @Override
  public Object getValue() {
    return new Byte(byteValue());
  }

  public void setValue(byte value) {
    handle.setByte(0, value);
  }

  @Override
  public void setValue(Object value) {
    setValue(((Number) value).byteValue());
  }

  @Override
  public int intValue() {
    return byteValue();
  }

  @Override
  public long longValue() {
    return byteValue();
  }

  @Override
  public short shortValue() {
    return byteValue();
  }

  @Override
  public String toString() {
    if (getNativeHandle() == null) {
      return "nope - no handle"; //$NON-NLS-1$
    }
    if (getNativeHandle().getAddress() == 0) {
      return "nope - null pointer"; //$NON-NLS-1$
    }
    return String.valueOf(byteValue());
  }
}
