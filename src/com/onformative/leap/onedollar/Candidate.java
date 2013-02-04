package com.onformative.leap.onedollar;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

import processing.core.PVector;
import processing.core.PApplet;


public class Candidate {

  private PApplet parent;
  private Integer id;
  private Integer birth;
  private Deque<PointInTime> line = new LinkedList<PointInTime>();
  private HashMap<String, Callback> binds;
  private Boolean online;
  private Integer ruleMaxLength, ruleMaxTime;

  protected Candidate(PApplet _parent, Integer _id, Boolean _online, Integer _ruleMaxLength,
      Integer _ruleMaxTime) {
    this.parent = _parent;
    this.id = _id;
    this.birth = _parent.millis();
    this.line = new LinkedList<PointInTime>();
    this.online = _online;
    this.ruleMaxLength = _ruleMaxLength;
    this.ruleMaxTime = _ruleMaxTime;
  }

  protected Candidate addPosition(PVector _point) {

    Integer deltaTime = this.parent.millis() - birth;
    this.line.add(new PointInTime(_point, deltaTime));

    if (this.line.size() > 1 && this.online) {
      while (true) {
        if (this.isTooOld(this.line, this.ruleMaxTime)) {
          this.line.removeFirst();
        } else {
          if (this.isTooLong(this.line, this.ruleMaxLength)) {
            this.line.removeFirst();
          } else {
            break;
          }
        }
      }
    }

    return this;
  }

  protected PVector getMiddlePoint() {
    ListIterator<PointInTime> iterator = (ListIterator<PointInTime>) line.iterator();
    PVector result = null;
    while (iterator.hasNext()) {
      PVector point = ((PointInTime) iterator.next()).getPosition();
      if (result == null) {
        result = point;
      } else {
        result.x += point.x;
        result.y += point.y;
      }
    }
    result.x = (int) (result.x / line.size());
    result.y = (int) (result.y / line.size());
    return result;
  }

  protected PVector getFirstPoint() {
    return line.getLast().getPosition();
  }

  protected Candidate addBind(String _template, Object _object, String _callback) {
    if (this.binds == null) {
      this.binds = new HashMap<String, Callback>();
    }
    if (!binds.containsKey(_template)) {
      this.binds.put(_template, new Callback(_object, _callback));
    }
    return this;
  }

  protected Candidate removeBind(String _template) {
    if (binds.containsKey(_template)) {
      this.binds.remove(_template);
    }
    return this;
  }

  protected void fire(String _template) {
    if (binds.containsKey(_template)) {
      binds.get(_template).fire(this, _template);
    }
  }

  protected void clear(PVector _point) {
    this.line.clear();
    this.birth = this.parent.millis();
    this.addPosition(_point);
  }

  protected Deque<PointInTime> getLine() {
    return this.line;
  }

  protected void draw() {
    this.parent.beginShape();
    ListIterator<PointInTime> iterator = (ListIterator<PointInTime>) line.iterator();
    while (iterator.hasNext()) {
      PVector point = ((PointInTime) iterator.next()).getPosition();
      this.parent.vertex(point.x, point.y);
      this.parent.ellipse(point.x, point.y, 2, 2);
    }
    this.parent.endShape();
  }

  protected Callback getBind(String _template) {
    return this.binds.get(_template);
  }

  protected HashMap<String, Callback> getBinds() {
    return this.binds;
  }

  protected Boolean hasBinds() {
    if (binds != null) {
      if (binds.size() > 0) {
        return true;
      }
    }
    return false;
  }

  private Boolean isTooOld(Deque<PointInTime> _line, Integer _ms) {
    Integer first = _line.getFirst().getTime();
    Integer last = _line.getLast().getTime();
    return (last > (first + _ms)) ? true : false;
  }

  private Boolean isTooLong(Deque<PointInTime> _line, Integer _length) {
    return (this.calcLength(_line) > _length) ? true : false;
  }

  private Float calcLength(Deque<PointInTime> _line) {
    Float length = 0.0f;
    PVector tempPosition = null;
    ListIterator<PointInTime> iterator = (ListIterator<PointInTime>) _line.iterator();
    while (iterator.hasNext()) {
      PointInTime point = (PointInTime) iterator.next();
      PVector position = point.getPosition();
      if (tempPosition != null) {
        length += PVector.dist(tempPosition, position);
      }
      tempPosition = position;
    }
    return length;
  }

}
