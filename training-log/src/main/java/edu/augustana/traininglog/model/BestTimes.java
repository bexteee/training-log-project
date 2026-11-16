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

  // method that stores a best time, keeping only the faster one for each athlete, stroke, distance and kind.
  public void addBestTime(RaceBest raceBest) {
    
    if(raceBest == null) {
      throw new IllegalArgumentException("race best must not be null.");
    }
    // initializes our key object, which holds: AthleteId, StrokeType, distance and if it was race time or practice(test) time
    BestKey athleteInfo = new BestKey(raceBest.athleteId(), raceBest.stroke(), raceBest.distance(), raceBest.kind());

    Double currentBestTime = bestTimes.get(athleteInfo);

    if(currentBestTime == null || raceBest.time() < currentBestTime) {
      bestTimes.put(athleteInfo, raceBest.time());
    }
  }

  public Optional<Double> findReferenceTime(String athleteId, StrokeType stroke, double distance) {
    // creates a BestKey object setting the kind to race.
    BestKey race = new BestKey(athleteId, stroke, distance, BestKind.RACE);

    // searches on the hashmap for the race best time
    Double currentBestTimeRace = bestTimes.get(race);

    // checks race first. If there is a race best time, returns it.
    if(currentBestTimeRace != null) {
      return Optional.of(currentBestTimeRace);
    }

    // creates another bestkey object but now for test times
    BestKey test = new BestKey(athleteId, stroke, distance, BestKind.TEST);

    // if there is no race best time, search for test best time
    Double currentBestTimeTest = bestTimes.get(test);

    // returns the test time, or an empty Optional if there is none
    return Optional.ofNullable(currentBestTimeTest);
  }

  public int bestTimesCount() {
    return bestTimes.size();
  }
}
