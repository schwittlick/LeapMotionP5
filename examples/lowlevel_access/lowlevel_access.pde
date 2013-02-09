import com.onformative.leap.LeapMotionP5;

LeapMotionP5 leap;

public void setup() {
  size(500, 500);
  leap = new LeapMotionP5(this);
}

public void draw() {
  com.leapmotion.leap.Frame frame = leap.getFrame();
  // do some processing of the frame
  // this is the same frame you'd get if you would use the callback
}

public void stop() {
  leap.stop();
}
