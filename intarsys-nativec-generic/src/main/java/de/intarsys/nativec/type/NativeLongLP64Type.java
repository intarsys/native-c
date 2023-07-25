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
package de.intarsys.nativec.type;

import de.intarsys.nativec.api.INativeHandle;

/**
 * The meta class implementation
 */
public class NativeLongLP64Type extends NativeNumberType {

	/**
	 * Utility method: return the given number as another number object with
	 * compatible byte size
	 */
	public static Object coerce(Number value) {
		if (NativeObject.SIZE_PTR == 4) {
			return value.intValue();
		}
		return value.longValue();
	}

	/**
	 * Utility method: return the java class whose instances have compatible byte
	 * size
	 */
	public static Class<? extends Number> primitiveClass() {
		if (NativeObject.SIZE_PTR == 4) {
			return Integer.class;
		}
		return Long.class;
	}

	protected NativeLongLP64Type() {
		super(NativeLongLP64.class);
	}

	@Override
	public INativeObject createNative(INativeHandle handle) {
		return new NativeLongLP64(handle);
	}

	@Override
	public INativeObject createNative(Object value) {
		return new NativeLongLP64(((Number) value).longValue());
	}

	@Override
	public int getByteCount() {
		return NativeObject.SIZE_PTR;
	}
}