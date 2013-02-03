package com.onformative.leap.gestures;

import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.onformative.leap.LeapMotionP5;

import processing.core.PApplet;

/**
 * TODO: Implement to check for a minimal distance to be performed, when the gesture is triggered
 * 
 * @author marcels
 * 
 */
public class SwipeLeftGesture extends Gesture {
  private boolean blockSwipeLeft = false;
  private int millisBlockSwipeLeftStarted;

  public SwipeLeftGesture(PApplet parent, LeapMotionP5 leap) {
    super(parent, leap);
  }

  /**
   * checks if the gesture has been performed
   * 
   * @return boolean returns true, if the gesture has been performed
   */
  public boolean check() {
    Frame frame = leap.getCurrentFrame();
    checkIfBlocked();

    if (!blockSwipeLeft) {
      if (!frame.hands().empty()) {

        // Get the first hand
        Hand hand = frame.hands().get(0);
        FingerList fingers = hand.fingers();
        if (!fingers.empty()) {
          if (fingers.get(0).tipVelocity().getX() < velocityThreshold) {
            blockSwipeLeft = true;
            millisBlockSwipeLeftStarted = parent.millis();
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Checks if the gesture is blocked. Returns true if the last time this gesture has been
   * recognized is less than gestureTimeOutInMillis milliseconds ago
   */
  private void checkIfBlocked() {
    if (parent.millis() - millisBlockSwipeLeftStarted > gestureTimeoutInMillis) {
      blockSwipeLeft = false;
    }
  }
}
