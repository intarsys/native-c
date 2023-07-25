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

/**
 * A "handle" to a piece of memory (in c space).
 * <p>
 * The handle combines an address and a memory chunk of a specified size.
 */
public interface INativeHandle {

	/**
	 * The start address of the memory chunk
	 * 
	 * @return The start address of the memory chunk
	 */
	long getAddress();

	/**
	 * Marshal the data at byte offset <code>index</code> from the start of the
	 * memory chunk to a byte.
	 * 
	 * @param index The byte offset from the start of the memory chunk
	 * @return A byte marshaled from the memory chunk
	 */
	byte getByte(int index);

	/**
	 * Marshal the data at byte offset <code>index</code> from the start of the
	 * memory chunk to a byte array of length <code>count</code>.
	 * 
	 * @param index The byte offset from the start of the memory chunk
	 * @param count The size of the byte array
	 * @return A byte array marshaled from the memory chunk
	 */
	byte[] getByteArray(int index, int count);

	/**
	 * Marshal the data at byte offset <code>index</code> from the start of the
	 * memory chunk to a long. Get only the "platform" number of bytes.
	 * 
	 * @param index The byte offset from the start of the memory chunk
	 * @return A long marshaled from the memory chunk
	 */
	long getCLong(int index);

	float getFloat(int index);

	/**
	 * Marshal the data at byte offset <code>index</code> from the start of the
	 * memory chunk to an int.
	 * 
	 * @param index The byte offset from the start of the memory chunk
	 * @return An int marshaled from the memory chunk
	 */
	int getInt(int index);

	/**
	 * Marshal the data at byte offset <code>index</code> from the start of the
	 * memory chunk to a long value (which is always 8 byte).
	 * 
	 * @param index The byte offset from the start of the memory chunk
	 * @return A long marshaled from the memory chunk
	 */
	long getLong(int index);

	/**
	 * Marshal the data at byte offset <code>index</code> from the start of the
	 * memory chunk to an {@link INativeHandle}.
	 * 
	 * @param index The byte offset from the start of the memory chunk
	 * @return An {@link INativeHandle} marshaled from the memory chunk
	 */
	INativeHandle getNativeHandle(int index);

	/**
	 * Marshal the data at byte offset <code>index</code> from the start of the
	 * memory chunk to a short.
	 * 
	 * @param index The byte offset from the start of the memory chunk
	 * @return A short marshaled from the memory chunk
	 */
	short getShort(int index);

	/**
	 * The size for the handle in bytes.
	 * <p>
	 * You can not access bytes from outside the range defined by getAdddress +
	 * size.
	 * 
	 */
	int getSize();

	/**
	 * Marshal the data at byte offset <code>index</code> from the start of the
	 * memory chunk to a String.
	 * 
	 * @param index The byte offset from the start of the memory chunk
	 * @return A String marshaled from the memory chunk
	 */
	String getString(int index);

	/**
	 * Marshal the data at byte offset <code>index</code> from the start of the
	 * memory chunk to a String using the platform wide character conversion.
	 * 
	 * @param index The byte offset from the start of the memory chunk
	 * @return A String marshaled from the memory chunk
	 */
	String getWideString(int index);

	/**
	 * Create a new {@link INativeHandle}, offset from this by <code>offset</code>
	 * bytes.
	 * 
	 * @param offset The byte offset from the start of the memory chunk
	 * @return A new {@link INativeHandle} pointing to "getAddress() + offset".
	 */
	INativeHandle offset(int offset);

	/**
	 * Write a byte to the memory at byte offset <code>index</code> from the start
	 * of the memory chunk.
	 * 
	 * @param index The byte offset from the start of the memory chunk
	 * @param value The value to write.
	 */
	void setByte(int index, byte value);

	/**
	 * Write a byte array to the memory at byte offset <code>index</code> from the
	 * start of the memory chunk. The method will write <code>valueCount</code>
	 * bytes from <code>value</code> starting at <code>valueOffset</code>.
	 * 
	 * @param index The byte offset from the start of the memory chunk
	 * @param value The value to write.
	 */
	void setByteArray(int index, byte[] value, int valueOffset, int valueCount);

	/**
	 * Write a long to the memory at byte offset <code>index</code> from the start
	 * of the memory chunk. Write only the "platform" number of bytes. The caller is
	 * responsible for observing the value range.
	 * 
	 * @param index The byte offset from the start of the memory chunk
	 * @param value The value to write.
	 */
	void setCLong(int index, long value);

	void setFloat(int index, float value);

	/**
	 * Write an int to the memory at byte offset <code>index</code> from the start
	 * of the memory chunk.
	 * 
	 * @param index The byte offset from the start of the memory chunk
	 * @param value The value to write.
	 */
	void setInt(int index, int value);

	/**
	 * Write a long to the memory at byte offset <code>index</code> from the start
	 * of the memory chunk.
	 * 
	 * @param index The byte offset from the start of the memory chunk
	 * @param value The value to write.
	 */
	void setLong(int index, long value);

	/**
	 * Write an {@link INativeHandle} to the memory at byte offset
	 * <code>index</code> from the start of the memory chunk.
	 * 
	 * @param index       The byte offset from the start of the memory chunk
	 * @param valueHandle The value to write.
	 */
	void setNativeHandle(int index, INativeHandle valueHandle);

	/**
	 * Write a short to the memory at byte offset <code>index</code> from the start
	 * of the memory chunk.
	 * 
	 * @param index The byte offset from the start of the memory chunk
	 * @param value The value to write.
	 */
	void setShort(int index, short value);

	/**
	 * Set the valid size for the handle to <code>count</code> bytes.
	 * <p>
	 * You can not access bytes from outside the range defined by getAdddress +
	 * size.
	 * 
	 * @param count The size of the memory managed by the {@link INativeHandle}
	 */
	void setSize(int count);

	/**
	 * Write a String to the memory at byte offset <code>index</code>from the start
	 * of the memory chunk.
	 * 
	 * @param index The byte offset from the start of the memory chunk
	 * @param value The value to write.
	 */
	void setString(int index, String value);

	/**
	 * Write a String to the memory at byte offset <code>index</code>from the start
	 * of the memory chunk using the platform wide character conversion.
	 * 
	 * @param index The byte offset from the start of the memory chunk
	 * @param value The value to write.
	 */
	void setWideString(int index, String value);
}
