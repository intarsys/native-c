package de.intarsys.nativec;

import de.intarsys.nativec.type.NativeVoid;

public class Handle<T extends NativeVoid> extends PseudoObject<T> {

	public Handle(T nativeHandle) {
		super(nativeHandle);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == getClass()) {
			return getNativeAddress() == ((Handle<?>) obj).getNativeAddress();
		}
		return false;
	}

	protected long getNativeAddress() {
		return getNativeObject().getNativeHandle().getAddress();
	}

	@Override
	public int hashCode() {
		return Long.hashCode(getNativeAddress());
	}
}
