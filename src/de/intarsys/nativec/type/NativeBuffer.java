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
package de.intarsys.nativec.type;

import java.nio.ByteBuffer;

import de.intarsys.nativec.api.INativeHandle;
import de.intarsys.nativec.api.NativeInterface;

/**
 * A wrapper for a sequence of bytes.
 * 
 */
public class NativeBuffer extends NativeObject {

	/** The meta class instance */
	public static final NativeBufferType META = new NativeBufferType();

	static {
		NativeType.register(NativeBuffer.class, META);
	}

	private NativeBufferType type;

	public NativeBuffer(byte[] bytes) {
		type = new NativeBufferType(bytes.length);
		handle = NativeInterface.get().allocate(bytes.length);
		handle.setByteArray(0, bytes, 0, bytes.length);
	}

	protected NativeBuffer(INativeHandle handle) {
		super(handle);
	}

	public NativeBuffer(int pSize) {
		type = new NativeBufferType(pSize);
		handle = NativeInterface.get().allocate(pSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.intarsys.tools.nativec.NativeObject#getByteCount()
	 */
	@Override
	public int getByteCount() {
		// todo alignment?
		return type.getByteCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.intarsys.graphic.freetype.NativeObject#getMetaClass()
	 */
	@Override
	public INativeType getNativeType() {
		return META;
	}

	/**
	 * The number of elements in the NativeBuffer
	 * 
	 * @return The number of elements in the NativeBuffer
	 */
	public int getSize() {
		return type.getByteCount();
	}

	public Object getValue() {
		return getBytes();
	}

	public void setSize(int size) {
		type = new NativeBufferType(size);
		handle.setSize(getByteCount());
	}

	public void setValue(Object value) {
		if (value instanceof byte[]) {
			byte[] data = (byte[]) value;
			setByteArray(0, data, 0, data.length);
		} else if (value instanceof ByteBuffer) {
			byte[] data = new byte[getSize()];
			((ByteBuffer) value).get(data);
			setByteArray(0, data, 0, data.length);
		}
	}
}
