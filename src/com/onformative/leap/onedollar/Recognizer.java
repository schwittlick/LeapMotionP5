package com.onformative.leap.onedollar;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Stack;

import processing.core.PApplet;
import processing.core.PVector;


/**
 * 
 * The $1 Gesture Recognizer is a research project by Wobbrock, Wilson and Li of the University of
 * Washington and Microsoft Research. It describes a simple algorithm for accurate and fast
 * recognition of drawn gestures.
 * 
 * Gestures can be recognised at any position, scale, and under any rotation. The system requires
 * little training, achieving a 97% recognition rate with only one template for each gesture.
 * 
 * http://depts.washington.edu/aimgroup/proj/dollar/
 * 
 */

public class Recognizer {

  private final Float PHI, HALFDIAGONAL;
  private Integer fragmentation, size, angle, angleStep;
  private Integer minLength;
  private Float score;

  public Recognizer(PApplet _parent, Integer _fragmentation, Integer _size, Integer _angle,
      Integer _angleStep) {

    this.PHI = 0.5f * (-1.0f + PApplet.sqrt(5.0f));
    this.HALFDIAGONAL = 0.5f * PApplet.sqrt(_size * _size + _size * _size);

    this.fragmentation = _fragmentation;
    this.size = _size;
    this.angle = _angle;

    this.setRotationAngle(_angleStep);
    this.setMinScore(85.0f);
    this.setMinLength(75);
  }

  protected LinkedList<PVector> getSample(LinkedList<PVector> _points) {

    Float seperator = this.getPathLength(_points) / (this.getFragmentation() - 1);
    Float distance = 0.0f;

    LinkedList<PVector> result = new LinkedList<PVector>();
    Stack<PVector> stack = new Stack<PVector>();

    ListIterator<PVector> iterator = _points.listIterator();
    while (iterator.hasNext()) {
      PVector point = (PVector) iterator.next();
      stack.push(point);
    }

    while (!stack.empty()) {
      PVector pPos = stack.pop();
      if (result.isEmpty()) {
        result.add(pPos);
      } else {
        if (stack.empty()) {
          result.add(pPos);
          continue;
        }
        PVector pos = stack.peek();
        Float localDistance = PVector.dist(pPos, pos);
        if ((distance + localDistance) >= seperator) {
          PVector resample =
              new PVector(pPos.x + ((seperator - distance) / localDistance) * (pos.x - pPos.x),
                  pPos.y + ((seperator - distance) / localDistance) * (pos.y - pPos.y));
          result.add(resample);
          if (result.size() == (this.getFragmentation() - 1)) {
            result.add(stack.lastElement());
            stack.clear();
            return result;
          }
          stack.push(resample);
          distance = 0.0f;
        } else {
          distance += localDistance;
        }
      }
    }

    return result;
  }

  protected Float getPathLength(LinkedList<PVector> _points) {

    Float length = 0.0f;
    PVector tempVector = null;
    ListIterator<PVector> iterator = _points.listIterator();

    while (iterator.hasNext()) {
      PVector point = (PVector) iterator.next();
      if (tempVector != null) {
        length += PVector.dist(tempVector, point);
      }
      tempVector = point;
    }

    return length;
  }

  protected Integer getFragmentation() {
    return this.fragmentation;
  }

  protected LinkedList<PVector> getRotateToZero(LinkedList<PVector> _points) {
    PVector centroid = this.getCentroid(_points);
    Float theta =
        PApplet.atan2(centroid.y - _points.getFirst().y, centroid.x - _points.getFirst().x);

    return this.getRotateBy(_points, -theta);
  }

  protected PVector getCentroid(LinkedList<PVector> _points) {

    PVector centroid = new PVector(0.0f, 0.0f);
    Integer length = _points.size();

    ListIterator<PVector> iterator = _points.listIterator();
    while (iterator.hasNext()) {
      PVector point = (PVector) iterator.next();
      centroid.x += point.x;
      centroid.y += point.y;
    }
    centroid.x /= length;
    centroid.y /= length;

    return centroid;
  }

  protected LinkedList<PVector> getRotateBy(LinkedList<PVector> _points, Float _theta) {

    PVector centroid = this.getCentroid(_points);
    Float sin = PApplet.sin(_theta);
    Float cos = PApplet.cos(_theta);
    LinkedList<PVector> result = new LinkedList<PVector>();

    ListIterator<PVector> iterator = _points.listIterator();
    while (iterator.hasNext()) {
      PVector point = (PVector) iterator.next();
      result.add(new PVector((point.x - centroid.x) * cos - (point.y - centroid.y) * sin
          + centroid.x, (point.x - centroid.x) * sin + (point.y - centroid.y) * cos + centroid.y,
          0.0f));
    }

    return result;
  }

  protected Rectangle getBoundingBox(LinkedList<PVector> _points) {

    Float minX, maxX, minY, maxY;
    maxX = maxY = Float.NEGATIVE_INFINITY;
    minX = minY = Float.POSITIVE_INFINITY;

    ListIterator<PVector> iterator = _points.listIterator();
    while (iterator.hasNext()) {
      PVector point = (PVector) iterator.next();
      minX = PApplet.min(point.x, minX);
      maxX = PApplet.max(point.x, maxX);
      minY = PApplet.min(point.y, minY);
      maxY = PApplet.max(point.y, maxY);
    }

    return new Rectangle(minX, minY, maxX - minX, maxY - minY);
  }

  protected LinkedList<PVector> getScaleToSquare(LinkedList<PVector> _points, Rectangle _box) {

    LinkedList<PVector> result = new LinkedList<PVector>();

    ListIterator<PVector> iterator = _points.listIterator();
    while (iterator.hasNext()) {
      PVector point = (PVector) iterator.next();
      result.add(new PVector(point.x * (this.size / _box.getDimension().getWidth()), point.y
          * (this.size / _box.getDimension().getHeight()), 0.0f));
    }

    return result;
  }

  protected LinkedList<PVector> getTranslateToOrigin(LinkedList<PVector> _points) {

    PVector centroid = this.getCentroid(_points);
    LinkedList<PVector> result = new LinkedList<PVector>();

    ListIterator<PVector> iterator = _points.listIterator();
    while (iterator.hasNext()) {
      PVector point = (PVector) iterator.next();
      result.add(new PVector(point.x - centroid.x, point.y - centroid.y));
    }

    return result;
  }

  protected synchronized Result check(LinkedList<PVector> _gesture,
      HashMap<String, Gesture> _templates, HashMap<String, Callback> _binds) {

    if (this.getPathLength(_gesture) > this.minLength) {

      LinkedList<PVector> points;
      points = this.getSample(_gesture);
      points = this.getRotateToZero(points);
      points = this.getScaleToSquare(points, this.getBoundingBox(points));
      points = this.getTranslateToOrigin(points);

      Float a = Float.POSITIVE_INFINITY;
      Float b = Float.POSITIVE_INFINITY;

      Gesture template = null;
      Gesture tempTemplate = null;

      for (String key : _binds.keySet()) {
        tempTemplate = _templates.get(key);

        Float distance =
            getDistanceAtBestAngle(points, tempTemplate, -(this.angle), this.angle, this.angleStep);
        if (distance < a) {
          b = a;
          a = distance;
          template = tempTemplate;
        } else if (distance < b) {
          b = distance;
        }
      }

      if (template != null) {

        Float score = 1.0f - (a / HALFDIAGONAL);
        Float otherScore = 1.0f - (b / HALFDIAGONAL);
        Float ratio = otherScore / score;

        if (score > this.score) {
          return new Result(template.getName(), score, ratio);
        }
      }
    }

    return null;
  }

  private Float getDistanceAtBestAngle(LinkedList<PVector> _points, Gesture _template,
      Integer _aDegree, Integer _bDegree, Integer _tresholdDegree) {

    Float a = PApplet.radians(_aDegree);
    Float b = PApplet.radians(_bDegree);
    Float treshold = PApplet.radians(_tresholdDegree);

    Float alpha = (PHI * a) + (1.0f - PHI) * b;
    Float beta = (1.0f - PHI) * a + (PHI * b);
    Float pathA = getDistanceAtAngle(_points, _template, alpha);
    Float pathB = getDistanceAtAngle(_points, _template, beta);

    if (pathA != Float.POSITIVE_INFINITY && pathB != Float.POSITIVE_INFINITY) {
      while (PApplet.abs(b - a) > treshold) {
        if (pathA < pathB) {
          b = beta;
          beta = alpha;
          pathB = pathA;
          alpha = PHI * a + (1.0f - PHI) * b;
          pathA = getDistanceAtAngle(_points, _template, alpha);
        } else {
          a = alpha;
          alpha = beta;
          pathA = pathB;
          beta = (1.0f - PHI) * a + PHI * b;
          pathB = getDistanceAtAngle(_points, _template, beta);
        }
      }
      return PApplet.min(pathA, pathB);
    } else {
      return Float.POSITIVE_INFINITY;
    }
  }

  private Float getDistanceAtAngle(LinkedList<PVector> _points, Gesture _template, Float _theta) {
    _points = getRotateBy(_points, _theta);
    return getPathDistance(_points, _template.getPoints());
  }

  private Float getPathDistance(LinkedList<PVector> _pointsA, LinkedList<PVector> _pointsB) {
    if (_pointsA.size() != _pointsB.size()) {
      return Float.POSITIVE_INFINITY;
    } else {
      Float length = 0.0f;
      ListIterator<PVector> iterator = _pointsA.listIterator();
      while (iterator.hasNext()) {
        Integer index = iterator.nextIndex();
        iterator.next();
        length += PVector.dist(_pointsA.get(index), _pointsB.get(index));
      }
      return (length / _pointsA.size());
    }
  }

  protected void setRotationAngle(Integer _degree) {
    if (_degree > 0) {
      this.angleStep = _degree;
    } else {
      this.angleStep = 2;
    }
  }

  protected void setMinScore(Float _score) {
    if (_score > 0 && _score < 100) {
      this.score = _score / 100;
    } else {
      this.score = 0.85f;
    }
  }

  protected Integer getScore() {
    return (int) (this.score * 100);
  }

  protected void setMinLength(Integer _length) {
    if (_length > 0) {
      this.minLength = _length;
    } else {
      this.minLength = 75;
    }
  }

  protected Integer getMinLength() {
    return this.minLength;
  }

  protected Integer getRotationAngle() {
    return this.angleStep;
  }

  protected Integer getFragmentationRate() {
    return this.fragmentation;
  }

  public Integer getSize() {
    return this.size;
  }

  public void setFragmentationRate(Integer _number) {
    if (_number > 0) {
      this.fragmentation = _number;
    } else {
      this.fragmentation = 64;
    }
  }

}
