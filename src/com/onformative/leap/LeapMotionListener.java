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

import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.CopyOnWriteArrayList;

import processing.core.PApplet;

import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Gesture.State;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.ScreenTapGesture;
import com.leapmotion.leap.SwipeGesture;

/**
 * LeapMotionListener.java
 * 
 * @author Marcel Schwittlick
 * @modified 04.02.2013
 * 
 *           this listener is listening for the events of the offical leap motion sdk
 */
class LeapMotionListener extends Listener {
  private LeapMotionP5 leap;

  protected int maxFramesToRecord = 1000;
  private String callbackMethodNameCircle;
  private String callbackMethodNameSwipe;
  private String callbackMethodNameScreenTap;
  private String callbackMethodNameKeyTap;

  /**
   * 
   * @param p PApplet the processing applet
   * @param leap LeapMotionP5 an instance of the LeapMotionP5 class
   */
  public LeapMotionListener(LeapMotionP5 leap) {
    this.leap = leap;
    this.leap.currentFrame = new Frame();
    this.leap.lastFrames = new LinkedList<Frame>();
    this.leap.lastFramesInclProperTimestamps = new ConcurrentSkipListMap<Date, Frame>();
    this.leap.oldFrames = new CopyOnWriteArrayList<Frame>();
    this.leap.oldControllers = new LinkedList<Controller>();

    this.callbackMethodNameCircle = "circleGestureRecognized";
    this.callbackMethodNameSwipe = "swipeGestureRecognized";
    this.callbackMethodNameScreenTap = "screenTapGestureRecognized";
    this.callbackMethodNameKeyTap = "keyTapGestureRecognized";
  }

  public void onInit(Controller controller) {
    System.out.println("Leap Motion Initialized");
  }

  public void onConnect(Controller controller) {
    System.out.println("Leap Motion Connected");
  }

  public void onDisconnect(Controller controller) {
    System.out.println("Leap Motion Disconnected");
  }

  public void onExit(Controller controller) {
    System.out.println("Leap Motion Exited");
  }

  private void invokeCallback(Gesture gesture) {
    PApplet parent = this.leap.getParent();

    if (parent != null) {
      switch (gesture.type()) {
        case TYPE_CIRCLE:
          CircleGesture circleGesture = new CircleGesture(gesture);
          String clockwiseness;
          if (circleGesture.pointable().direction().angleTo(circleGesture.normal()) <= Math.PI / 4) {
            // Clockwise if angle is less than 90 degrees
            clockwiseness = "clockwise";
          } else {
            clockwiseness = "counterclockwise";
          }
          try {
            parent.getClass()
                .getMethod(this.callbackMethodNameCircle, CircleGesture.class, String.class)
                .invoke(parent, circleGesture, clockwiseness);
          } catch (Exception e) {
            PApplet.println(e.getMessage() + " CALLBACK ERROR");
          }
          break;
        case TYPE_SWIPE:
          SwipeGesture swipeGesture = new SwipeGesture(gesture);
          try {
            parent.getClass().getMethod(this.callbackMethodNameSwipe, SwipeGesture.class)
                .invoke(parent, swipeGesture);
          } catch (Exception e) {
            PApplet.println(e.getMessage() + " CALLBACK ERROR");
          }
          break;
        case TYPE_SCREEN_TAP:
          ScreenTapGesture screenTapGesture = new ScreenTapGesture(gesture);
          try {
            parent.getClass().getMethod(this.callbackMethodNameScreenTap, ScreenTapGesture.class)
                .invoke(parent, screenTapGesture);
          } catch (Exception e) {
            PApplet.println(e.getMessage() + " CALLBACK ERROR");
          }
          break;
        case TYPE_KEY_TAP:
          KeyTapGesture keyTapGesture = new KeyTapGesture(gesture);
          try {
            parent.getClass().getMethod(this.callbackMethodNameKeyTap, KeyTapGesture.class)
                .invoke(parent, keyTapGesture);
          } catch (Exception e) {
            PApplet.println(e.getMessage() + " CALLBACK ERROR");
          }
          break;
        default:
          break;
      }
    }


  }

  private void printGestureDetails(Gesture gesture, Controller controller) {
    switch (gesture.type()) {
      case TYPE_CIRCLE:
        CircleGesture circle = new CircleGesture(gesture);

        // Calculate clock direction using the angle between circle normal and pointable
        String clockwiseness;
        if (circle.pointable().direction().angleTo(circle.normal()) <= Math.PI / 4) {
          // Clockwise if angle is less than 90 degrees
          clockwiseness = "clockwise";
        } else {
          clockwiseness = "counterclockwise";
        }

        // Calculate angle swept since last frame
        double sweptAngle = 0;
        if (circle.state() != State.STATE_START) {
          CircleGesture previousUpdate =
              new CircleGesture(controller.frame(1).gesture(circle.id()));
          sweptAngle = (circle.progress() - previousUpdate.progress()) * 2 * Math.PI;
        }

        System.out.println("Circle id: " + circle.id() + ", " + circle.state() + ", progress: "
            + circle.progress() + ", radius: " + circle.radius() + ", angle: "
            + Math.toDegrees(sweptAngle) + ", " + clockwiseness);
        break;
      case TYPE_SWIPE:
        SwipeGesture swipe = new SwipeGesture(gesture);
        System.out.println("Swipe id: " + swipe.id() + ", " + swipe.state() + ", position: "
            + swipe.position() + ", direction: " + swipe.direction() + ", speed: " + swipe.speed());
        break;
      case TYPE_SCREEN_TAP:
        ScreenTapGesture screenTap = new ScreenTapGesture(gesture);
        System.out.println("Screen Tap id: " + screenTap.id() + ", " + screenTap.state()
            + ", position: " + screenTap.position() + ", direction: " + screenTap.direction());
        break;
      case TYPE_KEY_TAP:
        KeyTapGesture keyTap = new KeyTapGesture(gesture);
        System.out.println("Key Tap id: " + keyTap.id() + ", " + keyTap.state() + ", position: "
            + keyTap.position() + ", direction: " + keyTap.direction());
        break;
      default:
        System.out.println("Unknown gesture type.");
        break;
    }

  }

  private void processGestures(Controller controller) {
    GestureList list = controller.frame().gestures();
    if (list.empty() == false) {
      for (int i = 0; i < list.count(); i++) {
        Gesture gesture = list.get(i);
        invokeCallback(gesture);
        // printGestureDetails(gesture, controller);
      }
    }
  }

  /**
   * this is called about 100-120 times a second delevering a new frame with information from the
   * leap tracking everything in its viewport. This is where most of the data which is accessible in
   * processing is coming from
   */
  public void onFrame(Controller controller) {
    Frame frame = controller.frame();
    this.leap.currentFrame = frame;

    processGestures(controller);

    // adding frames the list. making sure that only the newest frames are saved in order
    if (leap.lastFrames.size() >= maxFramesToRecord) {
      leap.lastFrames.removeFirst();
    }
    leap.lastFrames.add(frame);

    // adding frames to the list. adding a proper timestamp to each frame object
    if (leap.lastFramesInclProperTimestamps.size() >= maxFramesToRecord) {
      leap.lastFramesInclProperTimestamps.remove(leap.lastFramesInclProperTimestamps.firstKey());
    }
    leap.lastFramesInclProperTimestamps.put(new Date(), frame);

    // adding old frames to different object
    if (leap.oldFrames.size() >= maxFramesToRecord) {
      leap.oldFrames.remove(0);
    }
    leap.oldFrames.add(frame);

    if (leap.oldControllers.size() >= maxFramesToRecord) {
      leap.oldControllers.removeLast();
    }
    leap.oldControllers.add(controller);
  }
}
