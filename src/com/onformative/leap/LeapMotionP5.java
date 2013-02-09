package com.onformative.leap;

/**
 * LeapMotionP5
 * 
 * LeapMotionP5 library for Processing. Copyright (c) 2012-2013 held jointly by the individual
 * authors.
 * 
 * LeapMotionP5 library for Processing is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * 
 * LeapMotionP5 for Processing is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with LeapMotionP5 library
 * for Processing. If not, see http://www.gnu.org/licenses/.
 * 
 * Leap Developer SDK. Copyright (C) 2012-2013 Leap Motion, Inc. All rights reserved.
 * 
 * NOTICE: This developer release of Leap Motion, Inc. software is confidential and intended for
 * very limited distribution. Parties using this software must accept the SDK Agreement prior to
 * obtaining this software and related tools. This software is subject to copyright.
 */

import java.util.ArrayList;
import java.util.LinkedList;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.Tool;
import com.leapmotion.leap.Vector;
import com.onformative.leap.gestures.GestureHandler;

/**
 * LeapMotionP5.java
 * 
 * @author Marcel Schwittlick
 * @modified 04.02.2013
 * 
 */
public class LeapMotionP5 {
  private PApplet p;
  private LeapMotionListener listener;
  private Controller controller;

  protected LinkedList<Frame> lastFrames;

  protected Frame currentFrame;

  protected Finger lastDetectedFinger;
  protected Pointable lastDetectedPointable;
  protected Hand lastDetectedHand;
  protected Tool lastDetectedTool;

  private float LEAP_WIDTH = 200.0f; // in mm
  private float LEAP_HEIGHT = 500.0f; // in mm
  private float LEAP_DEPTH = 200.0f; // in mm

  public static final String CIRCLE = "circle";
  public static final String TRIANGLE = "triangle";
  public static final String RECTANGLE = "rectangle";
  public static final String X = "x";
  public static final String CHECK = "check";
  public static final String CHARET = "charet";
  public static final String ZIG_ZAG = "zig-zag";
  public static final String ARROW = "arrow";
  public static final String LEFT_SQUARE_BRACKET = "leftsquarebracket";
  public static final String RIGHT_SQUARE_BRACKET = "rightsquarebracket";
  public static final String LEFT_CURLY_BRACKET = "leftcurlybrace";
  public static final String RIGHT_CURLY_BRACKET = "rightcurlybrace";
  public static final String V = "v";
  public static final String DELETE = "delete";
  public static final String STAR = "star";
  public static final String PIGTAIL = "pigtail";

  public static final String SWIPE_LEFT = "swipeleft";
  public static final String SWIPE_RIGHT = "swiperight";
  public static final String SWIPE_UP = "swipeup";
  public static final String SWIPE_DOWN = "swipedown";
  public static final String PUSH = "push";
  public static final String PULL = "pull";
  public static final String ON_HAND_ENTER = "onhandenter";
  public static final String ON_HAND_LEAVE = "onhandleave";

  public static final String ON_FINGER_ENTER = "onfingerenter";
  public static final String ON_FINGER_LEAVE = "onfingerleave";

  public GestureHandler gestures;

  /**
   * TODO: methods to access fingers by type of finger (pointing finger, middlefinger, etc) check
   * this: https://github.com/alexopalka/determineFingers
   * 
   * @param p
   */
  public LeapMotionP5(PApplet p) {
    this.p = p;

    listener = new LeapMotionListener(this);
    controller = new Controller();

    controller.addListener(listener);

    lastDetectedFinger = new Finger();
    lastDetectedPointable = new Pointable();
    lastDetectedHand = new Hand();
    lastDetectedTool = new Tool();

    gestures = new GestureHandler(p, this);
  }

  /**
   * 
   */
  public void start() {
    gestures.start();
  }

  /**
   * 
   */
  public void stop() {
    try {
      gestures.stop();
    } catch (Exception e) {
      System.err.println("Gestures not initialized. Can not stop gesture recognition.");
    }

    controller.removeListener(listener);
  }

  /**
   * 
   * @param gestureName
   */
  public void addGesture(String gestureName) {
    try {
      gestures.addGesture(gestureName);
    } catch (Exception e) {
      System.err.println("Can not add gestures.");
      System.err.println(e);
    }
  }

  /**
   * 
   * @return
   */
  public PApplet getParent() {
    try {
      return p;
    } catch (Exception e) {
      System.err.println("Can not return parent PApplet. Returning new PApplet object instead");
      System.out.println(e);
      return new PApplet();
    }
  }

  /**
   * 
   * @return
   */
  public Controller getController() {
    try {
      return controller;
    } catch (Exception e) {
      System.err
          .println("Can not return controller not initialized. Returning new Controller object");
      System.out.println(e);
      return new Controller();
    }
  }

  /**
   * 
   */
  public void update() {
    try {
      gestures.update();
    } catch (Exception e) {
      System.err.println("Can not update gesture recognition.");
      System.err.println(e);
    }
  }

  /**
   * 
   * @return
   */
  public Frame getFrame() {
    try {
      return currentFrame;
    } catch (Exception e) {
      System.err.println("Can not return current frame. Returning new Frame object instead");
      System.err.println(e);
      return new Frame();
    }
  }

  public Frame getLastFrame() {
    return getFrames().get(getFrames().size() - 2);
  }

  /**
   * 
   * @return
   */
  public LinkedList<Frame> getFrames() {
    try {
      return lastFrames;
    } catch (Exception e) {
      System.err.println("Can not return list of last frames. Returning empty list instead.");
      System.err.println(e);
      return new LinkedList<Frame>();
    }
  }

  /**
   * the last entry in the linkedlist ist the newest frame and the first one is the oldest
   * 
   * @param frameCount
   * @return
   */
  public LinkedList<Frame> getFrames(int frameCount) {
    LinkedList<Frame> frames = new LinkedList<Frame>();
    for (int i = 0; i < frameCount; i++) {
      if (getFrames().size() > frameCount) {
        Frame fr = getFrames().get(getFrames().size() - frameCount + i);
        frames.add(fr);
      }
    }
    return frames;
  }

  /**
   * 
   * @return
   */
  public PVector getFingerPositionXYPlane() {
    PVector fingerPositionXYPlane = new PVector();

    Frame frame = getFrame();
    if (frame.hands().empty() == false) {
      Hand hand = frame.hands().get(0);
      if (hand.fingers().empty() == false) {
        Finger finger = hand.fingers().get(0);
        fingerPositionXYPlane.x = leapToScreenX(finger.tipPosition().getX());
        fingerPositionXYPlane.y = leapToScreenY(finger.tipPosition().getY());
      }
    }

    return fingerPositionXYPlane;
  }

  /**
   * 
   * @param x
   * @return
   */
  public float leapToScreenX(float x) {
    /*
     * int startX = -243; int endX = 256; float valueMapped = PApplet.map(x, startX, endX, 0,
     * p.width); return valueMapped;
     */
    float c = p.width / 2.0f;
    if (x > 0.0) {
      return PApplet.lerp(c, p.width, x / LEAP_WIDTH);
    } else {
      return PApplet.lerp(c, 0.0f, -x / LEAP_WIDTH);
    }
  }

  /**
   * 
   * @param y
   * @return
   */
  public float leapToScreenY(float y) {
    /*
     * int startY = 50; int endY = 350; float valueMapped = PApplet.map(y, startY, endY, 0,
     * p.height); return valueMapped;
     */
    return PApplet.lerp(p.height, 0.0f, y / LEAP_HEIGHT);
  }

  /**
   * 
   * @param z
   * @return
   */
  public float leapToScreenZ(float z) {
    /*
     * int startZ = -51; int endZ = 149; float valueMapped = PApplet.map(z, startZ, endZ, 0,
     * p.width); return valueMapped;
     */
    return PApplet.lerp(0, p.width, z / LEAP_DEPTH);
  }

  /**
   * 
   * @param vector
   * @return
   */
  public PVector convertVectorToPVector(Vector vector) {
    return convertToScreenDimension(vector.getX(), vector.getY(), vector.getZ());
  }

  /**
   * converts x, y and z coordinates of the leap to the dimensions of your sketch
   * 
   * @param x x position in leap world coordinate system
   * @param y y position in leap world coordinate system
   * @param z z position in leap world coordinate system
   * @return PVector the pvector of the point you passed in converted to the dimensions of your
   *         processing sketch window
   */
  public PVector convertToScreenDimension(float x, float y, float z) {
    PVector positionRelativeToFrame = new PVector();
    positionRelativeToFrame.x = leapToScreenX(x);
    positionRelativeToFrame.y = leapToScreenY(y);
    positionRelativeToFrame.z = leapToScreenZ(z);
    return positionRelativeToFrame;
  }

  /**
   * 
   * @return ArrayList<Hand> an arraylist containing all currently tracked hands
   */
  public ArrayList<Hand> getHandList() {
    ArrayList<Hand> hands = new ArrayList<Hand>();
    Frame frame = getFrame();
    if (frame.hands().empty() == false) {
      for (Hand hand : frame.hands()) {
        hands.add(hand);
      }
    }
    return hands;
  }

  /**
   * 
   * @return ArrayList<Hand> an arraylist containing all currently tracked hands
   */
  public ArrayList<Hand> getHandList(Frame frame) {
    ArrayList<Hand> hands = new ArrayList<Hand>();
    if (frame.hands().empty() == false) {
      for (Hand hand : frame.hands()) {
        if (hand.isValid()) {
          hands.add(hand);
        }
      }
    }
    return hands;
  }

  /**
   * 
   * @param handNr
   * @return
   */
  public Hand getHand(int handNr) {
    if (!getHandList().isEmpty()) {
      try {
        lastDetectedHand = getHandList().get(handNr);
      } catch (IndexOutOfBoundsException e) {
        // ignore
      }
    }
    return lastDetectedHand;
  }

  /**
   * 
   * @param hand
   * @return
   */
  public float getPitch(Hand hand) {
    // return PApplet.map((float) Math.toDegrees(hand.direction().pitch()), 0, 22, 0,
    // PConstants.TWO_PI);
    return (float) Math.toDegrees(hand.direction().pitch());
  }

  /**
   * 
   * @param hand
   * @return
   */
  public float getRoll(Hand hand) {
    return -PApplet.map((float) Math.toDegrees(hand.direction().roll()), 0, 180, 0,
        PConstants.TWO_PI);
  }

  /**
   * 
   * @param hand
   * @return
   */
  public float getYaw(Hand hand) {
    return (float) Math.toDegrees(hand.direction().yaw());
  }

  /**
   * 
   * @param hand
   * @return
   */
  public PVector getDirection(Hand hand) {
    return convertVectorToPVector(hand.direction());
  }

  /**
   * 
   * @param hand
   * @return
   */
  public PVector getPosition(Hand hand) {
    return convertVectorToPVector(hand.palmPosition());
  }

  /**
   * 
   * @param hand
   * @return
   */
  public PVector getNormal(Hand hand) {
    return convertVectorToPVector(hand.palmNormal());
  }

  /**
   * 
   * @param hand
   * @return
   */
  public PVector getVelocity(Hand hand) {
    return convertVectorToPVector(hand.palmVelocity());
  }

  /**
   * 
   * @return ArrayList<Finger> an arraylist containing all currently tracked fingers
   */
  public ArrayList<Finger> getFingerList() {
    ArrayList<Finger> fingers = new ArrayList<Finger>();

    Frame frame = getFrame();
    if (frame.hands().empty() == false) {
      for (Hand hand : frame.hands()) {
        fingers.addAll(getFingerList(hand));
      }
    }

    return fingers;
  }

  /**
   * 
   * @param frame
   * @return
   */
  public ArrayList<Finger> getFingerList(Frame frame) {
    ArrayList<Finger> fingers = new ArrayList<Finger>();

    if (frame.hands().empty() == false) {
      for (Hand hand : frame.hands()) {
        fingers.addAll(getFingerList(hand));
      }
    }

    return fingers;
  }

  /**
   * 
   * @param hand
   * @return
   */
  public ArrayList<Finger> getFingerList(Hand hand) {
    ArrayList<Finger> fingers = new ArrayList<Finger>();

    for (Finger finger : hand.fingers()) {
      fingers.add(finger);
    }
    return fingers;
  }

  /**
   * accessing a finger by number. the number indicates the fingernumber. for example: when there
   * are two fingers detected by the leap you can access the first one by getFinger(0) and the
   * second one by getFinger(1). this is a comfortable way to quickly access the leap in processing
   * 
   * @param fingerNr
   * @return
   */
  public Finger getFinger(int fingerNr) {
    if (!getFingerList().isEmpty()) {
      try {
        lastDetectedFinger = getFingerList().get(fingerNr);
      } catch (IndexOutOfBoundsException e) {
        // ignore
      }
    }
    return lastDetectedFinger;
  }

  /**
   * 
   * @param pointable
   * @return
   */
  public PVector getTip(Pointable pointable) {
    return convertToScreenDimension(pointable.tipPosition().getX(), pointable.tipPosition().getY(),
        pointable.tipPosition().getZ());
  }



  /**
   * 
   * @param pointable
   * @return
   */
  public PVector getOrigin(Pointable pointable) {
    Vector anklePos;

    float length = pointable.length();
    PVector direction = new PVector();
    direction.x = pointable.direction().getX();
    direction.y = pointable.direction().getY();
    direction.z = pointable.direction().getZ();
    direction.mult(length);
    anklePos =
        new Vector(pointable.tipPosition().getX() - direction.x, pointable.tipPosition().getY()
            - direction.y, pointable.tipPosition().getZ() - direction.z);

    return convertVectorToPVector(anklePos);
  }

  /**
   * 
   * @param pointable
   * @return
   */
  public PVector getVelocity(Pointable pointable) {
    // return convertVectorToPVector(pointable.tipVelocity());
    return convertVectorToPVector(pointable.tipVelocity());
  }



  /**
   * 
   * @param pointable
   * @return
   */
  public PVector getDirection(Pointable pointable) {
    return convertVectorToPVector(pointable.direction());
  }

  /**
   * 
   * @param pointable
   * @return
   */
  public float getLength(Pointable pointable) {
    return pointable.length();
  }

  /**
   * 
   * @param pointable
   * @return
   */
  public float getWidth(Pointable pointable) {
    return pointable.width();
  }

  /**
   * 
   * @return ArrayList<Pointable> an arraylist containing all currently tracked pointables
   */
  public ArrayList<Pointable> getPointableList() {
    ArrayList<Pointable> pointables = new ArrayList<Pointable>();

    Frame frame = getFrame();
    if (frame.hands().empty() == false) {
      for (Hand hand : frame.hands()) {
        pointables.addAll(getPointableList(hand));
      }
    }

    return pointables;
  }

  /**
   * 
   * @return ArrayList<Pointable> an arraylist containing all currently tracked pointables
   */
  public ArrayList<Pointable> getPointableList(Frame frame) {
    ArrayList<Pointable> pointables = new ArrayList<Pointable>();

    if (frame.hands().empty() == false) {
      for (Hand hand : frame.hands()) {
        pointables.addAll(getPointableList(hand));
      }
    }

    return pointables;
  }

  public ArrayList<Pointable> getPointableList(Hand hand) {
    ArrayList<Pointable> pointables = new ArrayList<Pointable>();

    for (Pointable pointable : hand.pointables()) {
      pointables.add(pointable);
    }

    return pointables;
  }

  /**
   * 
   * @param pointableNr
   * @return
   */
  public Pointable getPointable(int pointableNr) {
    if (!getPointableList().isEmpty()) {
      try {
        lastDetectedPointable = getPointableList().get(pointableNr);
      } catch (IndexOutOfBoundsException e) {
        // ignore
      }
    }
    return lastDetectedPointable;
  }

  /**
   * 
   * @return
   */
  public ArrayList<Tool> getToolList() {
    ArrayList<Tool> tools = new ArrayList<Tool>();

    Frame frame = getFrame();
    if (frame.hands().empty() == false) {
      for (Hand hand : frame.hands()) {
        tools.addAll(getToolList(hand));
      }
    }

    return tools;
  }

  /**
   * 
   * @return
   */
  public ArrayList<Tool> getToolList(Frame frame) {
    ArrayList<Tool> tools = new ArrayList<Tool>();

    if (frame.hands().empty() == false) {
      for (Hand hand : frame.hands()) {
        tools.addAll(getToolList(hand));
      }
    }

    return tools;
  }

  /**
   * 
   * @param hand
   * @return
   */
  public ArrayList<Tool> getToolList(Hand hand) {
    ArrayList<Tool> tools = new ArrayList<Tool>();

    for (Tool tool : hand.tools()) {
      tools.add(tool);
    }

    return tools;
  }

  /**
   * 
   * @param toolNr
   * @return
   */
  public Tool getTool(int toolNr) {
    if (!getToolList().isEmpty()) {
      try {
        lastDetectedTool = getToolList().get(toolNr);
      } catch (IndexOutOfBoundsException e) {
        // ignore
      }
    }
    return lastDetectedTool;
  }
}
