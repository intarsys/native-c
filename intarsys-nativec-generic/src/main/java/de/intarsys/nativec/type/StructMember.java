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
 * A field definition within a {@link NativeStructType}.
 */
public class StructMember {

	/** the members declaration */
	private final INativeType memberType;

	/** The members name */
	protected final String name;

	/** the index of the member within the struct */
	protected final int index;

	/** The offset of the declaration within its structure */
	private final int offset;

	/** the definition context of the member */
	private final NativeStructType structType;

	/**
	 * Create a slot with name "name" and the declaration "memberDeclaration"
	 */
	protected StructMember(NativeStructType structType, String name, int index, INativeType memberType, int offset) {
		super();
		this.structType = structType;
		this.name = name;
		this.index = index;
		this.memberType = memberType;
		this.offset = offset;
	}

	/**
	 * Performance shortcut to access "byte" in the struct member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 * @return The byte at index within the memory range of struct
	 */
	public byte getByte(NativeStruct struct, int index) {
		return struct.handle.getByte(offset + index);
	}

	/**
	 * Performance shortcut to access "byte[]" in the struct member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 * @return The byte array starting at index of length count within the memory
	 *         range of struct
	 */
	public byte[] getByteArray(NativeStruct struct, int index, int count) {
		return struct.handle.getByteArray(offset + index, count);
	}

	/**
	 * Performance shortcut to access "platform sized long" in the struct member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 * @return The platform sized long at index within the memory range of struct
	 */
	public long getCLong(NativeStruct struct, int index) {
		return struct.handle.getCLong(offset + index);
	}

	public float getFloat(NativeStruct struct, int index) {
		return struct.handle.getFloat(offset + index);
	}

	/**
	 * Performance shortcut to access "int" in the struct member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 * @return The int at index within the memory range of struct
	 */
	public int getInt(NativeStruct struct, int index) {
		return struct.handle.getInt(offset + index);
	}

	/**
	 * Performance shortcut to access "long" in the struct member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 * @return The long at index within the memory range of struct
	 */
	public long getLong(NativeStruct struct, int index) {
		return struct.handle.getLong(offset + index);
	}

	/**
	 * The type declaration of the slot.
	 * 
	 * @return The type declaration of the slot.
	 */
	protected INativeType getMemberType() {
		return memberType;
	}

	/**
	 * The slots name.
	 * 
	 * @return The slots name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Performance shortcut to access "INativeHandle" in the struct member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 * @return The INativeHandle at index within the memory range of struct
	 */
	public INativeHandle getNativeHandle(NativeStruct struct, int index) {
		return struct.handle.getNativeHandle(offset + index);
	}

	public synchronized INativeObject getNativeObject(NativeStruct struct) {
		if (struct.values == null) {
			struct.values = new INativeObject[structType.getFieldsSize()];
		}
		INativeObject result = struct.values[index];
		if (result == null) {
			result = memberType.createNative(struct.getNativeHandle().offset(offset));
			struct.values[this.index] = result;
		}
		return result;
	}

	/**
	 * The offset of the slot relative to the StructDeclaration.
	 * 
	 * @return The offset of the slot relative to the StructDeclaration.
	 */
	protected int getOffset() {
		return offset;
	}

	/**
	 * Performance shortcut to access "short" in the struct member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 * @return The short at index within the memory range of struct
	 */
	public short getShort(NativeStruct struct, int index) {
		return struct.handle.getShort(offset + index);
	}

	/**
	 * Performance shortcut to access "String" in the struct member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 * @return The String at index within the memory range of struct
	 */
	public String getString(NativeStruct struct, int index) {
		return struct.handle.getString(offset + index);
	}

	public Object getValue(NativeStruct struct) {
		return getNativeObject(struct).getValue();
	}

	/**
	 * Performance shortcut to access "String" (from wide characters) in the struct
	 * member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 * @return The wide character String at index within the memory range of struct
	 */
	public String getWideString(NativeStruct struct, int index) {
		return struct.handle.getWideString(offset + index);
	}

	/**
	 * Performance shortcut to access "byte" in the struct member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 */
	public void setByte(NativeStruct struct, int index, byte value) {
		struct.handle.setByte(offset + index, value);
	}

	/**
	 * Performance shortcut to access "byte[]" in the struct member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 */
	public void setByteArray(NativeStruct struct, int index, byte[] value, int valueOffset, int valueCount) {
		struct.handle.setByteArray(offset + index, value, valueOffset, valueCount);
	}

	/**
	 * Performance shortcut to access "platform sized long" in the struct member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 */
	public void setCLong(NativeStruct struct, int index, long value) {
		struct.handle.setCLong(offset + index, value);
	}

	/**
	 * Performance shortcut to access "int" in the struct member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 */
	public void setInt(NativeStruct struct, int index, int value) {
		struct.handle.setInt(offset + index, value);
	}

	/**
	 * Performance shortcut to access "long" in the struct member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 */
	public void setLong(NativeStruct struct, int index, long value) {
		struct.handle.setLong(offset + index, value);
	}

	/**
	 * Performance shortcut to access "INativeHandle" in the struct member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 */
	public void setNativeHandle(NativeStruct struct, int index, INativeHandle value) {
		struct.handle.setNativeHandle(offset + index, value);
	}

	/**
	 * Performance shortcut to access "short" in the struct member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 */
	public void setShort(NativeStruct struct, int index, short value) {
		struct.handle.setShort(offset + index, value);
	}

	/**
	 * Performance shortcut to access "String" in the struct member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 */
	public void setString(NativeStruct struct, int index, String value) {
		struct.handle.setString(offset + index, value);
	}

	public void setValue(NativeStruct struct, Object value) {
		getNativeObject(struct).setValue(value);
	}

	/**
	 * Performance shortcut to access "String" (from wide characters) in the struct
	 * member.
	 * 
	 * @param struct The container struct instance
	 * @param index  The memory offset from the struct member base
	 */
	public void setWideString(NativeStruct struct, int index, String value) {
		struct.handle.setWideString(offset + index, value);
	}

	@Override
	public String toString() {
		return "[" + getName() + "]";
	}
}
