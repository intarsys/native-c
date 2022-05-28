package de.intarsys.nativec.type;

import de.intarsys.nativec.api.INativeFunction;
import de.intarsys.nativec.api.INativeHandle;
import de.intarsys.nativec.api.NativeInterface;

public class NativeFunction extends NativeVoid {

  public static final NativeFunctionType META = new NativeFunctionType();
  private Object callingConvention;
  private INativeFunction function;
  protected NativeFunction(INativeHandle handle) {
    super(handle);
    callingConvention = INativeFunction.CallingConventionCdecl;
  }

  public Object getCallingConvention() {
    return callingConvention;
  }

  public void setCallingConvention(Object callingConvention) {
    if (function != null) {
      throw new IllegalStateException();
    }
    this.callingConvention = callingConvention;
  }

  public INativeFunction getFunction() {
    // TODO do we want to check if handle has changed?
    if (function == null) {
      function = NativeInterface.get().createFunction(
        getNativeHandle().getAddress(), callingConvention);
    }
    return function;
  }

  public static class NativeFunctionType extends NativeVoidType {

    @Override
    public INativeObject createNative(INativeHandle handle) {
      return new NativeFunction(handle);
    }
  }
}
