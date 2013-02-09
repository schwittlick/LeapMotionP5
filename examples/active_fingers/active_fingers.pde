import com.onformative.leap.LeapMotionP5;
import com.leapmotion.leap.Finger;

LeapMotionP5 leap;

public void setup() {
  size(500, 500);
  leap = new LeapMotionP5(this);
}

public void draw() {
  background(0);
  fill(255);
  for (Finger finger : leap.getFingerList()) {
    PVector fingerPos = leap.getTip(finger);
    ellipse(fingerPos.x, fingerPos.y, 10, 10);
  }
}

public void stop() {
  leap.stop();
}

