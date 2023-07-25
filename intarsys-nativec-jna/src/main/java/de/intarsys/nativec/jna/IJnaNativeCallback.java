package de.intarsys.nativec.jna;

import com.sun.jna.Callback;

public interface IJnaNativeCallback extends Callback {

	Object callback(Object[] args);
}
