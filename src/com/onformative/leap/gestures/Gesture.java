package com.onformative.leap.gestures;

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

import com.onformative.leap.LeapMotionP5;

import processing.core.PApplet;

/**
 * 
 * @author Marcel Schwittlick
 * 
 */
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
