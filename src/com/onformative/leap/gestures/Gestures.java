package com.onformative.leap.gestures;

import java.util.ArrayList;

import processing.core.PApplet;

import com.onformative.leap.LeapMotionP5;

public class Gestures {
  private ArrayList<Gesture> gestures;
  private String callbackMethodName;
  private PApplet parent;
  private LeapMotionP5 leap;

  private boolean blocked = false;
  private int millisStarted;

  private int gestureTimeoutMillis = 1500;

  public Gestures(PApplet parent, LeapMotionP5 leap) {
    this.parent = parent;
    this.leap = leap;

    gestures = new ArrayList<Gesture>();

    callbackMethodName = "gestureRecognized";
  }

  public void update() {
    checkIfBlocked();

    if (!blocked) {
      for (Gesture gesture : gestures) {
        if (gesture.check()) {
          if (gesture.getClass().getName().equals("com.onformative.leap.gestures.SwipeLeftGesture")) {
            invokeCallback("swipeleft");
            block();
          } else if (gesture.getClass().getName()
              .equals("com.onformative.leap.gestures.SwipeRightGesture")) {
            invokeCallback("swiperight");
            block();
          } else if (gesture.getClass().getName()
              .equals("com.onformative.leap.gestures.SwipeUpGesture")) {
            invokeCallback("swipeup");
            block();
          } else if (gesture.getClass().getName()
              .equals("com.onformative.leap.gestures.SwipeDownGesture")) {
            invokeCallback("swipedown");
            block();
          } else if (gesture.getClass().getName()
              .equals("com.onformative.leap.gestures.PushGesture")) {
            invokeCallback("push");
            block();
          } else if (gesture.getClass().getName()
              .equals("com.onformative.leap.gestures.PullGesture")) {
            invokeCallback("pull");
            block();
          } else if (gesture.getClass().getName()
              .equals("com.onformative.leap.gestures.VictoryGesture")) {
            invokeCallback("victory");
            block();
          }
        }

      }
    }
  }

  private void block() {
    blocked = true;
    millisStarted = leap.getParent().millis();
  }

  /**
   * returns if the gesture detection is currently blocked, because there has just been a gesture
   * detected
   * 
   * @return boolean
   */
  public boolean isBlocked() {
    return blocked;
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
   * Checks if the gesture is blocked. Returns true if the last time this gesture has been
   * recognized is less than gestureTimeOutInMillis milliseconds ago
   */
  private void checkIfBlocked() {
    if (leap.getParent().millis() - millisStarted > gestureTimeoutMillis) {
      blocked = false;
    }
  }

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
   * @param gestureName the name of the gesture all possible gestures: swipeleft swiperight swipeup
   *        swipedown pull push victory
   * 
   *        onedollar gestures: - circle - triangle - rectangle - x - check - charet - zig-zag -
   *        arrow - leftsquarebracket - rightsquarebracket - v - delete - leftcurlybrace -
   *        rightcurlybrace - star - pigtail
   * 
   */
  public void addGesture(String gestureName) {
    if (gestureName.equals("swipeleft")) {
      gestures.add(new SwipeLeftGesture(leap));
    } else if (gestureName.equals("swiperight")) {
      gestures.add(new SwipeRightGesture(leap));
    } else if (gestureName.equals("swipeup")) {
      gestures.add(new SwipeUpGesture(leap));
    } else if (gestureName.equals("swipedown")) {
      gestures.add(new SwipeDownGesture(leap));
    } else if (gestureName.equals("push")) {
      gestures.add(new PushGesture(leap));
    } else if (gestureName.equals("pull")) {
      gestures.add(new PullGesture(leap));
    } else if (gestureName.equals("victory")) {
      gestures.add(new VictoryGesture(leap));
    }
  }
}
