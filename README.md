LeapMotionP5
======================
#### A Leap Motion Library for Processing

Working with <b>all Processing versions</b> and <b>all operating systems (OSX, Windows)</b>, with the newest version of the leap motion sdk. Moreover it is including a variety of <b>gestures</b>.
Just download the library archive at the bottom of this readme and extract it into the libraries folder of your processing sketchbook and your're ready to go.

API
--------

#### List of all available methods:

LeapMotionP5(PApplet)

void start()<br />
void stop()<br />
void update()<br />
void addGesture(String)<br />

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
It's possible to use a vast variety of gestures with the leap motion in processing.

#### One finger gestures:
- swipe left
- swipe right
- swipe up
- swipe down
- push
- pull

#### Other:
- onHandEnter
- onHandLeave
- onFingerEnter
- onFingerLeave

#### One finger drawing gestures:
- circle
- triangle
- rectangle
- x
- check
- charet
- zig-zag
- arrow
- leftsquarebracket
- rightsquarebracket
- leftcurlybrace
- rightcurlybrace
- v
- delete
- star
- pigtail

<img src="http://depts.washington.edu/aimgroup/proj/dollar/unistrokes.gif"></img>

Download
--------
https://github.com/mrzl/LeapMotionP5/blob/master/LeapMotionP5.zip

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
import com.onformative.leap.LeapMotionP5;

LeapMotionP5 leap;
String lastGesture = "";

public void setup() {
  size(500, 500);
  textSize(30);

  leap = new LeapMotionP5(this);
  
  leap.addGesture(leap.SWIPE_LEFT);
  leap.addGesture(leap.SWIPE_RIGHT);
  leap.addGesture(leap.SWIPE_UP);
  leap.addGesture(leap.SWIPE_DOWN);
  //leap.addGesture(leap.ON_HAND_ENTER);
  //leap.addGesture(leap.ON_HAND_LEAVE);
  //leap.addGesture(leap.ON_FINGER_ENTER);
  //leap.addGesture(leap.ON_FINGER_LEAVE);
  //leap.addGesture(leap.PUSH);
  //leap.addGesture(leap.PULL);
  leap.addGesture(leap.CIRCLE);
  leap.addGesture(leap.TRIANGLE);
  leap.addGesture(leap.RECTANGLE);
  //leap.addGesture(leap.ZIG_ZAG);
  //leap.addGesture(leap.X);
  //leap.addGesture(leap.CHECK);
  //leap.addGesture(leap.CHARET);
  //leap.addGesture(leap.ARROW);
  //leap.addGesture(leap.LEFT_CURLY_BRACKET);
  //leap.addGesture(leap.RIGHT_CURLY_BRACKET);
  //leap.addGesture(leap.LEFT_SQUARE_BRACKET);
  //leap.addGesture(leap.RIGHT_SQUARE_BRACKET);
  //leap.addGesture(leap.V);
  //leap.addGesture(leap.DELETE);
  //leap.addGesture(leap.STAR);
  //leap.addGesture(leap.PIGTAIL);
  
  leap.start();
}

public void draw() {
  background(0);
  leap.gestures.one.draw();
  leap.update();
  text(lastGesture, 30, 30);
}

public void gestureRecognized(String gesture) {
  lastGesture = gesture;
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
