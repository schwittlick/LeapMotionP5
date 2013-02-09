package com.onformative.leap.gestures;

import com.leapmotion.leap.Frame;
import com.onformative.leap.LeapMotionP5;

public class OnFingerEnter extends Gesture {

  public OnFingerEnter(LeapMotionP5 leap) {
    super(leap);
    // TODO Auto-generated constructor stub
  }

  public boolean check() {
    Frame lastFrame = leap.getLastFrame();
    Frame currentFrame = leap.getFrame();
    int lastFrameFingerCount = leap.getFingerCount(lastFrame);
    int currentFrameFingerCount = leap.getFingerCount(currentFrame);
    if (lastFrameFingerCount < currentFrameFingerCount) {
      return true;
    }

    return false;
  }

  public String getShortname() {
    return LeapMotionP5.ON_FINGER_ENTER;
  }
}
