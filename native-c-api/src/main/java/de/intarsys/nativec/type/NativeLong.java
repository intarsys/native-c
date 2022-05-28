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
package de.intarsys.nativec.type;

import de.intarsys.nativec.api.INativeHandle;
import de.intarsys.nativec.api.NativeTools;

/**
 * A wrapper for a primitive long. The size of a long depends on the platform;
 * on a 64-bit Linux or Mac OS X we have 8 byte longs...
 */
public class NativeLong extends NativeNumber {

  /**
   * The meta class instance
   */
  public static final NativeLongType META = new NativeLongType();

  static {
    NativeType.register(NativeLong.class, META);
  }

  /**
   * Create a new wrapper
   */
  public NativeLong() {
    allocate();
  }

  protected NativeLong(INativeHandle handle) {
    super(handle);
  }

  /**
   * Create a new wrapper
   */
  public NativeLong(long value) {
    allocate();
    setValue(value);
  }

  static public NativeLong createFromAddress(long address) {
    return (NativeLong) NativeLong.META.createNative(NativeTools
                                                       .toHandle(address));
  }

  @Override
  public byte byteValue() {
    return (byte) intValue();
  }

  @Override
  public INativeType getNativeType() {
    return META;
  }

  @Override
  public Object getValue() {
    return new Long(longValue());
  }

  public void setValue(long value) {
    handle.setCLong(0, value);
  }

  @Override
  public void setValue(Object value) {
    setValue(((Number) value).longValue());
  }

  @Override
  public int intValue() {
    return (int) longValue();
  }

  @Override
  public long longValue() {
    return handle.getCLong(0);
  }

  @Override
  public short shortValue() {
    return (short) longValue();
  }

  @Override
  public String toString() {
    if (getNativeHandle() == null) {
      return "nope - no handle"; //$NON-NLS-1$
    }
    if (getNativeHandle().getAddress() == 0) {
      return "nope - null pointer"; //$NON-NLS-1$
    }
    return String.valueOf(longValue());
  }
}
