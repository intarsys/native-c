package de.intarsys.nativec;

import de.intarsys.nativec.type.NativeVoid;

/** A @{link PseudoObject} where the wrapped entity is a handle */
public class Opaque extends Handle<NativeVoid> {

	public Opaque(NativeVoid nativeObject) {
		super(nativeObject);
	}
}
