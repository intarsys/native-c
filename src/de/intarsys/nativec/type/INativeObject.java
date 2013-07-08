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

import de.intarsys.nativec.api.INativeHandle;

/**
 * The Java object representation of a c memory construct.
 * <p>
 * The {@link INativeObject} has a reference to c memory (the
 * {@link INativeHandle} and "marshalling" methods "getValue" and "setValue" to
 * get and set the Java representation.
 * 
 */
public interface INativeObject {

	/**
	 * The bytes that make up the {@link INativeObject}.
	 * 
	 * @return The bytes that make up the {@link INativeObject}.
	 */
	public byte[] getBytes();

	/**
	 * The {@link INativeHandle} to the c memory for the object.
	 * 
	 * @return The {@link INativeHandle} to the c memory for the object.
	 */
	public INativeHandle getNativeHandle();

	/**
	 * The {@link INativeType} for the object.
	 * 
	 * @return The {@link INativeType} for the object.
	 */
	public INativeType getNativeType();

	/**
	 * A Java side representation from the memory.
	 * 
	 * @return A Java side representation for the {@link INativeObject}.
	 */
	public Object getValue();

	/**
	 * Assign (and marshall to memory) the Java side representation.
	 * 
	 * @param value
	 *            The new Java value.
	 */
	public void setValue(Object value);
}
