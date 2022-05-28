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
public class NativeArrayType extends NativeType {

  private final int arraySize;
  /**
   * The size of the base type of the array.
   */
  private final int baseSize;
  /**
   * The base type of the array.
   */
  private final INativeType baseType;

  protected NativeArrayType(INativeType baseDeclaration, int arraySize) {
    super();
    this.baseType = baseDeclaration;
    this.baseSize = baseDeclaration.getByteCount();
    this.arraySize = arraySize;
  }

  public static NativeArrayType create(INativeType baseType, int size) {
    return new NativeArrayType(baseType, size);
  }

  @Override
  public INativeObject createNative(INativeHandle handle) {
    NativeArray array = new NativeArray(this, handle);
    if (arraySize > 0) {
      array.setSize(arraySize);
    }
    return array;
  }

  @Override
  public INativeObject createNative(Object value) {
    NativeArray array = new NativeArray(this);
    return array;
  }

  public int getArraySize() {
    return arraySize;
  }

  public int getBaseSize() {
    return baseSize;
  }

  public INativeType getBaseType() {
    return baseType;
  }

  @Override
  public int getByteCount() {
    // todo take into account alignment / packing?
    return arraySize * baseSize;
  }

  public int getPreferredBoundary() {
    return baseType.getPreferredBoundary();
  }
}
