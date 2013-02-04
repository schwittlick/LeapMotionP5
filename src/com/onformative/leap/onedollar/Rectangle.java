package com.onformative.leap.onedollar;

import processing.core.PVector;


public class Rectangle {

  private PVector position;
  private Dimension dimension;

  protected Rectangle(PVector _position, Dimension _dimension) {
    this.position = _position;
    this.dimension = _dimension;
  }

  protected Rectangle(Float x, Float y, Float width, Float height) {
    this(new PVector(x, y), new Dimension(width, height));
  }

  protected PVector getPosition() {
    return position;
  }

  protected Dimension getDimension() {
    return dimension;
  }

}
