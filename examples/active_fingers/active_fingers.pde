import com.onformative.leap.LeapMotionP5;

LeapMotionP5 leap;

public void setup() {
  size(500, 500);
  leap = new LeapMotionP5(this);
}

public void draw() {
  background(0);
  fill(255);
  for (com.leapmotion.leap.Finger finger : leap.getActiveFingers()) {
    ellipse(leap.leapToScreenX(finger.tipPosition().getX()), 
    leap.leapToScreenY(finger.tipPosition().getY()), 10, 10);
  }
}

public void stop() {
  leap.stop();
  super.stop();
}

