package com.onformative.leap;

/**
 * LeapMotionP5
 * 
 * LeapMotionP5 library for Processing. Copyright (c) 2012-2013 held jointly by the individual
 * authors.
 * 
 * LeapMotionP5 library for Processing is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * 
 * LeapMotionP5 for Processing is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with LeapMotionP5 library
 * for Processing. If not, see http://www.gnu.org/licenses/.
 * 
 * Leap Developer SDK. Copyright (C) 2012-2013 Leap Motion, Inc. All rights reserved.
 * 
 * NOTICE: This developer release of Leap Motion, Inc. software is confidential and intended for
 * very limited distribution. Parties using this software must accept the SDK Agreement prior to
 * obtaining this software and related tools. This software is subject to copyright.
 */

import java.util.LinkedList;
import java.util.concurrent.ConcurrentMap;

import processing.core.PApplet;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Vector;

/**
 * LeapMotionP5.java
 * 
 * @author Marcel Schwittlick
 * @modified 04.02.2013
 * 
 *           the main class of the library. this class has to be instanciated in order to access
 *           leap data within processing
 * 
 */
public class LeapMotionP5 {
  private LeapMotionListener listener;
  private Controller controller;

  protected ConcurrentMap<Integer, Vector> fingerPositions;
  protected ConcurrentMap<Integer, Vector> fingerVelocity;
  protected ConcurrentMap<Integer, Vector> toolPositions;
  protected ConcurrentMap<Integer, Double> handPitch;
  protected ConcurrentMap<Integer, Double> handRoll;
  protected ConcurrentMap<Integer, Double> handYaw;
  protected LinkedList<Frame> lastFrames;

  protected Frame currentFrame;

  public LeapMotionP5(PApplet p) {
    listener = new LeapMotionListener(p, this);
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

  public LinkedList<Frame> getFrames() {
    return lastFrames;
  }
}
