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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.jna.Function;
import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import de.intarsys.nativec.api.ICallback;
import de.intarsys.nativec.api.INativeCallback;
import de.intarsys.nativec.api.INativeFunction;
import de.intarsys.nativec.api.INativeHandle;
import de.intarsys.nativec.api.INativeInterface;
import de.intarsys.nativec.api.INativeLibrary;

/**
 * An {@link INativeInterface} implemented using JNA, a LGPL licensed Java
 * native interface abstraction.
 * <p>
 * In our point of view, JNA has the power of deploying all what we wanted to
 * have, but is ill designed in some key hot spots - so we worked around and
 * built on top of our own interfaces.
 * 
 */
public class JnaNativeInterface implements INativeInterface {

	static {
		/*
		 * Starting with JNA 4, the default encoding for native strings is UTF-8
		 * instead of the default platform encoding. Reset here because our code
		 * relies on platform encoding.
		 */
		System.setProperty("jna.encoding", Charset.defaultCharset().name());

		/*
		 * Ugly hack - multithreaded loading caused a deadlock here as
		 * com.sun.jna.Native requests a native library which later requires a
		 * lock on the class loader, while another piece of code
		 * (Security.addProvider) first locks the class loader and later on
		 * requests the runtime to load a native library. <p> We try here to
		 * provide the same ordering (which is based on a lot of assumptions...)
		 */
		ClassLoader loader = JnaNativeInterface.class.getClassLoader();
		synchronized (loader) {
			try {
				Class.forName("com.sun.jna.Native", true, loader);
			} catch (Exception e) {
				throw new InternalError("can not load JNA", e);
			}
		}
	}

	private List<String> searchPaths = new ArrayList<String>();

	@Override
	public void addSearchPath(String path) {
		if (searchPaths.contains(path)) {
			return;
		}
		searchPaths.add(path);
	}

	@Override
	public INativeHandle allocate(int size) {
		return new JnaNativeHandle(createMemory(size));
	}

	@Override
	public INativeCallback createCallback(ICallback callback) {
		if (callback == null) {
			return null;
		}
		if (callback.getCallingConvention() == INativeFunction.CallingConventionStdcall) {
			return new JnaNativeCallbackAlt(callback);
		}
		if (callback.getCallingConvention() == INativeFunction.CallingConventionCdecl) {
			return new JnaNativeCallbackStd(callback);
		}
		throw new IllegalArgumentException("illegal calling convention");
	}

	@Override
	public INativeFunction createFunction(long address) {
		return createFunction(address, INativeFunction.CallingConventionCdecl);
	}

	@Override
	public INativeFunction createFunction(long address, Object callingConvention) {
		int callFlags;
		if (callingConvention == INativeFunction.CallingConventionCdecl) {
			callFlags = Function.C_CONVENTION;
		} else if (callingConvention == INativeFunction.CallingConventionStdcall) {
			callFlags = Function.ALT_CONVENTION;
		} else {
			throw new IllegalArgumentException("illegal calling convention");
		}
		Pointer pointer = new Pointer(address);
		Function function = Function.getFunction(pointer, callFlags);
		return new JnaNativeFunction(function);
	}

	@Override
	public INativeHandle createHandle(long address) {
		return new JnaNativeHandle(address);
	}

	@Override
	public INativeLibrary createLibrary(String name) {
		return createLibrary(name, INativeFunction.CallingConventionCdecl);
	}

	@Override
	public INativeLibrary createLibrary(String name, Object callingConvention) {
		try {
			Map<String, Object> options = new HashMap<>(2);
			if (callingConvention == INativeFunction.CallingConventionCdecl) {
				options.put(Library.OPTION_CALLING_CONVENTION, Function.C_CONVENTION);
			} else if (callingConvention == INativeFunction.CallingConventionStdcall) {
				options.put(Library.OPTION_CALLING_CONVENTION, Function.ALT_CONVENTION);
			} else {
				throw new IllegalArgumentException("illegal calling convention");
			}
			options.put(Library.OPTION_OPEN_FLAGS, -1);
			return new JnaNativeLibrary(this, name, options);
		} catch (Throwable t) {
			Logger.getLogger("nativec").log(Level.FINEST, "JNA error creating library '" + name + "' (" + t.getMessage()
					+ ")");
			throw t;
		}
	}

	protected Pointer createMemory(int size) {
		try {
			Pointer p = new Memory(size);
			p.clear(size);
			return p;
		} catch (Exception e) {
			throw new InternalError("can not create Pointer");
		}
	}

	protected List<String> getSearchPaths() {
		return searchPaths;
	}

	@Override
	public int longSize() {
		return Native.LONG_SIZE;
	}

	@Override
	public int pointerSize() {
		return Native.POINTER_SIZE;
	}

	@Override
	public int wideCharSize() {
		return 2;
	}

}
