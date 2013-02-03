package com.onformative.leap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import processing.core.PApplet;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Vector;

public class LeapMotionP5 {
  SampleListener listener;
  Controller controller;

  protected ConcurrentMap<Integer, Vector> fingerPositions;
  protected ConcurrentMap<Integer, Vector> fingerVelocity;
  protected ConcurrentMap<Integer, Vector> toolPositions;
  protected ConcurrentMap<Integer, Integer> fingerColors;
  protected ConcurrentMap<Integer, Integer> toolColors;
  protected ConcurrentMap<Integer, Double> handPitch;
  protected ConcurrentMap<Integer, Double> handRoll;
  protected ConcurrentMap<Integer, Double> handYaw;
  protected LinkedList<Frame> lastFrames;

  protected Frame currentFrame;

  public LeapMotionP5(PApplet p) {
    listener = new SampleListener(p, this);
    controller = new Controller();

    controller.addListener(listener);
  }

  public void stop() {
    controller.removeListener(listener);
  }

  public ConcurrentMap<Integer, Vector> getFingerPositions() {
    return fingerPositions;
  }
  
  public ConcurrentMap<Integer, Vector> getFingerVelocity() {
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

  public ConcurrentMap<Integer, Double> getHandPitch() {
    return handPitch;
  }

  public ConcurrentMap<Integer, Double> getHandRoll() {
    return handRoll;
  }

  public ConcurrentMap<Integer, Double> getHandYaw() {
    return handRoll;
  }

  public Frame getCurrentFrame() {
    if (currentFrame != null) {
      return currentFrame;
    } else {
      return new Frame();
    }
  }
  
  public LinkedList<Frame> getFrames(){
    return lastFrames;
  }
}
