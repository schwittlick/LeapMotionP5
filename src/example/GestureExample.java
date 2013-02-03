package example;

import java.util.ArrayList;

import com.leapmotion.leap.Finger;
import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Tool;
import com.leapmotion.leap.Vector;
import com.onformative.leap.LeapMotionP5;
import com.onformative.leap.gestures.Gesture;
import com.onformative.leap.gestures.PullGesture;
import com.onformative.leap.gestures.PushGesture;
import com.onformative.leap.gestures.SwipeDownGesture;
import com.onformative.leap.gestures.SwipeLeftGesture;
import com.onformative.leap.gestures.SwipeRightGesture;
import com.onformative.leap.gestures.SwipeUpGesture;

import controlP5.ControlP5;

import processing.core.PApplet;
import processing.core.PFont;

public class GestureExample extends PApplet {

  LeapMotionP5 leap;
  SwipeLeftGesture sl;
  SwipeRightGesture sr;
  SwipeUpGesture su;
  SwipeDownGesture sd;
  PushGesture pg;
  PullGesture pug;

  ControlP5 cp5;
  boolean showGui = false;

  public void setup() {
    size(500, 500);
    leap = new LeapMotionP5(this);

    sl = new SwipeLeftGesture(this, leap);
    sr = new SwipeRightGesture(this, leap);
    su = new SwipeUpGesture(this, leap);
    sd = new SwipeDownGesture(this, leap);
    pg = new PushGesture(this, leap);
    pug = new PullGesture(this, leap);

    cp5 = new ControlP5(this);
    cp5.setAutoDraw(false);

    cp5.addSlider("velocityThreshold", 0, 2500, 1500, 20, 20, 100, 10);
    cp5.addSlider("gestureTimeoutMillis", 0, 1000, 100, 20, 40, 100, 10);
  }

  public void velocityThreshold(int val) {
    sl.setVelocityThreshold(val);
    sr.setVelocityThreshold(val);
    su.setVelocityThreshold(val);
    sd.setVelocityThreshold(val);
    pg.setVelocityThreshold(val);
    pug.setVelocityThreshold(val);
  }

  public void gestureTimeoutMillis(int val) {
    sl.setGestureTimeoutMillis(val);
    sr.setGestureTimeoutMillis(val);
    su.setGestureTimeoutMillis(val);
    sd.setGestureTimeoutMillis(val);
    pg.setGestureTimeoutMillis(val);
    pug.setGestureTimeoutMillis(val);
  }

  public void draw() {
    fill(0, 40);
    noStroke();
    rect(0, 0, width, height);

    if (sl.check()) {
      System.out.println("swipe left");
      fill(255, 0, 0);
      rect(0, 0, width, height);
    } else if (sr.check()) {
      System.out.println("swipe right");
      fill(0, 255, 0);
      rect(0, 0, width, height);
    } else if (su.check()) {
      System.out.println("swipe up");
      fill(0, 0, 255);
      rect(0, 0, width, height);
    } else if (sd.check()) {
      System.out.println("swipe down");
      fill(255, 255, 0);
      rect(0, 0, width, height);
    } else if (pg.check()) {
      System.out.println("pushed");
      fill(255, 0, 255);
      rect(0, 0, width, height);
    } else if (pug.check()) {
      System.out.println("pulled");
      fill(0, 255, 255);
      rect(0, 0, width, height);
    }

    if (showGui) {
      cp5.draw();
    }
  }

  public void keyPressed() {
    if (key == ' ') {
      showGui = !showGui;
    }
  }

  public void stop() {
    leap.stop();
    super.stop();
  }
}
