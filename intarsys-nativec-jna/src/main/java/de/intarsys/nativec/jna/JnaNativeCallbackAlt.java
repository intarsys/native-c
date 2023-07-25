package de.intarsys.nativec.jna;

import com.sun.jna.AltCallingConvention;

import de.intarsys.nativec.api.ICallback;

public class JnaNativeCallbackAlt extends JnaNativeCallback implements AltCallingConvention {

	public JnaNativeCallbackAlt(ICallback pCallback) {
		super(pCallback);
	}

}
