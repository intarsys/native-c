package de.intarsys.nativec.api;

public abstract class CallbackAdapter implements ICallback {

	private class CallbackHandle implements INativeCallback {

		private INativeHandle nativeHandle;

		public CallbackHandle(long address) {
			nativeHandle = NativeInterface.get().createHandle(address);
		}

		@Override
		public INativeHandle getNativeHandle() {
			return nativeHandle;
		}
	}

	private INativeCallback nativeCallback;

	public CallbackAdapter() {
		nativeCallback = NativeInterface.get().createCallback(this);
	}

	public CallbackAdapter(long address) {
		nativeCallback = new CallbackHandle(address);
	}

	public INativeCallback getNativeCallback() {
		return nativeCallback;
	}
}
