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
import processing.core.PVector;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Matrix;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.Tool;
import com.onformative.leap.gestures.GestureHandler;

/**
 * LeapMotionP5.java
 * 
 * @author Marcel Schwittlick
 * @modified 04.02.2013
 * 
 *           the main class of the library. this class has to be instanciated in order to access
 *           leap data within processing
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

  public GestureHandler gestures;

  /**
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
    if (gestures != null) {
      gestures.stop();
    }
    controller.removeListener(listener);
  }

  /**
   * 
   * @param gestureName
   */
  public void addGesture(String gestureName) {
    gestures.addGesture(gestureName);
  }

  /**
   * 
   * @return
   */
  public PApplet getParent() {
    return p;
  }

  /**
   * 
   * @return
   */
  public Controller getController() {
    return controller;
  }

  /**
   * 
   */
  public void update() {
    if (gestures != null) {
      gestures.update();
    }
  }

  /**
   * 
   * @return
   */
  public Frame getFrame() {
    if (currentFrame != null) {
      return currentFrame;
    } else {
      return new Frame();
    }
  }

  /**
   * 
   * @return
   */
  public LinkedList<Frame> getFrames() {
    return lastFrames;
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
   * @param finger
   * @return
   */
  public PVector convertFingerToPVector(Finger finger) {
    return convertToScreenDimension(finger.tipPosition().getX(), finger.tipPosition().getY(),
        finger.tipPosition().getZ());
  }

  /**
   * 
   * @param pointable
   * @return
   */
  public PVector convertPointableToPVector(Pointable pointable) {
    return convertToScreenDimension(pointable.tipPosition().getX(), pointable.tipPosition().getY(),
        pointable.tipPosition().getZ());
  }

  /**
   * 
   * @param hand
   * @return
   */
  public PVector convertHandToPVector(Hand hand) {
    return convertToScreenDimension(hand.palmPosition().getX(), hand.palmPosition().getY(), hand
        .palmPosition().getZ());
  }

  /**
   * 
   * @param hand
   * @return
   */
  public PVector convertToolToPVector(Tool tool) {
    return convertToScreenDimension(tool.tipPosition().getX(), tool.tipPosition().getY(), tool
        .tipPosition().getZ());
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
        hands.add(hand);
      }
    }
    return hands;
  }

  /**
   * 
   * @return int the number of currently tracked hands
   */
  public int getHandCount() {
    return getHandList().size();
  }

  /**
   * 
   * @return int the number of currently tracked hands
   */
  public int getHandCount(Frame frame) {
    return getHandList(frame).size();
  }

  /**
   * 
   * @param handNr
   * @return
   */
  public Hand getHand(int handNr) {
    if (!getHandList().isEmpty()) {
      lastDetectedHand = getHandList().get(handNr);
    }
    return lastDetectedHand;
  }

  /**
   * 
   * @param handNr
   * @return
   */
  public PVector getHandPVector(int handNr) {
    return convertHandToPVector(getHand(handNr));
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
   * @return
   */
  public int getFingerCount() {
    return getFingerList().size();
  }

  /**
   * 
   * @return
   */
  public int getFingerCount(Frame frame) {
    return getFingerList(frame).size();
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
   * @return
   */
  public int getFingerCount(Hand hand) {
    return getFingerList(hand).size();
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
      lastDetectedFinger = getFingerList().get(fingerNr);
    }
    return lastDetectedFinger;
  }

  public PVector getFingerPVector(int fingerNr) {
    return convertFingerToPVector(getFinger(fingerNr));
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

  /**
   * 
   * @return int the number of currently tracked pointables
   */
  public int getPointableCount() {
    return getPointableList().size();
  }

  /**
   * 
   * @return int the number of currently tracked pointables
   */
  public int getPointableCount(Frame frame) {
    return getPointableList(frame).size();
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
   * @return int the number of currently tracked pointables
   */
  public int getPointableCount(Hand hand) {
    return getPointableList(hand).size();
  }

  /**
   * 
   * @param pointableNr
   * @return
   */
  public Pointable getPointable(int pointableNr) {
    if (!getPointableList().isEmpty()) {
      lastDetectedPointable = getPointableList().get(pointableNr);
    }
    return lastDetectedPointable;
  }

  /**
   * 
   * @param pointableNr
   * @return
   */
  public PVector getPointablePVector(int pointableNr) {
    return convertPointableToPVector(getPointable(pointableNr));
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
   * @return
   */
  public int getToolCount() {
    return getToolList().size();
  }

  /**
   * 
   * @return
   */
  public int getToolCount(Frame frame) {
    return getToolList(frame).size();
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
   * @return
   */
  public int getToolCount(Hand hand) {
    return getToolList(hand).size();
  }

  /**
   * 
   * @param toolNr
   * @return
   */
  public Tool getTool(int toolNr) {
    if (!getToolList().isEmpty()) {
      lastDetectedTool = getToolList().get(toolNr);
    }
    return lastDetectedTool;
  }

  /**
   * 
   * @param toolNr
   * @return
   */
  public PVector getToolPVector(int toolNr) {
    return convertToolToPVector(getTool(toolNr));
  }

  public Matrix getRotationsMatrix(Hand hand) {
    return hand.rotationMatrix(currentFrame);
  }
}
