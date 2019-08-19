package com.aykuttasil.sweetloc.app;

import com.aykuttasil.sweetloc.App;
import com.facebook.stetho.Stetho;

/** Created by aykutasil on 24.07.2016. */
public class AppDebug extends App {
  @Override
  public void onCreate() {
    super.onCreate();
    Stetho.initializeWithDefaults(this);
  }
}
