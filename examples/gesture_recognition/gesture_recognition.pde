import com.onformative.leap.LeapMotionP5;
import com.onformative.leap.LeapGestures;
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

  leap.addGesture(LeapGestures.SWIPE_LEFT);
  leap.addGesture(LeapGestures.SWIPE_RIGHT);
  leap.addGesture(LeapGestures.SWIPE_UP);
  leap.addGesture(LeapGestures.SWIPE_DOWN);
  //leap.addGesture(LeapGestures.ON_HAND_ENTER);
  //leap.addGesture(LeapGestures.ON_HAND_LEAVE);
  //leap.addGesture(LeapGestures.ON_FINGER_ENTER);
  //leap.addGesture(LeapGestures.ON_FINGER_LEAVE);
  //leap.addGesture(LeapGestures.PUSH);
  //leap.addGesture(LeapGestures.PULL);
  leap.addGesture(LeapGestures.CIRCLE);
  leap.addGesture(LeapGestures.TRIANGLE);
  leap.addGesture(LeapGestures.RECTANGLE);
  //leap.addGesture(LeapGestures.ZIG_ZAG);
  //leap.addGesture(LeapGestures.X);
  //leap.addGesture(LeapGestures.CHECK);
  //leap.addGesture(LeapGestures.CHARET);
  //leap.addGesture(LeapGestures.ARROW);
  //leap.addGesture(LeapGestures.LEFT_CURLY_BRACKET);
  //leap.addGesture(LeapGestures.RIGHT_CURLY_BRACKET);
  //leap.addGesture(LeapGestures.LEFT_SQUARE_BRACKET);
  //leap.addGesture(LeapGestures.RIGHT_SQUARE_BRACKET);
  //leap.addGesture(LeapGestures.V);
  //leap.addGesture(LeapGestures.DELETE);
  //leap.addGesture(LeapGestures.STAR);
  //leap.addGesture(LeapGestures.PIGTAIL);

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

