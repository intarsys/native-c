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
package de.intarsys.nativec.api;

import java.util.Iterator;
import java.util.ServiceLoader;
public class NativeInterface {

  public static final String PROP_NATIVEINTERFACE = "de.intarsys.nativec.api.INativeInterface";

  private static final Class<?>[] NO_PARAMETERS = new Class<?>[0];

  private static final Object[] NO_ARGUMENTS = new Object[0];

  public static INativeHandle NULL;
  private static INativeInterface ACTIVE;
  private static String NAME;

  static protected INativeInterface createNativeInterface() {

    String className = getName();
    if (className == null) {
      className = System.getProperty(PROP_NATIVEINTERFACE);
    }

    if (className != null) {
      try {

        final Class<?> clazz = Class.forName(className);
        return (INativeInterface) clazz.getDeclaredConstructor(NO_PARAMETERS).newInstance(NO_ARGUMENTS);

      } catch (Exception e) {
        throw new NoClassDefFoundError(className);
      }
    }
    return findNativeInterface();
  }

  static protected INativeInterface findNativeInterface() {

    final ServiceLoader<INativeInterface> loader = ServiceLoader.load(INativeInterface.class);
    final Iterator<INativeInterface> ps = loader.iterator();

    if (ps.hasNext()) {
      try {
        return ps.next();
      } catch (Throwable e) {
        // ignore and try on
      }
    }

    return null;
  }

  synchronized static public INativeInterface get() {
    if (ACTIVE == null) {
      set(createNativeInterface());
    }
    return ACTIVE;
  }

  synchronized static public String getName() {
    return NAME;
  }

  synchronized static public void setName(String name) {
    NAME = name;
  }

  synchronized static public void set(INativeInterface nativeInterface) {

    if (nativeInterface == null) {
      throw new NullPointerException("INativeInterface not available.");
    }

    ACTIVE = nativeInterface;
    NULL = nativeInterface.createHandle(0);
  }
}
