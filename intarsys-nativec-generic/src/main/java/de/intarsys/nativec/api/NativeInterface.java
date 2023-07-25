/*
 * Copyright (c) 2008, intarsys AG
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
package de.intarsys.nativec.api;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Access the VM singleton for {@link INativeInterface}.
 * <p>
 * To make this work, just do one of the following:
 * <ul>
 * <li>set the class name of your implementation with
 * {@link NativeInterface}</li>
 * <li>set a {@link INativeInterface} of your choice in {@link NativeInterface}.
 * </li>
 * <li>set system property "de.intarsys.nativec.api.INativeInterface" to the
 * class name of your implementation.</li>
 * <li>include a service provider file
 * "de.intarsys.nativec.api.INativeInterface" in your deployment with the class
 * name of your implementation.</li>
 * </ul>
 * 
 */
public class NativeInterface {

	private static INativeInterface ACTIVE;

	private static String NAME;

	public static INativeHandle NULL;

	public static final String PROP_NATIVEINTERFACE = "de.intarsys.nativec.api.INativeInterface"; //$NON-NLS-1$

	static protected INativeInterface createNativeInterface() {
		String className = getName();
		if (className == null) {
			className = System.getProperty(PROP_NATIVEINTERFACE);
		}
		if (className != null) {
			try {
				Class clazz = Class.forName(className);
				return (INativeInterface) clazz.newInstance();
			} catch (Exception e) {
				throw new NoClassDefFoundError(className);
			}
		}
		return findNativeInterface();
	}

	static protected INativeInterface findNativeInterface() {
		ServiceLoader<INativeInterface> loader = ServiceLoader.load(INativeInterface.class);
		Iterator<INativeInterface> ps = loader.iterator();
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

	synchronized static public void set(INativeInterface nativeInterface) {
		if (nativeInterface == null) {
			throw new NullPointerException("no native interface available");
		}
		ACTIVE = nativeInterface;
		NULL = nativeInterface.createHandle(0);
	}

	synchronized static public void setName(String name) {
		NAME = name;
	}

}
