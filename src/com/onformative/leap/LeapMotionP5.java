package com.onformative.leap;

import java.util.concurrent.ConcurrentMap;

import processing.core.PApplet;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Vector;

public class LeapMotionP5 {
  SampleListener listener;
  Controller controller;

  protected ConcurrentMap<Integer, Vector> fingerPositions;
  protected ConcurrentMap<Integer, Vector> toolPositions;
  protected ConcurrentMap<Integer, Integer> fingerColors;
  protected ConcurrentMap<Integer, Integer> toolColors;

  public LeapMotionP5(PApplet p) {
    listener = new SampleListener(p, this);
    controller = new Controller();

    // Have the sample listener receive events from the controller
    controller.addListener(listener);
  }

  public void stop() {
    controller.removeListener(listener);
  }

  public ConcurrentMap<Integer, Vector> getFingerPositions() {
    return fingerPositions;
  }

  public ConcurrentMap<Integer, Vector> getToolPositions() {
    return toolPositions;
  }

  public ConcurrentMap<Integer, Integer> getFingerColors() {
    return fingerColors;
  }

  public ConcurrentMap<Integer, Integer> getToolColors() {
    return toolColors;
  }
}
