package example;

import java.util.LinkedList;
import java.util.Map;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Vector;
import com.onformative.leap.LeapMotionP5;
import com.onformative.leap.gestures.PullGesture;
import com.onformative.leap.gestures.PushGesture;
import com.onformative.leap.gestures.SwipeDownGesture;
import com.onformative.leap.gestures.SwipeLeftGesture;
import com.onformative.leap.gestures.SwipeRightGesture;
import com.onformative.leap.gestures.SwipeUpGesture;
import com.onformative.leap.gestures.VictoryGesture;

import controlP5.ControlP5;

import processing.core.PApplet;
import processing.core.PVector;

public class GestureExample extends PApplet {
LeapMotionP5 leap;

  public void setup() {
    size(500, 500);
    leap = new LeapMotionP5(this);

    
  }


  public void draw() {
    background(0);
    for (Finger finger : leap.getFingerList()) {
      PVector fingerPos = leap.convertFingerToPVector(finger);
      ellipse(fingerPos.x, fingerPos.y, 10, 10);
    }
    
    if(check()){
      System.out.println("hey");
    }
  }

  public boolean check() {
    boolean returnValue = false;


    LinkedList<Frame> last10Frames = leap.getFrames(10);
    for (Frame frame : last10Frames) {
      int fingerCount = leap.getFingerCount(frame);
      if (fingerCount == 2) {
        long frameNr = frame.id();

        for (Frame frame2 : last10Frames) {
          if (frame2.id() > frameNr) {
            int fc = leap.getFingerCount(frame2);
            if (fc == 3) {
              returnValue = true;
            }
          }
        }
      }

    }
    return returnValue;
  }

  public void stop() {
    leap.stop();
    super.stop();
  }
}
