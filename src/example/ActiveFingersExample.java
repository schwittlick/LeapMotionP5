package example;

import com.leapmotion.leap.Finger;
import com.onformative.leap.LeapMotionP5;

import processing.core.PApplet;

public class ActiveFingersExample extends PApplet {
  LeapMotionP5 leap;

  public void setup() {
    size(500, 500);
    leap = new LeapMotionP5(this);
  }

  public void draw() {
    background(0);
    fill(255);
    for (Finger finger : leap.getFingerList()) {
      ellipse(leap.leapToScreenX(finger.tipPosition().getX()),
          leap.leapToScreenY(finger.tipPosition().getY()), 10, 10);
    }
  }

  public void stop() {
    leap.stop();
    super.stop();
  }
}
