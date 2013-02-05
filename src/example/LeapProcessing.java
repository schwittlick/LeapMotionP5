package example;

import java.util.Map;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Vector;
import com.onformative.leap.LeapMotionP5;

import processing.core.PApplet;

public class LeapProcessing extends PApplet {

  LeapMotionP5 leap;

  public void setup() {
    size(500, 500, P3D);

    leap = new LeapMotionP5(this);
  }

  public void draw() {
    background(0);
    lights();
    fill(255);
    for (Finger finger : leap.getFingerList()) {
      ellipse(leap.leapToScreenX(finger.tipPosition().getX()),
          leap.leapToScreenY(finger.tipPosition().getY()), 20, 20);
    }

    stroke(255);
    fill(190);
    box(100);
  }

  public void stop() {
    leap.stop();
    super.stop();
  }
}
