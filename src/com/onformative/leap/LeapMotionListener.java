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

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Listener;

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

  private int maxFrameCountToCheckForGestures = 1000;

  /**
   * 
   * @param p PApplet the processing applet
   * @param leap LeapMotionP5 an instance of the LeapMotionP5 class
   */
  public LeapMotionListener(LeapMotionP5 leap) {
    this.leap = leap;
    leap.currentFrame = new Frame();
    leap.lastFrames = new LinkedList<Frame>();
    leap.lastFramesInclProperTimestamps = new ConcurrentSkipListMap<Date, Frame>();
    leap.oldFrames = new CopyOnWriteArrayList<Frame>();
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

  /**
   * this is called about 100-120 times a second delevering a new frame with information from the
   * leap tracking everything in its viewport. This is where most of the data which is accessible in
   * processing is coming from
   */
  public void onFrame(Controller controller) {
    Frame frame = controller.frame();
    leap.currentFrame = frame;

    // adding frames the list. making sure that only the newest frames are saved in order
    if (leap.lastFrames.size() >= maxFrameCountToCheckForGestures) {
      leap.lastFrames.removeFirst();
    }
    leap.lastFrames.add(frame);

    // adding frames to the list. adding a proper timestamp to each frame object
    if (leap.lastFramesInclProperTimestamps.size() >= maxFrameCountToCheckForGestures) {
      leap.lastFramesInclProperTimestamps.remove(leap.lastFramesInclProperTimestamps.firstKey());
    }
    leap.lastFramesInclProperTimestamps.put(new Date(), frame);

    // adding old frames to different object
    if (leap.oldFrames.size() >= maxFrameCountToCheckForGestures) {
      leap.oldFrames.remove(0);
    }
    leap.oldFrames.add(frame);
  }
}
