import com.onformative.leap.LeapMotionP5;
import com.onformative.leap.gestures.OneDollarGestures;

LeapMotionP5 leap;

OneDollarGestures one;
String lastGesture = "";

void setup() {
  size(500, 500);

  textSize(30);

  leap = new LeapMotionP5(this);
  one = new OneDollarGestures(this, leap);

  one.addGesture("circle");
  one.addGesture("triangle");
  one.addGesture("rectangle");
  one.addGesture("x");
  //one.addGesture("check");
  //one.addGesture("caret");
  one.addGesture("zig-zag");
  one.addGesture("arrow");
  //one.addGesture("leftsquarebracket");
  //one.addGesture("rightsquarebracket");
  one.addGesture("v");
  //one.addGesture("delete");
  //one.addGesture("leftcurlybrace");
  //one.addGesture("rightcurlybrace");
  one.addGesture("star");
  one.addGesture("pigtail");

  one.start();
}

public void detected(String gesture, int x, int y, int c_x, int c_y) {
  System.out.println(gesture + " recognized");
  lastGesture = gesture;
}

void draw() {
  background(0);
  one.draw();
  one.update();

  text("detected gesture: "+lastGesture, 40, 40);
}

