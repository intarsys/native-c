package de.intarsys.nativec.jna;

import com.sun.jna.Pointer;

public class JNATools {

  public static long getPeer(Pointer p) {
    if (p == null) {
      return 0;
    }
    return Pointer.nativeValue(p);
  }
}
