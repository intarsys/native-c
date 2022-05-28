package de.intarsys.nativec;

import de.intarsys.nativec.type.INativeObject;

public class PseudoObject<T extends INativeObject> {

  private T nativeObject;

  public PseudoObject(T pNativeObject) {
    nativeObject = pNativeObject;
  }

  public T getNativeObject() {
    return nativeObject;
  }
}
