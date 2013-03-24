LeapMotionP5
======================
#### A Leap Motion Library for Processing

Working with <b>all Processing versions</b> and <b>all operating systems (OSX, Windows)</b>, with the newest version of the leap motion sdk. Moreover it is including a variety of <b>gestures</b>.
Just download the library archive at the bottom of this readme and extract it into the libraries folder of your processing sketchbook and your're ready to go.

API
--------

#### List of all available methods:

LeapMotionP5(PApplet)

void enableGesture(Type gesture)<br />
void disableGesture(Type gesture)<br />

Frame getFrame()<br />
Frame getLastFrame()<br />
LinkedList<Frame> getFrames()<br />
LinkedList<Frame> getFrames(int)<br />

ArrayList<Hand> getHandList()<br />
ArrayList<Hand> getHandList(Frame)<br />
Hand getHand(int)<br />
float getPitch(Hand)<br />
float getRoll(Hand)<br />
float getYaw(Hand)<br />
PVector getDirection(Hand)<br />
PVector getVelocity(Hand)<br />
PVector getPosition(Hand)<br />
PVector getNormal(Hand)<br />
PVector getAcceleration(Hand)<br />

ArrayList<Pointable> getPointableList()<br />
ArrayList<Pointable> getPointableList(Frame)<br />
ArrayList<Pointable> getPointableList(Hand)<br />
Pointable getPointable(int)<br />

ArrayList<Tool> getToolList()<br />
ArrayList<Tool> getToolList(Frame)<br />
ArrayList<Tool> getToolList(Hand)<br />
Tool getTool(int)<br />

ArrayList<Finger> getFingerList()<br />
ArrayList<Finger> getFingerList(Frame)<br />
ArrayList<Finger> getFingerList(Hand)<br />
Finger getFinger(int)<br />

PVector getTip(Pointable)<br />
PVector getTip(Tool)<br />
PVector getTip(Finger)<br />

PVector getOrigin(Pointable)<br />
PVector getOrigin(Tool)<br />
PVector getOrigin(Finger)<br />

PVector getVelocity(Pointable)<br />
PVector getVelocity(Tool)<br />
PVector getVelocity(Finger)<br />

PVector getAcceleration(Pointable)<br />
PVector getAcceleration(Tool)<br />
PVector getAcceleration(Finger)<br />

PVector getDirection(Pointable)<br />
PVector getDirection(Tool)<br />
PVector getDirection(Finger)<br />

float getLength(Pointable)<br />
float getLength(Tool)<br />
float getLength(Finger)<br />

float getWidth(Pointable)<br />
float getWidth(Tool)<br />
float getWidth(Finger)<br />

Gesture Recognition
-------------------

#### Circle
- radius
- normal
- clockwiseness
- nr of turns
- center
- duration

#### Swipe
- position
- direction
- duration
- speed

#### KeyTap
- position
- direction
- duration

#### ScreenTap
- position
- direction
- duration

Download
--------
Download from here https://github.com/mrzl/LeapMotionP5/archive/master.zip extract the folder in the archive into your processing libraries folder and rename it from LeapMotionP5-master to LeapMotionP5. After that restart Processing and the library should be included. Check the examples within the library.


Examples
--------
#### Basic Example
<pre>
import com.onformative.leap.LeapMotionP5;
import com.leapmotion.leap.Finger;

LeapMotionP5 leap;

public void setup() {
  size(500, 500);
  leap = new LeapMotionP5(this);
}

public void draw() {
  background(0);
  fill(255);
  for (Finger finger : leap.getFingerList()) {
    PVector fingerPos = leap.getTip(finger);
    ellipse(fingerPos.x, fingerPos.y, 10, 10);
  }
}

public void stop() {
  leap.stop();
}
</pre>

#### Gesture Recognition
<pre>
import com.leapmotion.leap.CircleGesture;
import com.leapmotion.leap.Gesture.State;
import com.leapmotion.leap.Gesture.Type;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.ScreenTapGesture;
import com.leapmotion.leap.SwipeGesture;
import com.onformative.leap.LeapMotionP5;

LeapMotionP5 leap;
String lastGesture =
"enabling gestures: \n'c' for CircleGesture\n's' for SwipeGesture\n'k' for KeyTapGesture\n't' for ScreenTapGesture";

public void setup() {
  size(500, 500);
  textSize(17);

  leap = new LeapMotionP5(this);
}

public void draw() {
  background(0);
  for (Hand hand : leap.getHandList()) {
    PVector handPos = leap.getPosition(hand);
    ellipse(handPos.x, handPos.y, 20, 20);
  }
  text(lastGesture, 30, 30);
}

public void circleGestureRecognized(CircleGesture gesture, String clockwiseness) {
  if (gesture.state() == State.STATE_STOP) {
    System.out.println("//////////////////////////////////////");
    System.out.println("Gesture type: " + gesture.type().toString());
    System.out.println("ID: " + gesture.id());
    System.out.println("Radius: " + gesture.radius());
    System.out.println("Normal: " + gesture.normal());
    System.out.println("Clockwiseness: " + clockwiseness);
    System.out.println("Turns: " + gesture.progress());
    System.out.println("Center: " + leap.convertVectorToPVector(gesture.center()));
    System.out.println("Duration: " + gesture.durationSeconds() + "s");
    System.out.println("//////////////////////////////////////");
    lastGesture = "Gesture type: " + gesture.type().toString() + "\n";
    lastGesture += "ID: " + gesture.id() + "\n";
    lastGesture += "Radius: " + gesture.radius() + "\n";
    lastGesture += "Normal: " + gesture.normal() + "\n";
    lastGesture += "Clockwiseness: " + clockwiseness + "\n";
    lastGesture += "Turns: " + gesture.progress() + "\n";
    lastGesture += "Center: " + leap.convertVectorToPVector(gesture.center()) + "\n";
    lastGesture += "Duration: " + gesture.durationSeconds() + "s" + "\n";
  } 
  else if (gesture.state() == State.STATE_START) {
  } 
  else if (gesture.state() == State.STATE_UPDATE) {
  }
}

public void swipeGestureRecognized(SwipeGesture gesture) {
  if (gesture.state() == State.STATE_STOP) {
    System.out.println("//////////////////////////////////////");
    System.out.println("Gesture type: " + gesture.type());
    System.out.println("ID: " + gesture.id());
    System.out.println("Position: " + leap.convertVectorToPVector(gesture.position()));
    System.out.println("Direction: " + gesture.direction());
    System.out.println("Duration: " + gesture.durationSeconds() + "s");
    System.out.println("Speed: " + gesture.speed());
    System.out.println("//////////////////////////////////////");
    lastGesture = "Gesture type: " + gesture.type().toString() + "\n";
    lastGesture += "ID: " + gesture.id() + "\n";
    lastGesture += "Position: " + leap.convertVectorToPVector(gesture.position()) + "\n";
    lastGesture += "Direction: " + gesture.direction() + "\n";
    lastGesture += "Speed: " + gesture.speed() + "\n";
    lastGesture += "Duration: " + gesture.durationSeconds() + "s" + "\n";
  } 
  else if (gesture.state() == State.STATE_START) {
  } 
  else if (gesture.state() == State.STATE_UPDATE) {
  }
}

public void screenTapGestureRecognized(ScreenTapGesture gesture) {
  if (gesture.state() == State.STATE_STOP) {
    System.out.println("//////////////////////////////////////");
    System.out.println("Gesture type: " + gesture.type());
    System.out.println("ID: " + gesture.id());
    System.out.println("Position: " + leap.convertVectorToPVector(gesture.position()));
    System.out.println("Direction: " + gesture.direction());
    System.out.println("Duration: " + gesture.durationSeconds() + "s");
    System.out.println("//////////////////////////////////////");
    lastGesture = "Gesture type: " + gesture.type().toString() + "\n";
    lastGesture += "ID: " + gesture.id() + "\n";
    lastGesture += "Position: " + leap.convertVectorToPVector(gesture.position()) + "\n";
    lastGesture += "Direction: " + gesture.direction() + "\n";
    lastGesture += "Duration: " + gesture.durationSeconds() + "s" + "\n";
  } 
  else if (gesture.state() == State.STATE_START) {
  } 
  else if (gesture.state() == State.STATE_UPDATE) {
  }
}

public void KeyTapGestureRecognized(KeyTapGesture gesture) {
  if (gesture.state() == State.STATE_STOP) {
    System.out.println("//////////////////////////////////////");
    System.out.println("Gesture type: " + gesture.type());
    System.out.println("ID: " + gesture.id());
    System.out.println("Position: " + leap.convertVectorToPVector(gesture.position()));
    System.out.println("Direction: " + gesture.direction());
    System.out.println("Duration: " + gesture.durationSeconds() + "s");
    System.out.println("//////////////////////////////////////");
    lastGesture = "Gesture type: " + gesture.type().toString() + "\n";
    lastGesture += "ID: " + gesture.id() + "\n";
    lastGesture += "Position: " + leap.convertVectorToPVector(gesture.position()) + "\n";
    lastGesture += "Direction: " + gesture.direction() + "\n";
    lastGesture += "Duration: " + gesture.durationSeconds() + "s" + "\n";
  } 
  else if (gesture.state() == State.STATE_START) {
  } 
  else if (gesture.state() == State.STATE_UPDATE) {
  }
}

public void keyPressed() {
  if (key == 'c') {
    if (leap.isEnabled(Type.TYPE_CIRCLE)) {
      leap.disableGesture(Type.TYPE_CIRCLE);
      lastGesture = "Circle Gesture disabled.";
    } 
    else {
      leap.enableGesture(Type.TYPE_CIRCLE);
      lastGesture = "Circle Gesture enabled.";
    }
  }

  if (key == 's') {
    if (leap.isEnabled(Type.TYPE_SWIPE)) {
      leap.disableGesture(Type.TYPE_SWIPE);
      lastGesture = "Swipe Gesture disabled.";
    } 
    else {
      leap.enableGesture(Type.TYPE_SWIPE);
      lastGesture = "Swipe Gesture enabled.";
    }
  }

  if (key == 'k') {
    if (leap.isEnabled(Type.TYPE_KEY_TAP)) {
      leap.disableGesture(Type.TYPE_KEY_TAP);
      lastGesture = "KeyTap Gesture disabled.";
    } 
    else {
      leap.enableGesture(Type.TYPE_KEY_TAP);
      lastGesture = "KeyTap Gesture enabled.";
    }
  }

  if (key == 't') {
    if (leap.isEnabled(Type.TYPE_SCREEN_TAP)) {
      leap.disableGesture(Type.TYPE_SCREEN_TAP);
      lastGesture = "ScreenTap Gesture disabled. ";
    } 
    else {
      leap.enableGesture(Type.TYPE_SCREEN_TAP);
      lastGesture = "ScreenTap Gesture enabled. ";
    }
  }
}
public void stop() {
  leap.stop();
}


</pre>

License
-------
<pre>
LeapMotionP5 library for Processing.
Copyright (c) 2012-2013 held jointly by the individual authors.

LeapMotionP5 library for Processing is free software: you can redistribute it and/or
modify it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

LeapMotionP5 library for Processing is distributed in the hope that it will be
useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with LeapMotionP5 library for Processing.  If not, see http://www.gnu.org/licenses/.
</pre>

<pre>
Leap Developer SDK.
Copyright (C) 2012-2013 Leap Motion, Inc. All rights reserved.

NOTICE: This developer release of Leap Motion, Inc. software is confidential
and intended for very limited distribution. Parties using this software must
accept the SDK Agreement prior to obtaining this software and related tools.
This software is subject to copyright.
</pre>

<pre>
OneDollar Unistroke Recognizer
Copyright 2012, Darius Morawiec

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
</pre>
