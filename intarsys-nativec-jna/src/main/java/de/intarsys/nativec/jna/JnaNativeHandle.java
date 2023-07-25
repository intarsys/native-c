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

import com.sun.jna.Native;
import com.sun.jna.Pointer;

import de.intarsys.nativec.api.INativeHandle;

public class JnaNativeHandle implements INativeHandle {

	private final Pointer pointer;

	private int size;

	public JnaNativeHandle(JnaNativeHandle handle, int offset) {
		this.pointer = new Pointer(handle.getAddress() + offset);
		this.size = handle.size - offset;
	}

	public JnaNativeHandle(long address) {
		this.pointer = new Pointer(address);
	}

	public JnaNativeHandle(Pointer pointer) {
		this.pointer = pointer;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof INativeHandle)) {
			return false;
		}
		INativeHandle otherHandle = (INativeHandle) obj;
		return JNATools.getPeer(pointer) == otherHandle.getAddress() && size == otherHandle.getSize();
	}

	@Override
	public long getAddress() {
		return JNATools.getPeer(pointer);
	}

	@Override
	public byte getByte(int index) {
		return pointer.getByte(index);
	}

	@Override
	public byte[] getByteArray(int index, int count) {
		return pointer.getByteArray(index, count);
	}

	@Override
	public long getCLong(int index) {
		if (Native.LONG_SIZE == 4) {
			return pointer.getInt(index);
		}
		return pointer.getLong(index);
	}

	@Override
	public float getFloat(int index) {
		return pointer.getFloat(index);
	}

	@Override
	public int getInt(int index) {
		return pointer.getInt(index);
	}

	@Override
	public long getLong(int index) {
		return pointer.getLong(index);
	}

	@Override
	public INativeHandle getNativeHandle(int index) {
		Pointer tempPointer = pointer.getPointer(index);
		if (tempPointer == null) {
			// TODO can we reuse the existing "NULL" object?
			return new JnaNativeHandle(0);
		}
		return new JnaNativeHandle(tempPointer);
	}

	public Pointer getPointer() {
		return pointer;
	}

	@Override
	public short getShort(int index) {
		return pointer.getShort(index);
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public String getString(int index) {
		// if (buffer == null) {
		// return null;
		// }
		// // todo encoding
		// buffer.position(offset + index);
		// int i = 0;
		// while (buffer.get() != 0) {
		// i++;
		// }
		// buffer.position(offset + index);
		// byte[] valueBytes = new byte[i];
		// buffer.get(valueBytes);
		// return new String(valueBytes);
		return getPointer().getString(index);
	}

	@Override
	public String getWideString(int index) {
		return getPointer().getWideString(index);
	}

	@Override
	public int hashCode() {
		return pointer.hashCode();
	}

	@Override
	public INativeHandle offset(int offset) {
		return new JnaNativeHandle(this, offset);
	}

	@Override
	public void setByte(int index, byte value) {
		pointer.setByte(index, value);
	}

	@Override
	public void setByteArray(int index, byte[] value, int valueOffset, int valueCount) {
		pointer.write(index, value, valueOffset, valueCount);
	}

	@Override
	public void setCLong(int index, long value) {
		if (Native.LONG_SIZE == 4) {
			pointer.setInt(index, (int) value);
			return;
		}
		pointer.setLong(index, value);
	}

	@Override
	public void setFloat(int index, float value) {
		pointer.setFloat(index, value);
	}

	@Override
	public void setInt(int index, int value) {
		pointer.setInt(index, value);
	}

	@Override
	public void setLong(int index, long value) {
		pointer.setLong(index, value);
	}

	@Override
	public void setNativeHandle(int index, INativeHandle handle) {
		pointer.setPointer(index, new Pointer(handle.getAddress()));
	}

	@Override
	public void setShort(int index, short value) {
		pointer.setShort(index, value);
	}

	@Override
	public void setSize(int pSize) {
		this.size = pSize;
	}

	@Override
	public void setString(int index, String value) {
		// // todo encoding
		getPointer().setString(index, value);
	}

	@Override
	public void setWideString(int index, String value) {
		getPointer().setWideString(index, value);
	}
}
