LeapMotionP5
======================
<b>A Leap Motion Library for Processing</b> 

Working with <b>all Processing versions</b> and <b>all operating systems (OSX, Windows)</b>, with the newest version of the leap motion sdk. Moreover it is including a variety of <b>gestures</b>.
Just download the library archive at the bottom of this readme and extract it into the libraries folder of your processing sketchbook and your're ready to go.

Features
========
Gesture Recognition
It's possible to use a vast variety of gestures with the leap motion in processing.

<b>One finger gestures:</b>
- swipe left
- swipe right
- swipe up
- swipe down
- push
- pull

<b>One finger drawing gestures:</b>
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
========
https://github.com/mrzl/LeapMotionP5/blob/master/LeapMotionP5.zip

Examples
========
<b>Gesture Recognition</b>
<pre>
import com.onformative.leap.LeapMotionP5;

LeapMotionP5 leap;
String lastGesture = "";

public void setup() {
  size(500, 500);
  textSize(30);

  leap = new LeapMotionP5(this);
  leap.addGesture("swipeleft");
  leap.addGesture("swiperight");
  leap.addGesture("circle");
  leap.addGesture("triangle");
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
=======
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
