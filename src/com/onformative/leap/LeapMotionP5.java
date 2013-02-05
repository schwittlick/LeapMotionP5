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
import java.util.concurrent.ConcurrentMap;

import processing.core.PApplet;
import processing.core.PVector;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.Tool;
import com.leapmotion.leap.Vector;

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

  private float LEAP_WIDTH = 200.0f; // in mm
  private float LEAP_HEIGHT = 500.0f; // in mm
  private float LEAP_DEPTH = 200.0f; // in mm

  public LeapMotionP5(PApplet p) {
    this.p = p;

    listener = new LeapMotionListener(p, this);
    controller = new Controller();

    controller.addListener(listener);

    lastDetectedFinger = new Finger();
    lastDetectedPointable = new Pointable();
    lastDetectedHand = new Hand();
  }

  public void stop() {
    controller.removeListener(listener);
  }

  public Frame getCurrentFrame() {
    if (currentFrame != null) {
      return currentFrame;
    } else {
      return new Frame();
    }
  }

  public LinkedList<Frame> getFrames() {
    return lastFrames;
  }

  public PVector getFingerPositionXYPlane() {
    PVector fingerPositionXYPlane = new PVector();

    Frame frame = getCurrentFrame();
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

  public float leapToScreenX(float x) {
    float c = p.width / 2.0f;
    if (x > 0.0) {
      return PApplet.lerp(c, p.width, x / LEAP_WIDTH);
    } else {
      return PApplet.lerp(c, 0.0f, -x / LEAP_WIDTH);
    }
  }

  public float leapToScreenY(float y) {
    return PApplet.lerp(p.height, 0.0f, y / LEAP_HEIGHT);
  }

  public float leapToScreenZ(float z) {
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
   * @return ArrayList<Hand> an arraylist containing all currently tracked hands
   */
  public ArrayList<Hand> getHandList() {
    ArrayList<Hand> hands = new ArrayList<Hand>();
    Frame frame = getCurrentFrame();
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
   * @return ArrayList<Finger> an arraylist containing all currently tracked fingers
   */
  public ArrayList<Finger> getFingerList() {
    ArrayList<Finger> fingers = new ArrayList<Finger>();

    Frame frame = getCurrentFrame();
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
      lastDetectedFinger = getFingerList().get(fingerNr);
    }
    return lastDetectedFinger;
  }

  /**
   * 
   * @return int the number of currently tracked fingers
   */
  public int getFingerCount() {
    return getFingerList().size();
  }

  /**
   * 
   * @return ArrayList<Pointable> an arraylist containing all currently tracked pointables
   */
  public ArrayList<Pointable> getPointableList() {
    ArrayList<Pointable> pointables = new ArrayList<Pointable>();

    Frame frame = getCurrentFrame();
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
   * @return int the number of currently tracked pointables
   */
  public int getPointableCount() {
    return getPointableList().size();
  }

  /**
   * 
   * @return
   */
  public ArrayList<Tool> getToolList() {
    ArrayList<Tool> tools = new ArrayList<Tool>();

    Frame frame = getCurrentFrame();
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
   * @return
   */
  public int getToolCount() {
    return getToolList().size();
  }
}
