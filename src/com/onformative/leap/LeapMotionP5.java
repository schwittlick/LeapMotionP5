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

  protected ConcurrentMap<Integer, Vector> fingerPositions;
  protected ConcurrentMap<Integer, Vector> fingerVelocity;
  protected ConcurrentMap<Integer, Vector> toolPositions;
  protected ConcurrentMap<Integer, Double> handPitch;
  protected ConcurrentMap<Integer, Double> handRoll;
  protected ConcurrentMap<Integer, Double> handYaw;
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
  }

  public void stop() {
    controller.removeListener(listener);
  }

  public ConcurrentMap<Integer, Vector> getFingerPositions() {
    return fingerPositions;
  }

  public ConcurrentMap<Integer, Vector> getFingerVelocity() {
    return fingerPositions;
  }

  public ConcurrentMap<Integer, Vector> getToolPositions() {
    return toolPositions;
  }

  public ConcurrentMap<Integer, Double> getHandPitch() {
    return handPitch;
  }

  public ConcurrentMap<Integer, Double> getHandRoll() {
    return handRoll;
  }

  public ConcurrentMap<Integer, Double> getHandYaw() {
    return handRoll;
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
    return PApplet.lerp(100, 0, z / LEAP_DEPTH);
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
  public PVector convertFingerToPVector(Pointable pointable) {
    return convertToScreenDimension(pointable.tipPosition().getX(), pointable.tipPosition().getY(),
        pointable.tipPosition().getZ());
  }

  /**
   * 
   * @param hand
   * @return
   */
  public PVector convertFingerToPVector(Hand hand) {
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
   * accessing a finger by number. the number indicates the fingernumber. for example: when there
   * are two fingers detected by the leap you can access the first one by getFinger(0) and the
   * second one by getFinger(1). this is a comfortable way to quickly access the leap in processing
   * 
   * @param fingerNr
   * @return
   */
  public Finger getFinger(int fingerNr) {
    if (!getActiveFingers().isEmpty()) {
      lastDetectedFinger = getActiveFingers().get(fingerNr);
    }
    return lastDetectedFinger;
  }

  /**
   * 
   * @param pointableNr
   * @return
   */
  public Pointable getPointable(int pointableNr) {
    if (!getActivePointables().isEmpty()) {
      lastDetectedPointable = getActivePointables().get(pointableNr);
    }
    return lastDetectedPointable;
  }

  /**
   * 
   * @param handNr
   * @return
   */
  public Hand getHand(int handNr) {
    if (!getActiveHands().isEmpty()) {
      lastDetectedHand = getActiveHands().get(handNr);
    }
    return lastDetectedHand;
  }

  /**
   * 
   * @return ArrayList<Hand> an arraylist containing all currently tracked hands
   */
  public ArrayList<Hand> getActiveHands() {
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
    return getActiveHands().size();
  }

  /**
   * 
   * @return ArrayList<Finger> an arraylist containing all currently tracked fingers
   */
  public ArrayList<Finger> getActiveFingers() {
    ArrayList<Finger> fingers = new ArrayList<Finger>();

    Frame frame = getCurrentFrame();
    if (frame.hands().empty() == false) {
      for (Hand hand : frame.hands()) {
        if (hand.fingers().empty() == false) {
          for (Finger fin : hand.fingers()) {
            fingers.add(fin);
          }
        }
      }
    }

    return fingers;
  }

  /**
   * 
   * @return int the number of currently tracked fingers
   */
  public int getFingerCount() {
    return getActiveFingers().size();
  }

  /**
   * 
   * @return ArrayList<Pointable> an arraylist containing all currently tracked pointables
   */
  public ArrayList<Pointable> getActivePointables() {
    ArrayList<Pointable> pointables = new ArrayList<Pointable>();

    Frame frame = getCurrentFrame();
    if (frame.hands().empty() == false) {
      for (Hand hand : frame.hands()) {
        if (hand.pointables().empty() == false) {
          for (Pointable poi : hand.pointables()) {
            pointables.add(poi);
          }
        }
      }
    }

    return pointables;
  }

  /**
   * 
   * @return int the number of currently tracked pointables
   */
  public int getPointableCount() {
    return getActivePointables().size();
  }
}
