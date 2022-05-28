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
 * A wrapper for a C wide (double byte) string.
 */
public class NativeWideString extends NativeObject {

  /**
   * The meta class instance
   */
  public static final NativeWideStringType META = new NativeWideStringType();

  static {
    NativeType.register(NativeWideString.class, META);
  }

  private int size = 0;
  private NativeWideStringType type;
  protected NativeWideString(NativeWideStringType pType) {
    type = pType;
    allocate();
  }

  protected NativeWideString(NativeWideStringType pType, INativeHandle handle) {
    super(handle);
    type = pType;
    if (type.hasByteCount()) {
      handle.setSize(type.getByteCount());
    }
  }

  protected NativeWideString(NativeWideStringType pType, String value) {
    type = pType;
    if (type.getStringSize() == 0) {
      size = value.length() + 1;
      size = size << 1;
    }
    allocate();
    setValue(value);
  }

  public NativeWideString(String value) {
    this(META, value);
  }

  static public NativeWideString createFromAddress(long address) {
    return (NativeWideString) NativeWideString.META
      .createNative(NativeTools.toHandle(address));
  }

  @Override
  public int getByteCount() {
    if (type.hasByteCount()) {
      return type.getByteCount();
    }
    return size;
  }

  @Override
  public INativeType getNativeType() {
    return type;
  }

  public Object getValue() {
    return stringValue();
  }

  public void setValue(Object value) {
    setValue((String) value);
  }

  public void setValue(String value) {
    size = value.length() + 1;
    size = size << 1;
    handle.setWideString(0, value);
  }

  /**
   * The java object corresponding to this.
   *
   * @return The java object corresponding to this.
   */
  public String stringValue() {
    return handle.getWideString(0);
  }
}
