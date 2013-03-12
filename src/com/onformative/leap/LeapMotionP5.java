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
import java.util.concurrent.CopyOnWriteArrayList;

import processing.core.PApplet;
import processing.core.PVector;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture.Type;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Pointable;
import com.leapmotion.leap.ScreenList;
import com.leapmotion.leap.Tool;
import com.leapmotion.leap.Vector;

/**
 * LeapMotionP5.java
 * 
 * @author Marcel Schwittlick
 * @modified 19.02.2013
 * 
 */
public class LeapMotionP5 {
  private PApplet p;
  private LeapMotionListener listener;
  private Controller controller;

  protected LinkedList<Frame> lastFrames;
  protected CopyOnWriteArrayList<Frame> oldFrames;
  protected ConcurrentSkipListMap<Date, Frame> lastFramesInclProperTimestamps;

  protected Frame currentFrame;

  protected HashMap<Integer, Finger> lastDetectedFinger;
  protected HashMap<Integer, Pointable> lastDetectedPointable;
  protected HashMap<Integer, Hand> lastDetectedHand;
  protected HashMap<Integer, Tool> lastDetectedTool;

  private float LEAP_WIDTH = 200.0f; // in mm
  private float LEAP_HEIGHT = 500.0f; // in mm
  private float LEAP_DEPTH = 200.0f; // in mm

  private int activeScreenNr = 0;

  private Finger testFinger;

  /**
   * this class gives you some high level access to the data tracked and recorded by the leap. it
   * gives you a different way of access than the original leap sdk and transoforms all data into
   * processing equivalent datatypes.
   * 
   * @param p PApplet the processing applet
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

    // this is neccessary because the velocity of all objects has an offset.
    testFinger = new Finger();
  }

  public String getSDKVersion() {
    return "0.7.5";
  }

  /**
   * this prints out the current offset of the vectors from the sdk. this is just for information
   * and will give you the position, velocity and acceleration offsets
   */
  public void printCorrectionOffset() {
    System.out.println("pos offset: " + getTip(testFinger));
    System.out.println("velo offset: " + getVelocity(testFinger));
    System.out.println("acc offset: " + getAcceleration(testFinger));
  }

  /**
   * 
   */
  public void stop() {
    controller.removeListener(listener);
  }

  /**
   * this returns a pvector containing the velocity offset. the problems the velocity offset has
   * with it, is that the velocity is slightly shiftet. for example if you shouldnt have any
   * velocity, because the finger is stanging still its returning a velocity that is initialized
   * with a new Finger object. this should be fixed with the upcoming sdk releases (i hope) *
   * 
   * @return PVector containing the velocity offset
   */
  public PVector velocityOffset() {
    return convertVectorToPVector(testFinger.tipVelocity());
  }

  /**
   * this retuns an pvector containing the acceleration offset, that has to be substracted from
   * every vector returned from the library. unfortunately the leap sdk has a little bug there.
   * 
   * @return PVector containing the acceleration offset
   */
  public PVector accelerationOffset() {
    return getAcceleration(testFinger);
  }

  public void enableGesture(Type gestureName) {
    controller.enableGesture(gestureName);
  }

  public void disableGesture(Type gesture) {
    controller.enableGesture(gesture, false);
  }

  public boolean isEnabled(Type gesture) {
    return controller.isGestureEnabled(gesture);
  }

  /**
   * this returns the parent applet of the library - the processing applet.
   * 
   * @return PApplet parent
   */
  public PApplet getParent() {
    return p;
  }

  /**
   * returns the controller of the leap sdk
   * 
   * @return Controller controller
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
   * returns the most current frame from the leap sdk. a frame contains every tracked data from the
   * leap about your fingers.
   * 
   * @return Frame the leap frame
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
   * returns a frame by id.
   * 
   * @param id the id of the frame you want
   * @return Frame the frame which id you passed as a parameter. if the frame with the id you asked
   *         for is not currently saved anymore you'll get a new Frame object.
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
   * returns the frame before the most current frame.
   * 
   * @return
   */
  public Frame getLastFrame() {
    return getFrames().get(getFrames().size() - 2);
  }

  /**
   * returns the frame that was before the frame you passed.
   * 
   * @param frame
   * @return the frame that was recorded right before the frame you passed.
   */
  public Frame getFrameBeforeFrame(Frame frame) {
    Frame frameBefore = null;

    for (Frame of : getFrames()) {
      if (of.id() == frame.id() - 1) {
        frameBefore = of;
      }
    }


    return frameBefore;
  }

  /**
   * returns a CopyOnWriteArrayList<Frame> containing all recently buffered frames.
   * 
   * @return a CopyOnWriteArrayList containing the newest elements
   */
  public CopyOnWriteArrayList<Frame> getFrames() {
    try {
      return oldFrames;
    } catch (Exception e) {
      System.err.println("Can not return list of last frames. Returning empty list instead.");
      System.err.println(e);
      return new CopyOnWriteArrayList<Frame>();
    }
  }

  /**
   * returns a linkedlist containing the last buffered frame
   * 
   * @param frameCount the number of last frames
   * @return a list containing all last frames
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
        fingerPositionXYPlane.x = transformLeapToScreenX(finger.tipPosition().getX());
        fingerPositionXYPlane.y = transformLeapToScreenY(finger.tipPosition().getY());
      }
    }

    return fingerPositionXYPlane;
  }

  /**
   * converts the x coordinate from the leap space into the processing window space
   * 
   * @param x leap-space
   * @return processing-window space
   */
  public float transformLeapToScreenX(float x) {
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
   * converts the y coordinate from the leap space into the processing window space
   * 
   * @param y leap space
   * @return processing-window space
   */
  public float transformLeapToScreenY(float y) {
    /*
     * int startY = 50; int endY = 350; float valueMapped = PApplet.map(y, startY, endY, 0,
     * p.height); return valueMapped;
     */
    return PApplet.lerp(p.height, 0.0f, y / LEAP_HEIGHT);
  }

  /**
   * converts the z coordinate from the leap space into the processing window space
   * 
   * @param z leap space
   * @return processing window space
   */
  public float transformLeapToScreenZ(float z) {
    /*
     * int startZ = -51; int endZ = 149; float valueMapped = PApplet.map(z, startZ, endZ, 0,
     * p.width); return valueMapped;
     */
    return PApplet.lerp(0, p.width, z / LEAP_DEPTH);
  }

  /**
   * converts a vector from the leap space into the processing window space
   * 
   * @param vector from the leap sdk containing a position in the leap space
   * @return the vector in PVector data type containing the same position in processing window space
   */
  public PVector convertVectorToPVector(Vector vector) {
    return convertLeapToScreenDimension(vector.getX(), vector.getY(), vector.getZ());
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
  public PVector convertLeapToScreenDimension(float x, float y, float z) {
    PVector positionRelativeToFrame = new PVector();
    positionRelativeToFrame.x = transformLeapToScreenX(x);
    positionRelativeToFrame.y = transformLeapToScreenY(y);
    positionRelativeToFrame.z = transformLeapToScreenZ(z);
    return positionRelativeToFrame;
  }

  /**
   * returns an arraylist containing all currently tracked hands
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
   * returns all hands tracked in the frame you passed
   * 
   * @param frame the frame from which to find out all tracked hands
   * @return arraylist containing all hands from the passed frame
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
   * gets a hand by number. the number is indicated by the order the hand appeared in the leap. so
   * the first hand tracked has the nr 0 and the second one the number 1. once one hand of them two
   * leaves the leap the one hand left has the nr 0. this is implemented like that because the leap
   * is loosing track of the id's of hand to easily.
   * 
   * @param handNr nr of the hand
   * @return
   */
  public Hand getHand(int handNr) {
    Hand returnHand = null;
    if (!getHandList().isEmpty()) {
      lastDetectedHand.put(handNr, getHandList().get(handNr));
    }
    // returnHand = lastDetectedHand.get(handNr);
    int downCounter = 0;
    while (returnHand == null) {
      returnHand = lastDetectedHand.get(handNr - downCounter);
      downCounter++;
    }
    return returnHand;
  }

  /**
   * returns a hand by id in the frame you passed.
   * 
   * @param id
   * @param frame
   * @return
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
   * returns the pitch of the hand you passed
   * 
   * @param hand the hand you want the pitch of
   * @return a float value containing the pitch of the hand
   */
  public float getPitch(Hand hand) {
    // return PApplet.map((float) Math.toDegrees(hand.direction().pitch()), 0, 22, 0,
    // PConstants.TWO_PI);
    return (float) Math.toDegrees(hand.direction().pitch());
  }

  /**
   * returns the roll of the hand you passed
   * 
   * @param hand the hand you want the roll of
   * @return a float value containing the roll of the hand
   */
  public float getRoll(Hand hand) {
    // return -PApplet.map((float) Math.toDegrees(hand.direction().roll()), 0, 180, 0,
    // PConstants.TWO_PI);
    return (float) Math.toDegrees(hand.palmNormal().roll());
  }

  /**
   * returns the yaw of the hand you passed
   * 
   * @param hand the hand you want the yaw of
   * @return a float value containing the yaw of the hand
   */
  public float getYaw(Hand hand) {
    return (float) Math.toDegrees(hand.direction().yaw());
  }

  /**
   * returns a PVector containing the direction of the hand
   * 
   * @param hand the hand you want the direction of
   * @return PVector direction of the hand
   */
  public PVector getDirection(Hand hand) {
    return convertVectorToPVector(hand.direction());
  }

  /**
   * returns a PVector containing the position of the hand
   * 
   * @param hand the hand you want the position of
   * @return PVector position of the hand
   */
  public PVector getPosition(Hand hand) {
    return convertVectorToPVector(hand.palmPosition());
  }

  /**
   * retrusn the normal of the palm of the hand
   * 
   * @param hand the hand you want the normal of the handpalm of
   * @return a PVector containing the normal of thepalm of the hand
   */
  public PVector getNormal(Hand hand) {
    return convertVectorToPVector(hand.palmNormal());
  }

  /**
   * returns the velocity of the palm of the hand you passed in
   * 
   * @param hand the hand of which palm you want the velocity of
   * @return a PVector containing the velocity of the hand
   */
  public PVector getVelocity(Hand hand) {
    PVector velo = convertVectorToPVector(hand.palmVelocity());
    velo.sub(velocityOffset());
    return velo;
  }

  /**
   * access to the acceleration of the hand you passed in.
   * 
   * @param hand the hand you want the acceleration of
   * @return a PVector containing the acceleration of the hand you passed in
   */
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
   * access to all fingers that are currently tracked
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
   * access to all tracked fingers in the frame you passed in
   * 
   * @param frame the frame you want all tracked fingers of
   * @return an arraylist containing all tracked fingers
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
   * access to all fingers of the hand you passed in
   * 
   * @param hand the hand you want all tracked fingers of
   * @return an arraylist containing all tracked fingers of the hand
   */
  public ArrayList<Finger> getFingerList(Hand hand) {
    ArrayList<Finger> fingers = new ArrayList<Finger>();

    for (Finger finger : hand.fingers()) {
      fingers.add(finger);
    }
    return fingers;
  }

  /**
   * returns the finger by number. the fingers are numbered by the occurence in the leap.
   * 
   * @param fingerNr
   * @return
   */
  public Finger getFinger(int fingerNr) {
    Finger returnFinger = null;
    if (getFingerList().size() > fingerNr) {
      lastDetectedFinger.put(fingerNr, getFingerList().get(fingerNr));
    }
    // returnFinger = lastDetectedFinger.get(fingerNr);
    int downCounter = 0;
    while (returnFinger == null) {
      returnFinger = lastDetectedFinger.get(fingerNr - downCounter);
      downCounter++;
    }
    return returnFinger;
  }

  /**
   * 
   * @param id
   * @param frame
   * @return
   */
  public Finger getFingerById(int id, Frame frame) {
    Finger returnFinger = null;
    for (Finger finger : getFingerList(frame)) {
      if (finger.id() == id) {
        returnFinger = finger;
      }
    }
    return returnFinger;
  }

  /**
   * returns the tip position of the passed pointable
   * 
   * @param pointable the pointable you want the tippoisition of
   * @return a PVector containing the position of the tip of the pointable
   */
  public PVector getTip(Pointable pointable) {
    return convertLeapToScreenDimension(pointable.tipPosition().getX(), pointable.tipPosition()
        .getY(), pointable.tipPosition().getZ());
  }

  /**
   * sets the current screen for gettings the calibrated points. I should rewrite this, but nobody
   * is gonna read it anyway. arr.
   * 
   * @param screenNr
   */
  public void setActiveScreen(int screenNr) {
    this.activeScreenNr = screenNr;
  }

  /**
   * to use this utility you have to have the leap calirated to your screen
   * 
   * @param pointable the finger you want the intersection with your screen from
   * @param screenNr the number of the screen you calibrated
   * @return
   */
  public PVector getTipOnScreen(Pointable pointable) {
    PVector pos;

    ScreenList sl = controller.calibratedScreens();
    com.leapmotion.leap.Screen calibratedScreen = sl.get(activeScreenNr);
    Vector loc = calibratedScreen.intersect(pointable, true);

    float _x = PApplet.map(loc.getX(), 0, 1, 0, p.displayWidth);
    _x -= p.getLocationOnScreen().x;
    float _y = PApplet.map(loc.getY(), 0, 1, p.displayHeight, 0);
    _y -= p.getLocationOnScreen().y;

    pos = new PVector(_x, _y);
    return pos;
  }

  private PVector lastPositionOnScreen = new PVector();

  public PVector getVelocityOnScreen(Pointable pointable) {
    PVector velocityOnScreen;

    ScreenList sl = controller.calibratedScreens();
    com.leapmotion.leap.Screen calibratedScreen = sl.get(activeScreenNr);
    Vector loc = calibratedScreen.intersect(pointable, true);

    float _x = PApplet.map(loc.getX(), 0, 1, 0, p.displayWidth);
    _x -= p.getLocationOnScreen().x;
    float _y = PApplet.map(loc.getY(), 0, 1, p.displayHeight, 0);
    _y -= p.getLocationOnScreen().y;
    
    velocityOnScreen = new PVector(_x, _y);
    velocityOnScreen.sub(lastPositionOnScreen);

    lastPositionOnScreen = new PVector(_x, _y);


    return velocityOnScreen;
  }

  /**
   * returns the origin of the pointable. the origin is the place where the pointable leaves the
   * body of the hand.
   * 
   * @param pointable the pointable you want the origin of
   * @return a PVector containing the position of the origin of the passed pointable
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
   * returns the velocity of the pointbale
   * 
   * @param pointable the pointable you want the velocity of
   * @return a PVector containing the velocity of the tip of the pointble
   */
  public PVector getVelocity(Pointable pointable) {
    PVector velo = convertVectorToPVector(pointable.tipVelocity());
    velo.sub(velocityOffset());
    return velo;
  }



  /**
   * calculates the direction of the passed pointable
   * 
   * @param pointable the pointable you want the direction of
   * @return a PVector containing the direction of the pointable
   */
  public PVector getDirection(Pointable pointable) {
    return convertVectorToPVector(pointable.direction());
  }

  /**
   * passes the length of a pointable though.
   * 
   * @param pointable
   * @return
   */
  public float getLength(Pointable pointable) {
    return pointable.length();
  }

  /**
   * passes the width of a pointable through
   * 
   * @param pointable
   * @return
   */
  public float getWidth(Pointable pointable) {
    return pointable.width();
  }

  /**
   * calculates the acceleration of the pointable according to the velocity of the curent and the
   * last frame
   * 
   * @param pointable the pointable you want the acceleration of
   * @return a PVector containing the acceleration of the tip of the passed pointable
   */
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
   * returns all pointables in the current frame
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
   * returns all pointables of the passed framre
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
   * returns all pointables of the passed hand
   * 
   * @param hand the hand you want the pointables of
   * @return an arraylist containing the pointables of the passed hand
   */
  public ArrayList<Pointable> getPointableList(Hand hand) {
    ArrayList<Pointable> pointables = new ArrayList<Pointable>();

    for (Pointable pointable : hand.pointables()) {
      pointables.add(pointable);
    }

    return pointables;
  }

  /**
   * returns a pointable by its number. look up to the equivalent methods for hand/finger for
   * documentation
   * 
   * @param pointableNr the number of the pointable
   * @return
   */
  public Pointable getPointable(int pointableNr) {
    Pointable returnPointable = null;
    if (!getPointableList().isEmpty()) {
      lastDetectedPointable.put(pointableNr, getPointableList().get(pointableNr));
    }
    // returnPointable = lastDetectedPointable.get(pointableNr);
    int downCounter = 0;
    while (returnPointable == null) {
      returnPointable = lastDetectedPointable.get(pointableNr - downCounter);
      downCounter++;
    }
    return returnPointable;
  }

  /**
   * returns a pointable by id in the passed frame
   * 
   * @param id the if of the pointbale
   * @param frame the frame where to look for the pointable
   * @return
   */
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
   * calculates an arraylist containing all tools in the current frame
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
   * calculates an arraylist containing all tools in the passed frame
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
   * returns a arraylist of tools attached to the passed hand
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
   * returns a tool by its number
   * 
   * @param toolNr
   * @return
   */
  public Tool getTool(int toolNr) {
    Tool returnTool = null;
    if (!getToolList().isEmpty()) {
      lastDetectedTool.put(toolNr, getToolList().get(toolNr));
    }
    // returnTool = lastDetectedTool.get(toolNr);
    int downCounter = 0;
    while (returnTool == null) {
      returnTool = lastDetectedTool.get(toolNr - downCounter);
      downCounter++;
    }
    return returnTool;
  }

  /**
   * calculates a proper timestamp of the passed frame
   * 
   * @param frame the frame you want the timestamp of
   * @return Date containing the timestamp when the frame was taken
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
