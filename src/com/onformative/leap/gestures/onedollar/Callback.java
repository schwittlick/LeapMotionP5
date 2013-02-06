package com.onformative.leap.gestures.onedollar;

import processing.core.PApplet;


public class Callback {

  private Object object;
  private String callback;

  public Callback(Object _object, String _callback) {
    this.object = _object;
    this.callback = _callback;
  }

  public void fire(Candidate _motion, String _template) {
    if (this.object != null) {
      try {
        /*this.object
            .getClass()
            .getMethod(this.callback, String.class, int.class, int.class, int.class, int.class)
            .invoke(this.object, _template, (int) _motion.getFirstPoint().x,
                (int) _motion.getFirstPoint().y, (int) _motion.getMiddlePoint().x,
                (int) _motion.getMiddlePoint().y);*/
        this.object
        .getClass()
        .getMethod(this.callback, String.class)
        .invoke(this.object, _template);
      } catch (Exception e) {
        PApplet.println(e.getMessage());
      }
    }
  }

  protected String getObjectClass() {
    return this.object.getClass().getName();
  }

  protected String getCallbackString() {
    return this.callback;
  }

}
