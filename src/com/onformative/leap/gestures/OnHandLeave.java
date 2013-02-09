package com.onformative.leap.gestures;

import com.onformative.leap.LeapMotionP5;

public class OnHandLeave extends Gesture {
  public OnHandLeave(LeapMotionP5 leap) {
    super(leap);
    // TODO Auto-generated constructor stub
  }

  public boolean check() {
    int lastFrameHandCount = leap.getHandCount(leap.getLastFrame());
    int currentFrameHandCount = leap.getHandCount(leap.getFrame());
    if (lastFrameHandCount > currentFrameHandCount) {
      return true;
    }

    return false;
  }

  public String getShortname() {
    return LeapMotionP5.ON_HAND_LEAVE;
  }
}
