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
package de.intarsys.nativec.api;

import de.intarsys.nativec.type.INativeType;
import de.intarsys.nativec.type.NativeArray;
import de.intarsys.nativec.type.NativeArrayType;
import de.intarsys.nativec.type.NativeBuffer;
import de.intarsys.nativec.type.NativeBufferType;
import de.intarsys.nativec.type.NativeInt;
import de.intarsys.nativec.type.NativeLong;

/**
 * Tool class for for dealing with the native framework.
 */
public class NativeTools {

	public static byte[] fromNativeByteArray(long ptr, int count) {
		INativeHandle handle;
		INativeType type;
		NativeBuffer nativeValue;

		if (ptr == 0) {
			return null;
		}
		handle = NativeInterface.get().createHandle(ptr);
		type = NativeBufferType.create(count);
		nativeValue = (NativeBuffer) type.createNative(handle);
		return nativeValue.getBytes();
	}

	public static long fromNativeCLong(long ptr) {
		INativeHandle handle;
		NativeLong nativeValue;

		handle = NativeInterface.get().createHandle(ptr);
		nativeValue = (NativeLong) NativeLong.META.createNative(handle);
		return nativeValue.intValue();
	}

	public static IValueHolder<Number> fromNativeCLongHolder(long ptr) {
		return new ObjectValueHolder<Number>(fromNativeCLong(ptr));
	}

	public static int fromNativeInt(long ptr) {
		INativeHandle handle;
		NativeInt nativeValue;

		handle = NativeInterface.get().createHandle(ptr);
		nativeValue = (NativeInt) NativeInt.META.createNative(handle);
		return nativeValue.intValue();
	}

	public static int[] fromNativeIntArray(INativeHandle handle, int count) {
		int[] ints;
		int offset;

		ints = new int[count];
		offset = NativeInt.META.getByteCount();
		for (int index = 0; index < count; index++) {
			ints[index] = handle.getInt(index * offset);
		}
		return ints;
	}

	public static int[] fromNativeIntArray(long ptr, int count) {
		INativeHandle handle;

		handle = NativeInterface.get().createHandle(ptr);
		return fromNativeIntArray(handle, count);
	}

	public static IValueHolder<Integer> fromNativeIntHolder(long ptr) {
		return new ObjectValueHolder<Integer>(fromNativeInt(ptr));
	}

	public static String fromNativeString(long ptr, int count) {
		return null;
	}

	public static INativeHandle toHandle(long address) {
		return NativeInterface.get().createHandle(address);
	}

	public static void toNativeByteArray(long ptr, byte[] value) {
		if (value != null) {
			INativeHandle handle;
			INativeType type;
			NativeBuffer buffer;

			handle = NativeInterface.get().createHandle(ptr);
			type = NativeBufferType.create(value.length);
			buffer = (NativeBuffer) type.createNative(handle);
			buffer.setValue(value);
		}
	}

	public static void toNativeCLong(long ptr, int[] value) {
		if (value != null) {
			INativeHandle handle;
			NativeArrayType type;
			NativeArray array;

			handle = NativeInterface.get().createHandle(ptr);
			type = NativeArrayType.create(NativeLong.META, value.length);
			array = (NativeArray) type.createNative(handle);
			for (int index = 0; index < value.length; index++) {
				array.setValue(index, value[index]);
			}
		}
	}

	public static void toNativeCLong(long ptr, IValueHolder<Number> value) {
		toNativeCLong(ptr, value.get().longValue());
	}

	public static void toNativeCLong(long ptr, long value) {
		INativeHandle handle;
		NativeLong nativeValue;

		handle = NativeInterface.get().createHandle(ptr);
		nativeValue = (NativeLong) NativeLong.META.createNative(handle);
		nativeValue.setValue(value);
	}

	public static void toNativeCLong(long ptr, long[] value) {
		if (value != null) {
			INativeHandle handle;
			NativeArrayType type;
			NativeArray array;

			handle = NativeInterface.get().createHandle(ptr);
			type = NativeArrayType.create(NativeLong.META, value.length);
			array = (NativeArray) type.createNative(handle);
			for (int index = 0; index < value.length; index++) {
				array.setValue(index, value[index]);
			}
		}
	}

	public static void toNativeInt(long ptr, int value) {
		INativeHandle handle;

		handle = NativeInterface.get().createHandle(ptr);
		handle.setSize(NativeInt.META.getByteCount());
		handle.setInt(0, value);
	}

	public static void toNativeInt(long ptr, int[] value) {
		if (value != null) {
			INativeHandle handle;
			NativeArrayType type;
			NativeArray array;

			handle = NativeInterface.get().createHandle(ptr);
			type = NativeArrayType.create(NativeInt.META, value.length);
			array = (NativeArray) type.createNative(handle);
			for (int index = 0; index < value.length; index++) {
				array.setValue(index, value[index]);
			}
		}
	}

	public static void toNativeInt(long ptr, IValueHolder<Integer> value) {
		toNativeInt(ptr, value.get());
	}

	public static void toNativePointer(long ptr, INativeHandle value) {
		INativeHandle handle;

		handle = NativeInterface.get().createHandle(ptr);
		handle.setNativeHandle(0, value);
	}

}
