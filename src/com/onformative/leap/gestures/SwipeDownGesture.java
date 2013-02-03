package com.onformative.leap.gestures;

import processing.core.PApplet;

import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.onformative.leap.LeapMotionP5;

public class SwipeDownGesture extends Gesture {
  private boolean blockSwipeDown = false;
  private int millisBlockSwipeDownStarted;

  public SwipeDownGesture(PApplet parent, LeapMotionP5 leap) {
    super(parent, leap);
    // TODO Auto-generated constructor stub
  }

  /**
   * checks if the gesture has been performed
   * 
   * @return boolean returns true, if the gesture has been performed
   */
  public boolean check() {
    Frame frame = leap.getCurrentFrame();
    checkIfBlocked();

    if (!blockSwipeDown) {
      if (!frame.hands().empty()) {

        // Get the first hand
        Hand hand = frame.hands().get(0);
        FingerList fingers = hand.fingers();
        if (!fingers.empty()) {
          if (fingers.get(0).tipVelocity().getY() < velocityThreshold) {
            blockSwipeDown = true;
            millisBlockSwipeDownStarted = parent.millis();
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
    if (parent.millis() - millisBlockSwipeDownStarted > gestureTimeoutInMillis) {
      blockSwipeDown = false;
    }
  }


}
