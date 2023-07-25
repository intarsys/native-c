package de.intarsys.nativec;

import de.intarsys.nativec.type.INativeObject;

/**
 * A wrapper object for a handle or struct. Many libraries have groups of
 * functions that take a handle or struct as first argument and work on that
 * "object". We can treat the latter as real objects by wrapping them in an
 * instance of a subclass of this class.
 */
public class PseudoObject<T extends INativeObject> {

	private T nativeObject;

	public PseudoObject(T pNativeObject) {
		nativeObject = pNativeObject;
	}

	public T getNativeObject() {
		return nativeObject;
	}
}
