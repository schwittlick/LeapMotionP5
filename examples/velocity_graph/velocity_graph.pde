import com.onformative.leap.LeapMotionP5;
import java.util.*;

LeapMotionP5 leap;
LinkedList<Integer> values;

public void setup() {
  size(800, 300);
  frameRate(120);
  leap = new LeapMotionP5(this);
  values = new LinkedList<Integer>();
  stroke(255);
}

int lastY = 0;

public void draw() {
  translate(0, 180);
  background(0);
  if (values.size() >= width) {
    values.removeFirst();
  }

  values.add((int) leap.getVelocity(leap.getHand(0)).y);
  //System.out.println((int) leap.getVelocity(leap.getHand(0)).y);
  int counter = 0;
  for (Integer val : values) {
    val = (int) map(val, 0, 1500, 0, height);
    line(counter, val, counter - 1, lastY);
    point(counter, val);
    lastY = val;
    counter++;
  }

  line(0, map(1300, 0, 1500, 0, height), width, map(1300, 0, 1500, 0, height));
}

