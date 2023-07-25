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
package de.intarsys.nativec.jna;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.CallbackProxy;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;

import de.intarsys.nativec.api.CLong;
import de.intarsys.nativec.api.CWideString;
import de.intarsys.nativec.api.ICallback;
import de.intarsys.nativec.api.INativeCallback;
import de.intarsys.nativec.api.INativeHandle;
import de.intarsys.nativec.type.INativeType;
import de.intarsys.nativec.type.NativeObject;
import de.intarsys.nativec.type.NativeType;

public abstract class JnaNativeCallback implements IJnaNativeCallback, INativeCallback, CallbackProxy {

	public static class DummyStructure extends Structure {

		@SuppressWarnings("unused")
		public IJnaNativeCallback callback;

		public DummyStructure(Pointer pointer) {
			super(pointer);
		}

		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("callback");
		}
	}

	public static Class<?> translateType(Class<?> type) {
		// TODO implement more elegant type translation
		if (CWideString.class.isAssignableFrom(type)) {
			return WString.class;
		} else if (CLong.class.isAssignableFrom(type)) {
			return NativeLong.class;
		} else if (NativeObject.class.isAssignableFrom(type)) {
			return Pointer.class;
		} else if (INativeHandle.class.isAssignableFrom(type)) {
			return Pointer.class;
		}
		return type;
	}

	private ICallback callback;
	private INativeHandle nativeHandle;

	public JnaNativeCallback(ICallback pCallback) {
		callback = pCallback;
	}

	public Object callback(Object[] args) {
		for (int index = 0; index < args.length; index++) {
			Class<?> parameterType;
			Object object;

			parameterType = callback.getParameterTypes()[index];
			if (CWideString.class.isAssignableFrom(parameterType)) {
				WString wstring = (WString) args[index];
				object = new CWideString(wstring.toString());
			} else if (CLong.class.isAssignableFrom(parameterType)) {
				Number nativeNumber = (Number) args[index];
				object = new CLong(nativeNumber.longValue());
			} else if (NativeObject.class.isAssignableFrom(parameterType)) {
				Pointer pointer = (Pointer) args[index];
				if (pointer == null) {
					object = null;
				} else {
					INativeHandle handle = new JnaNativeHandle(pointer);
					INativeType type = NativeType.lookup(parameterType);
					if (type == null) {
						throw new IllegalArgumentException("no type for '" + parameterType + "'");
					}
					object = type.createNative(handle);
				}
			} else if (INativeHandle.class.isAssignableFrom(parameterType)) {
				Pointer pointer = (Pointer) args[index];
				if (pointer == null) {
					object = null;
				} else {
					object = new JnaNativeHandle(pointer);
				}
			} else {
				object = args[index];
			}
			args[index] = object;
		}

		Class<?> returnType = callback.getReturnType();
		Object returnValue = callback.invoke(args);
		// TODO map other return values too
		if (CLong.class.isAssignableFrom(returnType)) {
			if (Native.LONG_SIZE == 8) {
				return ((CLong) returnValue).longValue();
			}
			return ((CLong) returnValue).intValue();
		}
		if (NativeObject.class.isAssignableFrom(returnType)) {
			NativeObject nativeObject = (NativeObject) returnValue;
			return ((JnaNativeHandle) nativeObject.getNativeHandle()).getPointer();
		}
		return returnValue;
	}

	public INativeHandle getNativeHandle() {
		if (nativeHandle == null) {
			/*
			 * It seems JNA won't let us access a callback's address directly. The
			 * workaround is to write it to a structure designed specifically for that
			 * purpose that is at a known memory location. And then read that memory.
			 */
			Pointer pointer = new Memory(Native.POINTER_SIZE);
			DummyStructure dummyStructure = new DummyStructure(pointer);
			dummyStructure.writeField("callback", this);
			nativeHandle = new JnaNativeHandle(pointer.getPointer(0));
		}
		return nativeHandle;
	}

	public Class<?>[] getParameterTypes() {
		Class<?>[] genericParameterTypes;
		Class<?>[] nativeParameterTypes;

		genericParameterTypes = callback.getParameterTypes();
		nativeParameterTypes = new Class<?>[genericParameterTypes.length];

		for (int index = 0; index < genericParameterTypes.length; index++) {
			nativeParameterTypes[index] = translateType(genericParameterTypes[index]);
		}
		return nativeParameterTypes;
	}

	public Class<?> getReturnType() {
		return translateType(callback.getReturnType());
	}
}
