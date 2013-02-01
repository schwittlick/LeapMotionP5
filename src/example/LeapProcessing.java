package example;

import java.util.Map;

import com.leapmotion.leap.Vector;
import com.onformative.leap.LeapMotionP5;

import processing.core.PApplet;

public class LeapProcessing extends PApplet {

  LeapMotionP5 leap;

  static float LEAP_WIDTH = 200.0f; // in mm
  static float LEAP_HEIGHT = 700.0f; // in mm

  public void setup() {
    size(500, 500);

    leap = new LeapMotionP5(this);
  }

  public void draw() {
    background(0);

    for (Map.Entry entry : leap.getFingerPositions().entrySet()) {
      Integer fingerId = (Integer) entry.getKey();
      Vector position = (Vector) entry.getValue();
      fill(leap.getFingerColors().get(fingerId));
      noStroke();
      ellipse(leapToScreenX(position.getX()), leapToScreenY(position.getY()), 24.0f, 24.0f);
    }

    for (Map.Entry entry : leap.getToolPositions().entrySet()) {
      Integer toolId = (Integer) entry.getKey();
      Vector position = (Vector) entry.getValue();
      fill(leap.getToolColors().get(toolId));
      noStroke();
      ellipse(leapToScreenX(position.getX()), leapToScreenY(position.getY()), 24.0f, 24.0f);
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

  public static void main(String[] args) {
    PApplet.main(new String[] {"example.LeapProcessing"});
  }
}
