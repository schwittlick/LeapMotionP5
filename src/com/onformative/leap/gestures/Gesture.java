package com.onformative.leap.gestures;

import com.onformative.leap.LeapMotionP5;

import processing.core.PApplet;

public class Gesture {
  protected PApplet parent;
  protected LeapMotionP5 leap;

  protected int gestureTimeoutInMillis = 100;
  protected int velocityThreshold = -1500;

  public Gesture(PApplet parent, LeapMotionP5 leap) {
    this.parent = parent;
    this.leap = leap;
  }
}
