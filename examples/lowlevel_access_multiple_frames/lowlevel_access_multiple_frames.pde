import com.onformative.leap.LeapMotionP5;

LeapMotionP5 leap;

public void setup() {
  size(500, 500);
  leap = new LeapMotionP5(this);
}

public void draw() {
  for (com.leapmotion.leap.Frame frame : leap.getFrames()) {
    // do something with the last 250 frames
  }
}

