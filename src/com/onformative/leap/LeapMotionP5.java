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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;

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
  protected ConcurrentSkipListMap<Date, Frame> lastFramesInclProperTimestamps;

  protected Frame currentFrame;

  protected HashMap<Integer, Finger> lastDetectedFinger;
  protected HashMap<Integer, Pointable> lastDetectedPointable;
  protected HashMap<Integer, Hand> lastDetectedHand;
  protected HashMap<Integer, Tool> lastDetectedTool;

  private float LEAP_WIDTH = 200.0f; // in mm
  private float LEAP_HEIGHT = 500.0f; // in mm
  private float LEAP_DEPTH = 200.0f; // in mm

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

    lastDetectedFinger = new HashMap<Integer, Finger>();
    lastDetectedPointable = new HashMap<Integer, Pointable>();
    lastDetectedHand = new HashMap<Integer, Hand>();
    lastDetectedTool = new HashMap<Integer, Tool>();

    lastDetectedFinger.put(0, new Finger());
    lastDetectedPointable.put(0, new Pointable());
    lastDetectedHand.put(0, new Hand());
    lastDetectedTool.put(0, new Tool());

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

  /**
   * 
   * @param id
   * @return
   */
  public Frame getFrame(int id) {
    Frame returnFrame = new Frame();
    for (Frame frame : getFrames()) {
      if (frame.id() >= id) {
        returnFrame = frame;
      }
    }
    return returnFrame;
  }

  /**
   * 
   * @return
   */
  public Frame getLastFrame() {
    return getFrames().get(getFrames().size() - 2);
  }

  public Frame getFrameBeforeFrame(Frame frame) {
    Frame frameBefore = null;
    for (int i = 0; i < getFrames().size() - 1; i++) {
      try {
        if (getFrames().get(i).equals(frame)) {
          try {

            frameBefore = getFrames().get(i - 1);
            if (frame.equals(frameBefore)) {
              System.out.println("SAME FRAMEE");
            }
          } catch (Exception e) {
            // ignore
          }
        }
      } catch (Exception e) {
        frameBefore = frame;
      }
    }
    return frameBefore;
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
    try {
      if (frame.hands().empty() == false) {
        for (Hand hand : frame.hands()) {
          if (hand.isValid()) {
            hands.add(hand);
          }
        }
      }
    } catch (Exception e) {
      // ignore
    }
    return hands;
  }

  /**
   * 
   * @param handNr
   * @return
   */
  public Hand getHand(int handNr) {
    Hand returnHand = null;
    if (!getHandList().isEmpty()) {
      lastDetectedHand.put(handNr, getHandList().get(handNr));
    }
    returnHand = lastDetectedHand.get(handNr);
    int downCounter = 0;
    while (returnHand == null) {
      returnHand = lastDetectedHand.get(handNr - downCounter);
      downCounter++;
    }
    return returnHand;
  }

  /**
   * 
   * @param handNr
   * @return
   */
  /*
   * public Hand getHand(int handNr, Frame frame) { if (!getHandList(frame).isEmpty()) { try {
   * lastDetectedHand = getHandList(frame).get(handNr); } catch (IndexOutOfBoundsException e) { //
   * ignore } } return lastDetectedHand; }
   */

  public Hand getHandById(int id, Frame frame) {
    Hand returnHand = null;
    for (Hand hand : getHandList(frame)) {
      if (hand.id() == id) {
        returnHand = hand;
      }
    }
    return returnHand;
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
    //return -PApplet.map((float) Math.toDegrees(hand.direction().roll()), 0, 180, 0,
    //    PConstants.TWO_PI);
    return (float) Math.toDegrees(hand.palmNormal().roll());
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


  public PVector getAcceleration(Hand hand) {
    PVector acceleration = null;


    Frame currentFrame = getFrame();
    Frame lastFrame = getFrameBeforeFrame(currentFrame);
    PVector currentVelo = new PVector();
    PVector lastVelo = new PVector();
    try {
      currentVelo = getVelocity(hand);
      lastVelo = getVelocity(getHandById(hand.id(), lastFrame));
    } catch (Exception e) {
      // ignore
    }
    currentVelo.sub(lastVelo);
    currentVelo.div(2);
    acceleration = currentVelo;

    return acceleration;
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
   * 
   * @param fingerNr
   * @return
   */
  public Finger getFinger(int fingerNr) {
    Finger returnFinger = null;
    if (!getFingerList().isEmpty()) {
      lastDetectedFinger.put(fingerNr, getFingerList().get(fingerNr));
    }
    returnFinger = lastDetectedFinger.get(fingerNr);
    int downCounter = 0;
    while (returnFinger == null) {
      returnFinger = lastDetectedFinger.get(fingerNr - downCounter);
      downCounter++;
    }
    return returnFinger;
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

  public PVector getAcceleration(Pointable pointable) {

    PVector acceleration = null;

    Frame currentFrame = getFrame();
    Frame lastFrame = getFrameBeforeFrame(currentFrame);
    PVector currentVelo = new PVector();
    PVector lastVelo = new PVector();
    try {
      currentVelo = getVelocity(pointable);
      lastVelo = getVelocity(getPointableById(pointable.id(), lastFrame));
    } catch (Exception e) {
      // ignore
    }
    currentVelo.sub(lastVelo);
    currentVelo.div(2);
    acceleration = currentVelo;

    return acceleration;
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
    Pointable returnPointable = null;
    if (!getPointableList().isEmpty()) {
      lastDetectedPointable.put(pointableNr, getPointableList().get(pointableNr));
    }
    returnPointable = lastDetectedPointable.get(pointableNr);
    int downCounter = 0;
    while (returnPointable == null) {
      returnPointable = lastDetectedPointable.get(pointableNr - downCounter);
      downCounter++;
    }
    return returnPointable;
  }

  public Pointable getPointableById(int id, Frame frame) {
    Pointable returnPointable = null;
    for (Pointable pointable : getPointableList(frame)) {
      if (pointable.id() == id) {
        returnPointable = pointable;
      }
    }
    return returnPointable;
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
    Tool returnTool = null;
    if (!getToolList().isEmpty()) {
      lastDetectedTool.put(toolNr, getToolList().get(toolNr));
    }
    returnTool = lastDetectedTool.get(toolNr);
    int downCounter = 0;
    while (returnTool == null) {
      returnTool = lastDetectedTool.get(toolNr - downCounter);
      downCounter++;
    }
    return returnTool;
  }

  /*
   * public PVector getAcceleration(Hand hand) {
   * 
   * PVector acceleration; try { Frame currentFrame = hand.frame(); Frame lastFrame = getFrame((int)
   * (hand.frame().id() + 1));
   * 
   * System.out.println("getAcceleration(hand)"); PVector currentVelo = getVelocity(getHand(0,
   * currentFrame)); PVector lastVelo = getVelocity(getHand(0, lastFrame));
   * System.out.println("currentV: " + currentVelo); System.out.println("lastV   : " + lastVelo);
   * currentVelo.sub(lastVelo); currentVelo.div(2); acceleration = currentVelo;
   * System.out.println("acceleration: " + acceleration);
   * 
   * } catch (Exception e) { System.err.println(e); acceleration = new PVector(); }
   * 
   * return acceleration; }
   */

  public Date getTimestamp(Frame frame) {
    Date date = null;
    Set<Entry<Date, Frame>> lastFramesInclDates = lastFramesInclProperTimestamps.entrySet();
    Iterator<Entry<Date, Frame>> i = lastFramesInclDates.iterator();
    while (i.hasNext()) {
      Entry<Date, Frame> entry = i.next();
      String stringOfTimestampInMap = entry.getValue().timestamp() + "";
      String stringOfTimestampPassedParameter = frame.timestamp() + "";
      if (stringOfTimestampInMap.equals(stringOfTimestampPassedParameter)) {
        date = entry.getKey();
      }
    }
    return date;
  }
}
