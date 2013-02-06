package example;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Hand;
import com.onformative.leap.LeapMotionP5;

import processing.core.PApplet;
import processing.core.PVector;

public class ActiveFingersExample extends PApplet {
  LeapMotionP5 leap;

  public void setup() {
    size(500, 500);
    textSize(30);
    leap = new LeapMotionP5(this);
  }

  public void draw() {
    background(0);
    fill(255);
    int counter = 0;
    for (Finger finger : leap.getFingerList()) {
      PVector fingerPos = leap.convertFingerToPVector(finger);
      
      ellipse(fingerPos.x, fingerPos.y, 10, 10);
      pushMatrix();
      translate(fingerPos.x+5, fingerPos.y+5);
      text(finger.id(), 0, 0);
      popMatrix();
      counter++;
    }
    
    for(Hand hand : leap.getHandList()){
      PVector handPos = leap.convertHandToPVector(hand);
      pushMatrix();
      translate(handPos.x+5, handPos.y+5);
      text(hand.id(), 0, 0);
      popMatrix();
    }
  }

  public void stop() {
    leap.stop();
  }
}
