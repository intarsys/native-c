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
import de.intarsys.nativec.api.NativeInterface;

/**
 * An object representing a reference to another object ("pointer").
 */
public class NativeReference<T extends INativeObject> extends NativeObject {

	/** The meta class instance */
	public static final NativeReferenceType META = new NativeReferenceType(NativeVoid.META);

	static {
		NativeType.register(NativeReference.class, META);
	}

	public static <U extends INativeObject> NativeReference<U> create(INativeType baseType) {
		NativeReferenceType type = new NativeReferenceType(baseType);
		return new NativeReference<U>(type);
	}

	private INativeHandle dereferenceHandle;

	private T dereferenced;

	private NativeReferenceType type;

	protected NativeReference(NativeReferenceType type) {
		this.type = type;
		allocate();
	}

	protected NativeReference(NativeReferenceType type, INativeHandle handle) {
		super(handle);
		this.type = type;
		handle.setSize(getByteCount());
	}

	public INativeType getBaseType() {
		return type.getBaseType();
	}

	@Override
	public int getByteCount() {
		return NativeInterface.get().pointerSize();
	}

	@Override
	public INativeType getNativeType() {
		return type;
	}

	public long getReferencedAddress() {
		return getNativeHandle(0).getAddress();
	}

	@Override
	public synchronized T getValue() {
		dereferenceHandle = handle.getNativeHandle(0);
		// check if not yet computed or handle changed
		if (dereferenced == null || !dereferenced.getNativeHandle().equals(dereferenceHandle)) {
			dereferenced = (T) type.getBaseType().createNative(dereferenceHandle);
		}
		return dereferenced;
	}

	public void setBaseType(INativeType baseType) {
		type = NativeReferenceType.create(baseType);
		dereferenceHandle = null;
		dereferenced = null;
	}

	@Override
	public synchronized void setValue(Object value) {
		if (value instanceof INativeObject) {
			T nativeObject = (T) value;
			INativeHandle valueHandle = nativeObject.getNativeHandle();
			handle.setNativeHandle(0, valueHandle);
			dereferenceHandle = valueHandle;
			dereferenced = nativeObject;
		} else if (value instanceof INativeHandle) {
			INativeHandle valueHandle = (INativeHandle) value;
			handle.setNativeHandle(0, valueHandle);
			dereferenceHandle = valueHandle;
			dereferenced = null;
		} else {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String toString() {
		if (handle == null) {
			return "Ref to null";
		}
		return "Ref to " + getReferencedAddress();
	}

}
