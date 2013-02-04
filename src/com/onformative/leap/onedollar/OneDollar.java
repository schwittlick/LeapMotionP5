package com.onformative.leap.onedollar;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

import processing.core.PApplet;
import processing.core.PVector;


public class OneDollar {

  private PApplet parent;
  private HashMap<Integer, Candidate> candidates;
  private HashMap<String, Gesture> templates;
  private HashMap<String, Callback> callbacks;
  private Recognizer recognizer;
  private Boolean online, verbose;
  private Integer maxLength, maxTime;


  /**
   * Constructor of the recognizer.
   * 
   * @param parent reference of the processing sketch
   */
  public OneDollar(PApplet parent) {

    System.out.println("# OneDollar-Unistroke-Recognizer - v" + this.getVersion()
        + " - https://github.com/voidplus/OneDollar-Unistroke-Recognizer");

    parent.registerDispose(this);
    this.parent = parent;
    this.candidates = new HashMap<Integer, Candidate>();
    this.callbacks = new HashMap<String, Callback>();
    this.recognizer = new Recognizer(parent, 64, 250, 45, 2);

    this.setOnline(true);
    this.setMinLength(50);
    this.setMaxLength(2500);
    this.setMaxTime(1000);

    this.setVerbose(false);
  }


  /**
   * Add new template to recognizer.
   * 
   * @param name name of template
   * @param points points as array of template
   * @return
   */
  public OneDollar addGesture(String name, Integer[] points) {
    if ((points.length % 2) == 0 && points.length > 0) {
      LinkedList<PVector> vectors = new LinkedList<PVector>();
      for (int i = 0, l = points.length; i < l; i += 2) {
        vectors.add(new PVector(points[i], points[i + 1]));
      }
      if (this.templates == null) {
        this.templates = new HashMap<String, Gesture>();
      }
      templates.put(name, new Gesture(name, vectors, this.recognizer));
    } else {
      System.err.println("Error.");
    }
    return this;
  }

  public OneDollar add(String name, Integer[] points) {
    return this.addGesture(name, points);
  }


  /**
   * Remove specified template from recognizer.
   * 
   * @param name name of template
   * @return
   */
  public OneDollar removeGesture(String name) {
    if (templates.containsKey(name)) {
      templates.remove(name);
    }
    return this;
  }

  public OneDollar remove(String name) {
    return this.removeGesture(name);
  }


  /**
   * Run the recognition and in case of success execute the binded callback.
   * 
   * @return
   */
  public synchronized Result check() {
    Result result = null;
    if (this.templates.size() > 0) {
      Candidate motion = null;

      if (this.candidates.size() > 0) {
        for (Integer id : this.candidates.keySet()) {
          motion = this.candidates.get(id);

          Deque<PointInTime> line = motion.getLine();
          LinkedList<PVector> positions = new LinkedList<PVector>();

          ListIterator<PointInTime> iterator = (ListIterator<PointInTime>) line.iterator();
          while (iterator.hasNext()) {
            PointInTime point = (PointInTime) iterator.next();
            PVector position = point.getPosition();
            positions.add(position);
          }

          // binded templates
          if (motion.hasBinds()) {
            result = this.recognizer.check(positions, this.templates, motion.getBinds());
            if (result != null) {

              if (this.verbose) {
                String object = this.candidates.get(id).getBind(result.getName()).getObjectClass();
                String method =
                    this.candidates.get(id).getBind(result.getName()).getCallbackString();
                System.out.println("# Candidate: " + id + " # Template: " + result.getName() + " ("
                    + result.getScore() + "%)" + " # Object: " + object + " # Method: " + method);
              }
              motion.fire(result.getName());

              if (this.callbacks.containsKey(result.getName())) {
                if (this.verbose) {
                  String object = this.callbacks.get(result.getName()).getObjectClass();
                  String method = this.callbacks.get(result.getName()).getCallbackString();
                  System.out.println("# Candidate: " + id + " # Template: " + result.getName()
                      + " (" + result.getScore() + "%)" + " # Object: " + object + " # Method: "
                      + method);
                }
                this.callbacks.get(result.getName()).fire(motion, result.getName());
              }
              this.candidates.get(id).clear(positions.getLast());
              return result;
            }
          }

          // all templates
          if (this.callbacks.size() > 0) {
            result = this.recognizer.check(positions, this.templates, this.callbacks);
            if (result != null) {
              if (this.verbose) {
                String object = this.callbacks.get(result.getName()).getObjectClass();
                String method = this.callbacks.get(result.getName()).getCallbackString();
                System.out.println("# Candidate: " + id + " # Template: " + result.getName() + " ("
                    + result.getScore() + "%)" + " # Object: " + object + " # Method: " + method);
              }
              this.callbacks.get(result.getName()).fire(motion, result.getName());
              this.candidates.get(id).clear(positions.getLast());
              return result;
            }
          }

        }
      }

    }
    return result;
  }


  /**
   * Draw all candidates points as lines.
   * 
   * @return
   */
  public synchronized OneDollar draw() {
    for (Integer id : candidates.keySet()) {
      candidates.get(id).draw();
    }
    return this;
  }


  /**
   * Bind sketch callback to candidate.
   * 
   * @param template name of added template
   * @param callback name of callback in current sketch
   * @return
   */
  public OneDollar bind(String template, String callback) {
    this.bind(template, this.parent, callback);
    return this;
  }

  /**
   * Bind object callback to candidate.
   * 
   * @param template name of added template
   * @param object object, which implemented the callback
   * @param callback name of callback
   * @return
   */
  public OneDollar bind(String template, Object object, String callback) {
    if (!this.callbacks.containsKey(template)) {
      this.callbacks.put(template, new Callback(object, callback));
    }
    return this;
  }


  /**
   * Bind sketch callback to candidate.
   * 
   * @param id unique id of candidate
   * @param template name of added template
   * @param callback name of callback in current sketch
   * @return
   */
  public OneDollar bind(Integer id, String template, String callback) {
    this.bind(id, template, this.parent, callback);
    return this;
  }


  /**
   * Bind object callback to candidate.
   * 
   * @param id unique id of candidate
   * @param template name of added template
   * @param object object, which implemented the callback
   * @param callback name of callback
   * @return
   */
  public OneDollar bind(Integer id, String template, Object object, String callback) {
    if (candidates.containsKey(id) && templates.containsKey(template)) {
      candidates.get(id).addBind(template, object, callback);
    }
    return this;
  }


  /**
   * Unbind callback from candidate.
   * 
   * @param id unique id of candidate
   * @param template name of the added template
   * @return
   */
  public OneDollar unbind(Integer id, String template) {
    if (candidates.containsKey(id)) {
      candidates.get(id).removeBind(template);
    }
    return this;
  }


  /**
   * Start new candidate.
   * 
   * @param id unique id of candidate
   * @param online explicit decision for online gesture
   * @see #setOnline(Boolean)
   * @return
   */
  public synchronized OneDollar start(Integer id, Boolean online) {
    if (!candidates.containsKey(id)) {
      candidates.put(id, new Candidate(this.parent, id, online, this.maxLength, this.maxTime));
    }
    return this;
  }


  /**
   * Start new candidate with the global defined setting for online gestures.
   * 
   * @param id unique id of candidate
   * @see #setOnline(Boolean)
   */
  public synchronized void start(Integer id) {
    this.start(id, this.online);
  }


  /**
   * Add a new point to specified candidate.
   * 
   * @param id unique id of candidate
   * @param point new x and y position of the candidate
   * @return
   */
  public synchronized void update(Integer id, PVector point) {
    if (candidates.containsKey(id)) {
      candidates.get(id).addPosition(point);
    }
  }


  /**
   * Add a new point to specified candidate.
   * 
   * @param id unique id of candidate
   * @param x new x position of the candidate
   * @param y new y position of the candidate
   */
  public synchronized void update(Integer id, float x, float y) {
    this.update(id, new PVector(x, y));
  }


  /**
   * Stop and delete a candidate.
   * 
   * @param id unique id of candidate
   */
  public synchronized void end(Integer id) {
    if (candidates.containsKey(id)) {
      candidates.remove(id);
    }
  }


  /**
   * Show result messages.
   * 
   * @param value
   * @return
   */
  public OneDollar setVerbose(Boolean value) {
    this.verbose = value;
    return this;
  }


  /**
   * Set the minimum equality in percent between candidate and template.
   * 
   * @param percent integer between 0 and 100
   * @return
   */
  public OneDollar setMinScore(Integer percent) {
    this.recognizer.setMinScore((float) percent);
    return this;
  }


  /**
   * Set, whether you use online gestures.
   * 
   * @param bool
   * @return
   */
  public OneDollar setOnline(Boolean bool) {
    this.online = bool;
    return this;
  }


  /**
   * Set the time to live of candidates points.
   * 
   * @param ms time in millisecond
   * @return
   */
  public OneDollar setMaxTime(Integer ms) {
    if (ms > 0) {
      this.maxTime = ms;
    }
    return this;
  }


  /**
   * Set the minimum length of a candidate.
   * 
   * @param length length in pixel
   * @return
   */
  public OneDollar setMinLength(Integer length) {
    if (length > 0) {
      this.recognizer.setMinLength(length);
    }
    return this;
  }


  /**
   * Set the maximum length of a candidate.
   * 
   * @param length length in pixel
   * @return
   */
  public OneDollar setMaxLength(Integer length) {
    if (length > 0) {
      this.maxLength = length;
    }
    return this;
  }


  /**
   * Set the rotation angle of the Unistroke Recognition algorithm.
   * 
   * @param angle angle in degree
   * @return
   */
  public OneDollar setRotationAngle(Integer degree) {
    if (degree > 0) {
      this.recognizer.setRotationAngle(degree);
    }
    return this;
  }


  /**
   * Set the fragmentation rate of the Unistroke Recognition algorithm.
   * 
   * @param number
   * @return
   */
  public OneDollar setFragmentationRate(Integer number) {
    if (number > 0) {
      this.recognizer.setFragmentationRate(number);
    }
    return this;
  }


  /**
   * Print all settings.
   */
  public String toString() {
    String feedback =
        "# OneDollar-Unistroke-Recognizer\n"
            + "#    Gesture Recognition Settings:\n"
            // + "#       Online Gestures:                "+this.online+"\n"
            + "#       Minimum Score:                  " + this.recognizer.getScore() + " %\n"
            + "#       Minimum Path Length:            " + this.recognizer.getMinLength() + "\n"
            + "#       Maximum Path Length:            " + this.maxLength + "\n"
            + "#       Maximum Time Length:            " + this.maxTime + "\n"
            + "#    Unistroke Algorithm Settings:\n" + "#       Fragmentation/Resampling Rate:  "
            + this.recognizer.getFragmentationRate() + "\n"
            + "#       Rotation Angle:                 " + this.recognizer.getRotationAngle()
            + "\n";
    return feedback;
  }


  /**
   * Delete references.
   */
  public void dispose() {
    this.parent = null;
    this.candidates = null;
    this.templates = null;
    this.recognizer = null;
    this.online = null;
    this.verbose = null;
  }


  /**
   * Return the version of the library.
   * 
   * @return String
   */
  public static String getVersion() {
    return VERSION;
  }

  public final static String VERSION = "0.2";

}
