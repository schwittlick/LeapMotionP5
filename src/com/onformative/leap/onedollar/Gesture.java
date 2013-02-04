package com.onformative.leap.onedollar;

import java.util.LinkedList;

import processing.core.PVector;


public class Gesture {

  private String name;
  private LinkedList<PVector> points;

  protected Gesture(String _name, LinkedList<PVector> _points, Recognizer _recognizer) {
    this.name = _name;
    this.points = _recognizer.getSample(_points);
    this.points = _recognizer.getRotateToZero(this.points);
    this.points =
        _recognizer.getScaleToSquare(this.points, _recognizer.getBoundingBox(this.points));
    this.points = _recognizer.getTranslateToOrigin(this.points);
  }

  protected LinkedList<PVector> getPoints() {
    return this.points;
  }

  protected String getName() {
    return this.name;
  }

}
