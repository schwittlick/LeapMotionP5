package com.onformative.leap.onedollar;

public class Result {

  private String name;
  private Float score, ratio;

  protected Result(String _name, Float _score, Float _ratio) {
    this.name = _name;
    this.score = (float) (Math.round(_score * 10000) / 100.);
    this.ratio = _ratio;
  }

  protected String getName() {
    return this.name;
  }

  protected Float getScore() {
    return this.score;
  }

  protected Float getRatio() {
    return this.ratio;
  }

}
