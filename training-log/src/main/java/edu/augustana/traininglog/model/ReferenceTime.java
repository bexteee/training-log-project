package edu.augustana.traininglog.model;

// the result of a reference-time lookup: the time and whether its start matches the set's start 
public record ReferenceTime(double time, boolean startMatch)  {

  public ReferenceTime {
    if(time <= 0) {
      throw new IllegalArgumentException("reference time must be greater than 0.");
    }
  }
}
