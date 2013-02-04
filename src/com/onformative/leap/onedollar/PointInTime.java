package com.onformative.leap.onedollar;

import processing.core.PVector;


public class PointInTime {

  private Integer time;
  private PVector position;

  protected PointInTime(PVector _position, Integer _time) {
    this.position = _position;
    this.time = _time;
  }

  protected PVector getPosition() {
    return this.position;
  }

  protected Integer getTime() {
    return time;
  }

}
