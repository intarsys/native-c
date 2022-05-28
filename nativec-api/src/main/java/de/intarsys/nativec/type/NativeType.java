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

import java.util.HashMap;
import java.util.Map;

import de.intarsys.nativec.api.INativeHandle;
public abstract class NativeType implements INativeType {

  /**
   * All known meta classes.
   */
  private static Map<Class<?>, INativeType> META_CLASSES = new HashMap<Class<?>, INativeType>();
  protected NativeType() {
    //
  }

  protected NativeType(Class<?> instanceClass) {
    register(instanceClass, this);
  }

  public static synchronized INativeType lookup(Class<?> clazz) {

    INativeType result = META_CLASSES.get(clazz);
    if (result == null) {
      ClassLoader classLoader;

      clazz.getClasses();
      classLoader = Thread.currentThread().getContextClassLoader();
      if (classLoader == null) {
        classLoader = NativeType.class.getClassLoader();
      }

      try {
        Class.forName(clazz.getName(), true, classLoader);
      } catch (ClassNotFoundException ex) {
        // ignore
      }
      result = META_CLASSES.get(clazz);
    }
    return result;
  }
  public static synchronized void register(Class<?> clazz, INativeType type) {
    META_CLASSES.put(clazz, type);
  }

  /**
   * Create a Declaration that represents an array of this.
   *
   * @return Create a Declaration that represents an array of this.
   */
  public INativeType Array(int size) {
    return new NativeArrayType(this, size);
  }

  public INativeObject createNative(INativeHandle handle) {
    throw new IllegalStateException("meta constructor missing");
  }

  public INativeObject createNative(Object value) {
    throw new IllegalStateException("meta constructor missing");
  }

  public int getByteCount() {
    return 0;
  }

  /**
   * Create a Declaration that represents a reference to this.
   *
   * @return Create a Declaration that represents a reference to this.
   */
  public INativeType Ref() {
    return new NativeReferenceType(this);
  }
}
