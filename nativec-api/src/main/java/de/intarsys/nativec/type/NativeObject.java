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
import de.intarsys.nativec.api.NativeInterface;
public abstract class NativeObject implements INativeObject {

  public static final int SIZE_BYTE = 1;

  public static final int SIZE_INT = 4;

  public static final int SHIFT_INT = 2;

  public static final int SIZE_LONGLONG = 8;

  public static final int SHIFT_LONGLONG = 3;

  public static final int SIZE_LONG = NativeInterface.get().longSize();

  public static final int SHIFT_LONG = SIZE_LONG == 4 ? 2 : 3;

  public static final int SIZE_PTR = NativeInterface.get().pointerSize();

  public static final int SIZE_SHORT = 2;

  /**
   * DEBUG flag
   */
  public static boolean DEBUG = true;

  /**
   * The handle to the memory chunk used by this object. While in fact this is
   * final, Java language semantics does not allow to declare so!
   * <p>
   * The handle should only be assigned in the constructor, via parameter or
   * "allocate".
   */
  protected INativeHandle handle;

  /**
   *
   */
  protected NativeObject() {
    //
  }

  /**
   * Create a new NativeObject in C-Memory at pointer "handle". The bytes
   * belonging to this object may already have been copied from C-Memory and
   * made available in bytes at location offset.
   *
   * @param handle The pointer in C-memory
   */
  protected NativeObject(INativeHandle handle) {
    this.handle = handle;
  }

  /**
   * Manage the objects memory in Java. C memory will be valid at least as
   * long as we hold a reference to the buffer. C memory is undefined (but not
   * a memory leak) and may be reclaimed at any time after dropping our
   * pointer to the buffer.
   */
  protected void allocate() {
    int size = getByteCount();
    handle = NativeInterface.get().allocate(size);
  }

  /**
   * This is a special form of the "createNative" signature, implementing a
   * "type cast" on the same memory location.
   *
   * @param declaration The new base declaration type.
   *
   * @return The {@link INativeObject} at the same memory location as this,
   * but of a different type.
   */
  public INativeObject cast(INativeType declaration) {
    return declaration.createNative(handle);
  }

  public INativeObject createReference() {
    NativeReference ref = NativeReference.create(getNativeType());
    ref.setValue(this);
    return ref;
  }

  /**
   * The byte at index as a byte.
   *
   * @param index The index of the element to be reported.
   *
   * @return The element at index as a native byte.
   */
  public byte getByte(int index) {
    return handle.getByte(index);
  }

  /**
   * The element at index as an array of bytes with dimension count. This is a
   * lightweight optimization.
   *
   * @param index The index of the element to be reported.
   *
   * @return The element at index as an array of native byte with dimension
   * count.
   */
  public byte[] getByteArray(int index, int count) {
    return handle.getByteArray(index, count);
  }

  /**
   * The number of bytes occupied by this.
   *
   * @return The number of bytes occupied by this.
   */
  public abstract int getByteCount();

  /**
   * The bytes copied from C-memory that represent this.
   *
   * @return The bytes copied from C-memory that represent this.
   */
  public byte[] getBytes() {
    return handle.getByteArray(0, getByteCount());
  }

  /**
   * The element at index as a native long. Only the "platform" number of
   * bytes are read.
   *
   * @param index The index of the element to be reported.
   *
   * @return The element at index as a native long.
   */
  public long getCLong(int index) {
    return handle.getCLong(index);
  }

  /**
   * The element at index as a native int.
   *
   * @param index The index of the element to be reported.
   *
   * @return The element at index as a native int.
   */
  public int getInt(int index) {
    return handle.getInt(index);
  }

  /**
   * The C-Pointer where the associated memory is found.
   *
   * @return The C-Pointer where the associated memory is found.
   */
  public INativeHandle getNativeHandle() {
    return handle;
  }

  public INativeHandle getNativeHandle(int index) {
    return handle.getNativeHandle(index);
  }

  /**
   * The meta information and behavior for the NativeObject.
   * <p>
   * There is exactly one meta instance for all NativeObject instances of a
   * certain type.
   *
   * @return The meta information and behavior for the NativeObject.
   */
  abstract public INativeType getNativeType();

  /**
   * The element at index as a native short. This is a lightweight
   * optimization.
   *
   * @param index The index of the element to be reported.
   *
   * @return The element at index as a native short.
   */
  public short getShort(int index) {
    return handle.getShort(index);
  }

  public String getString(int index) {
    return handle.getString(index);
  }

  public String getWideString(int index) {
    return handle.getWideString(index);
  }

  /**
   * Answer <code>true</code> if this is "null". This means the associated
   * C-pointer is pointing to 0.
   *
   * @return Answer <code>true</code> if this is "null".
   */
  public boolean isNull() {
    return handle == null || handle.getAddress() == 0;
  }

  public void setByte(int index, byte value) {
    handle.setByte(index, value);
  }

  public void setByteArray(int index, byte[] value, int valueOffset,
                           int valueCount) {
    handle.setByteArray(index, value, valueOffset, valueCount);
  }

  public void setCLong(int index, long value) {
    handle.setCLong(index, value);
  }

  public void setInt(int index, int value) {
    handle.setInt(index, value);
  }

  public void setNativeHandle(int index, INativeHandle value) {
    handle.setNativeHandle(index, value);
  }

  public void setShort(int index, short value) {
    handle.setShort(index, value);
  }

  public void setString(int index, String value) {
    handle.setString(index, value);
  }

  public void setWideString(int index, String value) {
    handle.setWideString(index, value);
  }

  /**
   * A string for debugging purposes.
   *
   * @return A string for debugging purposes.
   */
  public String toNestedString() {
    return toString();
  }
}
