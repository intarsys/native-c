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

import java.util.Iterator;

import de.intarsys.nativec.api.INativeHandle;
public abstract class NativeStruct extends NativeObject {

  /**
   * The meta class instance
   */
  public static final NativeStructType META = new NativeStructType();

  static {
    NativeType.register(NativeGenericStruct.class, META);
  }

  protected INativeObject[] values;

  public NativeStruct() {
    super();
  }

  public NativeStruct(INativeHandle handle) {
    super(handle);
  }

  @Override
  public int getByteCount() {
    return getNativeType().getByteCount();
  }

  /**
   * The NativeObject at the named slot name.
   * <p>
   * The marshalling is delegated to the StructMember in the
   * StructDeclaration.
   *
   * @param name The name of the slot in the structure.
   *
   * @return The NativeObject at the named slot name.
   */
  public INativeObject getNativeObject(String name) {
    return getStructType().getNativeObject(this, name);
  }

  protected StructMember getStructField(String name) {
    return getStructType().getField(name);
  }

  public NativeStructType getStructType() {
    return (NativeStructType) getNativeType();
  }

  public Object getValue() {
    throw new UnsupportedOperationException(
      "getValue not implemented for NativeStruct");
  }

  public void setValue(Object value) {
    throw new UnsupportedOperationException(
      "getValue not implemented for NativeStruct");
  }

  /*
   * (non-Javadoc)
   *
   * @see de.intarsys.tools.nativec.NativeObject#toNestedString()
   */
  @Override
  public String toNestedString() {
    return "Struct " + getClass().getName();
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getClass().getName());
    sb.append("\n");
    for (Iterator it = getStructType().getFields().iterator(); it.hasNext(); ) {
      StructMember field = (StructMember) it.next();
      sb.append(field.getName());
      sb.append("=");
      try {
        sb.append(getNativeObject(field.getName()).toString());
      } catch (RuntimeException e) {
        sb.append("**error**");
      }
      sb.append("\n");
    }
    return sb.toString();
  }
}
