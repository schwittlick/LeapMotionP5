package com.onformative.leap.gestures;

import com.onformative.leap.LeapMotionP5;

import processing.core.PApplet;

public class Gesture {
  protected PApplet parent;
  protected LeapMotionP5 leap;

  protected int gestureTimeoutInMillis = 100;
  protected float velocityThreshold = 1500;

  public Gesture(PApplet parent, LeapMotionP5 leap) {
    this.parent = parent;
    this.leap = leap;
  }

  /**
   * changes the threshold, according to which a gesture is triggered
   * 
   * @param threshold
   */
  public void setVelocityThreshold(float threshold) {
    this.velocityThreshold = threshold;
  }

  /**
   * changes the timeout in millis for how long one gesture can not be performed after it has been
   * performed. this is set in order to avoid gesture flickering
   * 
   * @param millis
   */
  public void setGestureTimeoutMillis(int millis) {
    this.gestureTimeoutInMillis = millis;
  }
}
