import com.onformative.leap.LeapMotionP5;

LeapMotionP5 leap;

void setup() {
  size(500, 500);
  leap = new LeapMotionP5(this);
}

void draw() {
  background(0);
  PVector fingerPos = leap.getTip(leap.getFinger(0));
  ellipse(fingerPos.x, fingerPos.y, 20, 20);
}

