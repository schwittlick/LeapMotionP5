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
    for (Finger finger : leap.getActiveFingers()) {
      ellipse(leap.leapToScreenX(finger.tipPosition().getX()),
          leap.leapToScreenY(finger.tipPosition().getY()), 20, 20);
    }

    translate(width / 2, height / 2);
    for (Map.Entry entry : leap.getHandPitch().entrySet()) {
      double pitch = (Double) entry.getValue();
      rotateX(map((float) pitch, -1, 1, 0, TWO_PI));
    }

    for (Map.Entry entry : leap.getHandRoll().entrySet()) {
      double roll = (Double) entry.getValue();
      rotateY(map((float) roll, -1, 1, 0, TWO_PI));
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
