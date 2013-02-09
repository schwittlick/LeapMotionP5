import com.onformative.leap.LeapMotionP5;
import controlP5.*;

LeapMotionP5 leap;
ControlP5 cp5;

int gestureCount = 0;
String lastGesture = "";

public void setup() {
  size(500, 500);
  textSize(30);

  leap = new LeapMotionP5(this);
  cp5 = new ControlP5(this);

  leap.addGesture(leap.SWIPE_LEFT);
  leap.addGesture(leap.SWIPE_RIGHT);
  leap.addGesture(leap.SWIPE_UP);
  leap.addGesture(leap.SWIPE_DOWN);
  //leap.addGesture(leap.ON_HAND_ENTER);
  //leap.addGesture(leap.ON_HAND_LEAVE);
  //leap.addGesture(leap.ON_FINGER_ENTER);
  //leap.addGesture(leap.ON_FINGER_LEAVE);
  //leap.addGesture(leap.PUSH);
  //leap.addGesture(leap.PULL);
  leap.addGesture(leap.CIRCLE);
  leap.addGesture(leap.TRIANGLE);
  leap.addGesture(leap.RECTANGLE);
  //leap.addGesture(leap.ZIG_ZAG);
  //leap.addGesture(leap.X);
  //leap.addGesture(leap.CHECK);
  //leap.addGesture(leap.CHARET);
  //leap.addGesture(leap.ARROW);
  //leap.addGesture(leap.LEFT_CURLY_BRACKET);
  //leap.addGesture(leap.RIGHT_CURLY_BRACKET);
  //leap.addGesture(leap.LEFT_SQUARE_BRACKET);
  //leap.addGesture(leap.RIGHT_SQUARE_BRACKET);
  //leap.addGesture(leap.V);
  //leap.addGesture(leap.DELETE);
  //leap.addGesture(leap.STAR);
  //leap.addGesture(leap.PIGTAIL);

  leap.start();
}

public void draw() {
  background(0);
  leap.gestures.one.draw();
  leap.update();
  text(lastGesture, 30, 30);
}

public void gestureRecognized(String gesture) {
  gestureCount++;
  lastGesture = gesture+" "+gestureCount;
}

public void stop() {
  leap.stop();
}

