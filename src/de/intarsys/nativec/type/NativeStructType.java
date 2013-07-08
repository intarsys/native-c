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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.intarsys.nativec.api.NativeInterface;

/**
 * The meta class implementation
 */
public class NativeStructType extends NativeType {

	/**
	 * The boundary where the struct as a member of another struct would want to
	 * be aligned.
	 */
	private int byteBoundary = 1;

	/**
	 * The offset where theoretically a new member could be placed. Where it
	 * will actually be placed depends on the member itself.
	 */
	private int byteOffset = 0;

	/** The size in bytes of the member collection */
	private int byteSize = 0;

	/** The named members of the structure definition */
	private final Map<String, StructMember> fields = new HashMap<String, StructMember>();

	/**
	 * The number of members in the structure
	 */
	private int fieldsSize = 0;

	/** Where members should be aligned in native memory */
	// expecting pointer size to be a reasonable default - have to look that up
	private int packing = NativeInterface.get().pointerSize();

	protected NativeStructType() {
		super();
	}

	protected NativeStructType(Class<? extends NativeStruct> instanceClass) {
		super(instanceClass);
	}

	/**
	 * Declare a new member for the struct.
	 * 
	 * @param name
	 *            The name of the new member slot.
	 * @param declaration
	 *            The type declaration for the slot
	 */
	public StructMember declare(String name, INativeType declaration) {
		int size;
		int boundary;

		size = declaration.getByteCount();
		boundary = Math.min(declaration.getPreferredBoundary(), packing);
		byteBoundary = Math.max(byteBoundary, boundary);

		byteOffset = byteOffset - ((byteOffset + boundary - 1) % boundary + 1)
				+ boundary;
		StructMember field = new StructMember(this, name, fieldsSize++,
				declaration, byteOffset);
		fields.put(name, field);
		byteOffset = byteOffset + size;
		byteSize = byteOffset;
		return field;
	}

	public int getByteBoundary() {
		return byteBoundary;
	}

	@Override
	public int getByteCount() {
		return getByteSize();
	}

	/**
	 * The total size of the StructDeclaration.
	 * 
	 * @return The total size of the StructDeclaration.
	 */
	public int getByteSize() {
		return byteSize;
	}

	public StructMember getField(String name) {
		return fields.get(name);
	}

	/**
	 * The collection of StructMember instances in declaration order.
	 * 
	 * @return The collection of StructMember instances in declaration order.
	 */
	public List<StructMember> getFields() {
		return new ArrayList<StructMember>(fields.values());
	}

	public int getFieldsSize() {
		return fieldsSize;
	}

	public INativeObject getNativeObject(NativeStruct struct, String name) {
		StructMember member = fields.get(name);
		return member.getNativeObject(struct);
	}

	public int getPacking() {
		return packing;
	}

	@Override
	public int getPreferredBoundary() {
		return getByteBoundary();
	}

	public void setPacking(int pPacking) {
		if (!fields.isEmpty()) {
			throw new IllegalStateException(
					"packing must be set before members are declared");
		}
		packing = pPacking;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (StructMember field : getFields()) {
			sb.append(field.toString());
			sb.append("\n");
		}
		return sb.toString();
	}

}