package edu.augustana.traininglog.model;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

// stores every swimmer’s best times and finds the reference time for a set.
public class BestTimes {

  private final Map<BestKey, Double> bestTimes;

  // constructor runs only once, when the object is created
  public BestTimes() {
    bestTimes = new HashMap<>();
  }

  // method that stores a best time, keeping only the faster one for each athlete, stroke, distance,kind and start.
  public void addBestTime(RaceBest raceBest) {
    
    if(raceBest == null) {
      throw new IllegalArgumentException("race best must not be null.");
    }
    // initializes our key object, which holds: AthleteId, StrokeType, distance, if it was race time or practice(test) time and the start type(dive/push)
    BestKey athleteInfo = new BestKey(raceBest.athleteId(), raceBest.stroke(), raceBest.distance(), raceBest.kind(), raceBest.start());

    Double currentBestTime = bestTimes.get(athleteInfo);

    if(currentBestTime == null || raceBest.time() < currentBestTime) {
      bestTimes.put(athleteInfo, raceBest.time());
    }
  }

  // helper method to do lookups and change kind and start for different situations.
  private Double lookUpKindAndStart(String athleteId, StrokeType stroke, double distance, BestKind kind, StartType start) {
    BestKey key = new BestKey(athleteId, stroke, distance, kind, start);

    Double currentBestTime = bestTimes.get(key);

    return currentBestTime;
  }

  // finds the reference time for a set: athlete ID, stroke type, distance and set's start -> the best time + whether the start matched.
  // same start first: race/setStart (true), then test/setStart (true). If none, other start: race/otherStart (false), then test/otherStart (false).
  // returns Optional.empty() when there is none of the time options.
  public Optional<ReferenceTime> findReferenceTime(String athleteId, StrokeType stroke, double distance, StartType setStart) {
    StartType otherStart = (setStart == StartType.PUSH) ? StartType.DIVE : StartType.PUSH;

    Double raceAndSameStart = lookUpKindAndStart(athleteId, stroke, distance, BestKind.RACE, setStart);

    if(raceAndSameStart != null) {
      return Optional.of(new ReferenceTime(raceAndSameStart, true));
    }

    Double testAndSameStart = lookUpKindAndStart(athleteId, stroke, distance, BestKind.TEST, setStart);

    if(testAndSameStart != null) {
      return Optional.of(new ReferenceTime(testAndSameStart, true));
    }

    Double raceAndOtherStart = lookUpKindAndStart(athleteId, stroke, distance, BestKind.RACE, otherStart);

    if(raceAndOtherStart != null) {
      return Optional.of(new ReferenceTime(raceAndOtherStart, false));
    }

    Double testAndOtherStart = lookUpKindAndStart(athleteId, stroke, distance, BestKind.TEST, otherStart);

    if(testAndOtherStart != null) {
      return Optional.of(new ReferenceTime(testAndOtherStart, false));
    }

    return Optional.empty();
  }

  public int bestTimesCount() {
    return bestTimes.size();
  }
}
