package com.onformative.leap.gestures;

/*
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

import com.onformative.leap.LeapMotionP5;

/**
 * GestureHandler.java
 * 
 * @author Marcel Schwittlick
 * 
 */
public class GestureHandler {
  private ArrayList<Gesture> gestures;
  public OneDollarGestures one;
  private final String callbackMethodName;
  private PApplet parent;
  private LeapMotionP5 leap;

  private int millisStarted;

  public static final String CIRCLE = "circle";
  public static final String TRIANGLE = "triangle";
  public static final String RECTANGLE = "rectangle";
  public static final String X = "x";
  public static final String CHECK = "check";
  public static final String CHARET = "charet";
  public static final String ZIG_ZAG = "zig-zag";
  public static final String ARROW = "arrow";
  public static final String LEFT_SQUARE_BRACKET = "leftsquarebracket";
  public static final String RIGHT_SQUARE_BRACKET = "rightsquarebracket";
  public static final String LEFT_CURLY_BRACKET = "leftcurlybrace";
  public static final String RIGHT_CURLY_BRACKET = "rightcurlybrace";
  public static final String V = "v";
  public static final String DELETE = "delete";
  public static final String STAR = "star";
  public static final String PIGTAIL = "pigtail";

  public static final String SWIPE_LEFT = "swipeleft";
  public static final String SWIPE_RIGHT = "swiperight";
  public static final String SWIPE_UP = "swipeup";
  public static final String SWIPE_DOWN = "swipedown";
  public static final String PUSH = "push";
  public static final String PULL = "pull";
  public static final String VICTORY = "victory";


  private int gestureTimeoutMillis = 150;

  /**
   * 
   * @param parent
   * @param leap
   */
  public GestureHandler(PApplet parent, LeapMotionP5 leap) {
    this.parent = parent;
    this.leap = leap;

    gestures = new ArrayList<Gesture>();
    one = new OneDollarGestures(parent, leap);

    callbackMethodName = "gestureRecognized";
    millisStarted = parent.millis();
  }

  /**
   * 
   */
  public void start() {
    one.start();
  }

  /**
   * 
   */
  public void stop() {
    one.stop();
  }

  /**
   * 
   */
  public void update() {
    if (isBlocked()) {
      one.update();

      // this triggeres the call of the callbackfunction in the main processing class.
      for (Gesture gesture : gestures) {
        if (gesture.check()) {
          invokeCallback(gesture.getShortname());
          block();
        }
      }
    }
  }

  /**
   * 
   */
  private void block() {
    // blocked = true;
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
    for (Gesture ges : gestures) {
      ges.setVelocityThreshold(threshold);
    }
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

    if (gestureName.equals(SWIPE_LEFT)) {
      gestures.add(new SwipeLeftGesture(leap));
    } else if (gestureName.equals(SWIPE_RIGHT)) {
      gestures.add(new SwipeRightGesture(leap));
    } else if (gestureName.equals(SWIPE_UP)) {
      gestures.add(new SwipeUpGesture(leap));
    } else if (gestureName.equals(SWIPE_DOWN)) {
      gestures.add(new SwipeDownGesture(leap));
    } else if (gestureName.equals(PUSH)) {
      gestures.add(new PushGesture(leap));
    } else if (gestureName.equals(PULL)) {
      gestures.add(new PullGesture(leap));
    } else if (gestureName.equals(VICTORY)) {
      gestures.add(new VictoryGesture(leap));
    }

    if (gestureName.equals(CIRCLE)) {
      one.addGesture(gestureName);
    } else if (gestureName.equals(TRIANGLE)) {
      one.addGesture(gestureName);
    } else if (gestureName.equals(RECTANGLE)) {
      one.addGesture(gestureName);
    } else if (gestureName.equals(X)) {
      one.addGesture(gestureName);
    } else if (gestureName.equals(CHECK)) {
      one.addGesture(gestureName);
    } else if (gestureName.equals(CHARET)) {
      one.addGesture(gestureName);
    } else if (gestureName.equals(ZIG_ZAG)) {
      one.addGesture(gestureName);
    } else if (gestureName.equals(ARROW)) {
      one.addGesture(gestureName);
    } else if (gestureName.equals(LEFT_SQUARE_BRACKET)) {
      one.addGesture(gestureName);
    } else if (gestureName.equals(RIGHT_SQUARE_BRACKET)) {
      one.addGesture(gestureName);
    } else if (gestureName.equals(V)) {
      one.addGesture(gestureName);
    } else if (gestureName.equals(DELETE)) {
      one.addGesture(gestureName);
    } else if (gestureName.equals(LEFT_CURLY_BRACKET)) {
      one.addGesture(gestureName);
    } else if (gestureName.equals(RIGHT_CURLY_BRACKET)) {
      one.addGesture(gestureName);
    } else if (gestureName.equals(STAR)) {
      one.addGesture(gestureName);
    } else if (gestureName.equals(PIGTAIL)) {
      one.addGesture(gestureName);
    }
  }
}
