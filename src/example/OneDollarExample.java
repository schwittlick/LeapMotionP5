package example;

import processing.core.PApplet;
import processing.core.PVector;

import com.leapmotion.leap.Vector;
import com.onformative.leap.LeapMotionP5;
import com.onformative.leap.onedollar.OneDollar;

public class OneDollarExample extends PApplet {


  LeapMotionP5 leap;

  String gesture_name;
  PVector position, centroid;

  OneDollar one;

  public void setup() {
    size(500, 500);
    background(17);
    textFont(createFont("Arial", 11));
    smooth();
    frameRate(200);
    leap = new LeapMotionP5(this);

    gesture_name = "-";
    position = new PVector(-10, -10);
    centroid = new PVector(-10, -10);

    one = new OneDollar(this);
    one.setVerbose(true); // activate verbose mode
    println(one); // print the settings

    // add template gestures:
    one.add("triangle", new Integer[] {137, 139, 135, 141, 133, 144, 132, 146, 130, 149, 128, 151,
        126, 155, 123, 160, 120, 166, 116, 171, 112, 177, 107, 183, 102, 188, 100, 191, 95, 195,
        90, 199, 86, 203, 82, 206, 80, 209, 75, 213, 73, 213, 70, 216, 67, 219, 64, 221, 61, 223,
        60, 225, 62, 226, 65, 225, 67, 226, 74, 226, 77, 227, 85, 229, 91, 230, 99, 231, 108, 232,
        116, 233, 125, 233, 134, 234, 145, 233, 153, 232, 160, 233, 170, 234, 177, 235, 179, 236,
        186, 237, 193, 238, 198, 239, 200, 237, 202, 239, 204, 238, 206, 234, 205, 230, 202, 222,
        197, 216, 192, 207, 186, 198, 179, 189, 174, 183, 170, 178, 164, 171, 161, 168, 154, 160,
        148, 155, 143, 150, 138, 148, 136, 148});
    one.add("circle", new Integer[] {127, 141, 124, 140, 120, 139, 118, 139, 116, 139, 111, 140,
        109, 141, 104, 144, 100, 147, 96, 152, 93, 157, 90, 163, 87, 169, 85, 175, 83, 181, 82,
        190, 82, 195, 83, 200, 84, 205, 88, 213, 91, 216, 96, 219, 103, 222, 108, 224, 111, 224,
        120, 224, 133, 223, 142, 222, 152, 218, 160, 214, 167, 210, 173, 204, 178, 198, 179, 196,
        182, 188, 182, 177, 178, 167, 170, 150, 163, 138, 152, 130, 143, 129, 140, 131, 129, 136,
        126, 139});
    // ... find more gestures in 'Gestures.pde'
    // http://depts.washington.edu/aimgroup/proj/dollar/unistrokes.gif

    // bind callbacks:
    one.bind("circle", "detected");
    one.bind("triangle", "detected");

    one.start(100);
  }


  void detected(String gesture, int x, int y, int c_x, int c_y) {
    gesture_name = gesture;
    position.x = x;
    position.y = y;
    centroid.x = c_x;
    centroid.y = c_y;
  }


  public void draw() {
    background(17);
    //println(frameRate);
    // draw circle background shape
    noFill();
    stroke(30);
    if (gesture_name.equals("circle")) {
      stroke(100);
    }
    ellipse(width / 10 * 3, height / 2, 200, 200);

    // draw triangle background shape
    stroke(30);
    if (gesture_name.equals("triangle")) {
      stroke(100);
    }
    pushMatrix();
    translate(width / 10 * 7, height / 2);
    triangle(0, -100, 100, 100, -100, 100);
    popMatrix();

    fill(254);
    noStroke();
    text("Detected gesture: " + gesture_name, 30, 40);
    text("Draw a circle or triangle.", 30, height - 30);

    fill(0, 70, 245);
    text("Position: " + (int) position.x + " / " + (int) position.y, 30, 60);
    ellipse(position.x, position.y, 5, 5);

    fill(0, 238, 174);
    text("Centroid: " + (int) centroid.x + " / " + (int) centroid.y, 30, 80);
    ellipse(centroid.x, centroid.y, 5, 5);

    noFill();
    stroke(220);
    one.draw(); // draw the relevant candidates
    one.check(); // run the gesture detection
    com.leapmotion.leap.Frame frame = leap.getCurrentFrame();
    if (frame.hands().empty() == false) {
      com.leapmotion.leap.Hand hand = frame.hands().get(0);
      com.leapmotion.leap.FingerList fingers = hand.fingers();
      if (fingers.empty() == false) {
        com.leapmotion.leap.Finger finger = fingers.get(0);
        Vector position = finger.tipPosition();
        one.update(100, leap.leapToScreenX(position.getX()), leap.leapToScreenY(position.getY()));
      }
    }
  }

  public void stop() {
    leap.stop();
    super.stop();
  }

}
