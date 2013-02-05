package example;

import com.leapmotion.leap.Finger;
import com.onformative.leap.LeapMotionP5;

import processing.core.PApplet;

public class ActiveFingersExample extends PApplet {
  LeapMotionP5 leap;

  static float LEAP_WIDTH = 200.0f; // in mm
  static float LEAP_HEIGHT = 700.0f; // in mm

  public void setup() {
    size(500, 500);

    leap = new LeapMotionP5(this);
  }

  public void draw() {
    background(0);
    fill(255);
    for (Finger finger : leap.getActiveFingers()) {
      ellipse(leapToScreenX(finger.tipPosition().getX()),
          leapToScreenY(finger.tipPosition().getY()), 10, 10);
    }
  }

  public void stop() {
    leap.stop();
    super.stop();
  }

  float leapToScreenX(float x) {
    float c = width / 2.0f;
    if (x > 0.0) {
      return lerp(c, width, x / LEAP_WIDTH);
    } else {
      return lerp(c, 0.0f, -x / LEAP_WIDTH);
    }
  }

  float leapToScreenY(float y) {
    return lerp(height, 0.0f, y / LEAP_HEIGHT);
  }
}
