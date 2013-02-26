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


import java.util.ArrayList;

import processing.core.PApplet;

import com.onformative.leap.LeapGestures;
import com.onformative.leap.LeapMotionP5;

/**
 * GestureHandler.java
 * 
 * @author Marcel Schwittlick
 * 
 */
public class GestureHandler {
  public ArrayList<Gesture> gestures;
  private final String callbackMethodName;
  private PApplet parent;
  private LeapMotionP5 leap;

  private int millisStarted;

  private int gestureTimeoutMillis = 200;
  private float velocityThreshold = 1500;

  /**
   * 
   * @param parent
   * @param leap
   */
  public GestureHandler(PApplet parent, LeapMotionP5 leap) {
    this.parent = parent;
    this.leap = leap;

    gestures = new ArrayList<Gesture>();

    callbackMethodName = "gestureRecognized";
    millisStarted = parent.millis();
  }

  /**
   * 
   */
  private void block() {
    millisStarted = leap.getParent().millis();
  }

  /**
   * Checks if the gesture is blocked. Returns true if the last time this gesture has been
   * recognized is less than gestureTimeOutInMillis milliseconds ago
   */
  private boolean isBlocked() {
    if (leap.getParent().millis() - millisStarted > gestureTimeoutMillis) {
      // blocked = false;
      return true;
    } else {
      return false;
    }
  }

  /**
   * TODO: threshold value should be normalized
   * 
   * changes the threshold, according to which a gesture is triggered
   * 
   * @param threshold
   */
  public void setVelocityThreshold(float threshold) {
    this.velocityThreshold = threshold;
    for (Gesture ges : gestures) {
      ges.setVelocityThreshold(threshold);
    }
  }

  /**
   * 
   * @return
   */
  public float getVelocityThreshold() {
    return this.velocityThreshold;
  }

  /**
   * changes the timeout in millis for how long one gesture can not be performed after it has been
   * performed. this is set in order to avoid gesture flickering
   * 
   * @param millis
   */
  public void setGestureTimeoutMillis(int millis) {
    this.gestureTimeoutMillis = millis;
    for (Gesture ges : gestures) {
      ges.setGestureTimeoutMillis(millis);
    }
  }

  /**
   * 
   * @return
   */
  public int getGestureTimeoutMillis() {
    return this.gestureTimeoutMillis;
  }

  /**
   * 
   * @param method
   */
  private void invokeCallback(String method) {
    if (this.parent != null) {
      try {
        this.parent.getClass().getMethod(this.callbackMethodName, String.class)
            .invoke(this.parent, method);
      } catch (Exception e) {
        PApplet.println(e.getMessage() + " CALLBACK ERROR");
      }
    }
  }

  /**
   * add a gesture to the gesture recognizer
   * 
   * @param gestureName
   * 
   */
  public void addGesture(String gestureName) {

  }
}
