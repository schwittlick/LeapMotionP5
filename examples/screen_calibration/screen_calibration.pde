import com.leapmotion.leap.Finger;
import com.onformative.leap.LeapMotionP5;

LeapMotionP5 leap;

/**
 * this example shows how to use the screen calibration utility in processing. 
 * in order to use this example you have to have your leap calibrated to your 
 * screen. this will show you the intersection points between the extension of 
 * your fingertips and your screen.
 * TIP: try moving the sketch window while you are pointing at your screen
 */
public void setup() {
  size(1000, 700, P3D);

  leap = new LeapMotionP5(this);
  fill(255);
}

public void draw() {
  background(0);
  for (Finger f : leap.getFingerList()) {
    PVector screenPos = leap.getTipOnScreen(f);
    ellipse(screenPos.x, screenPos.y, 10, 10);
  }
}

