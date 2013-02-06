package com.onformative.leap.gestures;

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
import java.util.NoSuchElementException;

import com.leapmotion.leap.FingerList;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.onformative.leap.LeapMotionP5;

/**
 * 
 * @author Marcel Schwittlick
 * 
 */
public class VictoryGesture extends Gesture {
  private int lastFingerCount;

  public VictoryGesture(LeapMotionP5 leap) {
    super(leap);

    lastFingerCount = 0;
  }

  public boolean check() {
    ArrayList<Frame> frames = new ArrayList<Frame>();
    try {
      frames.add(leap.getFrames().getLast());
      frames.add(leap.getFrames().get(leap.getFrames().size() - 2));
    } catch (NoSuchElementException e) {
      return false;
    }
    for (Frame frame : frames) {
      if (!frame.hands().empty()) {
        Hand hand = frame.hands().get(0);
        FingerList fingers = hand.fingers();
        if (!fingers.empty()) {
          if (lastFingerCount == 1 && fingers.count() == 2) {
            lastFingerCount = fingers.count();
            return true;
          }
          lastFingerCount = fingers.count();
        }
      }
    }
    return false;
  }

  public boolean closed() {
    Frame frame1 = leap.getFrames().getLast();
    Frame frame2 = leap.getFrames().get(leap.getFrames().size() - 2);

    // System.out.println(frame1.timestamp());
    // System.out.println(frame2.timestamp());


    if (!frame1.hands().empty() && !frame2.hands().empty()) {
      Hand hand1 = frame1.hands().get(0);
      Hand hand2 = frame2.hands().get(0);
      FingerList fingers1 = hand1.fingers();
      FingerList fingers2 = hand2.fingers();
      if (!fingers1.empty() && !fingers2.empty()) {
        // System.out.println("fingers1 count: " + fingers1.count());
        // System.out.println("fingers2 count: " + fingers2.count());
        if (fingers1.count() == 2 && fingers2.count() == 1) {
          // System.out.println("hey");
          return true;
        }
      }
    }

    return false;
  }
}
