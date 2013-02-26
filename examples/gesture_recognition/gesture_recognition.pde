import com.onformative.leap.LeapMotionP5;
import com.onformative.leap.LeapGestures;
import controlP5.*;

LeapMotionP5 leap;
  String lastGesture = "";

  public void setup() {
    size(500, 500);
    textSize(30);
    frameRate(120);

    leap = new LeapMotionP5(this);

    //leap.enableGesture(Type.TYPE_CIRCLE);
    //leap.enableGesture(Type.TYPE_SWIPE);
    //leap.enableGesture(Type.TYPE_SCREEN_TAP);
    //leap.enableGesture(Type.TYPE_KEY_TAP);
  }

  public void draw() {
    background(0);
    for (Hand hand : leap.getHandList()) {
      PVector handPos = leap.getPosition(hand);
      ellipse(handPos.x, handPos.y, 20, 20);
    }
    text(lastGesture, 30, 30);
  }

  public void circleGestureRecognized(CircleGesture gesture, String clockwiseness) {
    if (gesture.state() == State.STATE_STOP) {
      System.out.println("//////////////////////////////////////");
      System.out.println("Gesture type: " + gesture.type().toString());
      System.out.println("ID: " + gesture.id());
      System.out.println("Radius: " + gesture.radius());
      System.out.println("Normal: "+gesture.normal());
      System.out.println("Clockwiseness: " + clockwiseness);
      System.out.println("Progress: " + gesture.progress());
      System.out.println("Center: " + leap.convertVectorToPVector(gesture.center()));
      System.out.println("Duration: " + gesture.durationSeconds() + "s");
      System.out.println("//////////////////////////////////////");
      lastGesture = "CIRCLE";
    } else if (gesture.state() == State.STATE_START) {

    } else if (gesture.state() == State.STATE_UPDATE) {

    }
  }

  public void swipeGestureRecognized(SwipeGesture gesture) {
    if (gesture.state() == State.STATE_STOP) {
      System.out.println("//////////////////////////////////////");
      System.out.println("Gesture type: " + gesture.type());
      System.out.println("ID: " + gesture.id());
      System.out.println("Position: " + leap.convertVectorToPVector(gesture.position()));
      System.out.println("Direction: " + gesture.direction());
      System.out.println("Duration: " + gesture.durationSeconds() + "s");
      System.out.println("Speed: " + gesture.speed());
      System.out.println("//////////////////////////////////////");
      lastGesture = "SWIPE";
    } else if (gesture.state() == State.STATE_START) {

    } else if (gesture.state() == State.STATE_UPDATE) {

    }
  }

  public void screenTapGestureRecognized(ScreenTapGesture gesture) {
    if (gesture.state() == State.STATE_STOP) {
      System.out.println("//////////////////////////////////////");
      System.out.println("Gesture type: " + gesture.type());
      System.out.println("ID: " + gesture.id());
      System.out.println("Position: " + leap.convertVectorToPVector(gesture.position()));
      System.out.println("Direction: " + gesture.direction());
      System.out.println("Duration: " + gesture.durationSeconds() + "s");
      System.out.println("//////////////////////////////////////");
      lastGesture = "SCREENTAP";
    } else if (gesture.state() == State.STATE_START) {

    } else if (gesture.state() == State.STATE_UPDATE) {

    }
  }

  public void KeyTapGestureRecognized(KeyTapGesture gesture) {
    if (gesture.state() == State.STATE_STOP) {
      System.out.println("//////////////////////////////////////");
      System.out.println("Gesture type: " + gesture.type());
      System.out.println("ID: " + gesture.id());
      System.out.println("Position: " + leap.convertVectorToPVector(gesture.position()));
      System.out.println("Direction: " + gesture.direction());
      System.out.println("Duration: " + gesture.durationSeconds() + "s");
      System.out.println("//////////////////////////////////////");
      lastGesture = "KEYTAP";
    } else if (gesture.state() == State.STATE_START) {

    } else if (gesture.state() == State.STATE_UPDATE) {

    }
  }

  public void keyPressed() {
    if (key == 'c') {
      if (leap.isEnabled(Type.TYPE_CIRCLE)) {
        leap.disableGesture(Type.TYPE_CIRCLE);
      } else {
        leap.enableGesture(Type.TYPE_CIRCLE);
      }
      System.out.println("Circle gesture: " + leap.isEnabled(Type.TYPE_CIRCLE));
    }

    if (key == 's') {
      if (leap.isEnabled(Type.TYPE_SWIPE)) {
        leap.disableGesture(Type.TYPE_SWIPE);
      } else {
        leap.enableGesture(Type.TYPE_SWIPE);
      }
      System.out.println("Swipe gesture: " + leap.isEnabled(Type.TYPE_SWIPE));
    }

    if (key == 'k') {
      if (leap.isEnabled(Type.TYPE_KEY_TAP)) {
        leap.disableGesture(Type.TYPE_KEY_TAP);
      } else {
        leap.enableGesture(Type.TYPE_KEY_TAP);
      }
      System.out.println("KeyTap gesture: " + leap.isEnabled(Type.TYPE_KEY_TAP));
    }

    if (key == 't') {
      if (leap.isEnabled(Type.TYPE_SCREEN_TAP)) {
        leap.disableGesture(Type.TYPE_SCREEN_TAP);
      } else {
        leap.enableGesture(Type.TYPE_SCREEN_TAP);
      }
      System.out.println("ScreenTap gesture: " + leap.isEnabled(Type.TYPE_SCREEN_TAP));
    }
  }
  
  public void stop(){
    leap.stop();
  }
