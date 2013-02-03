package com.onformative.leap;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import processing.core.PApplet;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Finger;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Hand;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.Tool;
import com.leapmotion.leap.Vector;

class SampleListener extends Listener {
  private PApplet p;
  private LeapMotionP5 leap;
  
  private int maxFrameCountToCheckForGestures = 250;

  public SampleListener(PApplet p, LeapMotionP5 leap) {
    this.p = p;
    this.leap = leap;
    leap.fingerPositions = new ConcurrentHashMap<Integer, Vector>();
    leap.fingerVelocity = new ConcurrentHashMap<Integer, Vector>();
    leap.toolPositions = new ConcurrentHashMap<Integer, Vector>();
    leap.fingerColors = new ConcurrentHashMap<Integer, Integer>();
    leap.toolColors = new ConcurrentHashMap<Integer, Integer>();
    leap.handPitch = new ConcurrentHashMap<Integer, Double>();
    leap.handRoll = new ConcurrentHashMap<Integer, Double>();
    leap.handYaw = new ConcurrentHashMap<Integer, Double>();
    leap.currentFrame = new Frame();
    leap.lastFrames = new LinkedList<Frame>();
  }

  public void onInit(Controller controller) {
    System.out.println("Initialized");
  }

  public void onConnect(Controller controller) {
    System.out.println("Connected");
  }

  public void onDisconnect(Controller controller) {
    System.out.println("Disconnected");
  }

  public void onExit(Controller controller) {
    System.out.println("Exited");
  }

  public void onFrame(Controller controller) {
    Frame frame = controller.frame();
    leap.currentFrame = frame;
    if(leap.lastFrames.size() >= maxFrameCountToCheckForGestures){
      leap.lastFrames.removeFirst();
    }
    leap.lastFrames.add(frame);

    for (Hand hand : frame.hands()) {
      int handId = hand.id();
      Vector handDirection = hand.direction();
      Vector palmNormal = hand.palmNormal();
      leap.handPitch.put(handId, (double) handDirection.pitch());
      leap.handRoll.put(handId, (double) (palmNormal.roll()));
      leap.handYaw.put(handId, (double) (handDirection.yaw()));
      // System.out.println("handpitch added");
    }

    for (Finger finger : frame.fingers()) {
      int fingerId = finger.id();
      int c = p.color(p.random(0, 255), p.random(0, 255), p.random(0, 255));
      leap.fingerColors.putIfAbsent(fingerId, c);
      leap.fingerPositions.put(fingerId, finger.tipPosition());
      leap.fingerVelocity.put(fingerId, finger.tipVelocity());
    }
    for (Tool tool : frame.tools()) {
      int toolId = tool.id();
      int c = p.color(p.random(0, 255), p.random(0, 255), p.random(0, 255));
      leap.toolColors.putIfAbsent(toolId, c);
      leap.toolPositions.put(toolId, tool.tipPosition());

    }
  }
}
