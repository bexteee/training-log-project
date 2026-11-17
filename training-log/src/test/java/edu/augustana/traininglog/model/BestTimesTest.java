package edu.augustana.traininglog.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Optional;

class BestTimesTest {
  
  // helper method created to create a RaceBest object and change variables of: distance, kind and time for tests
  private RaceBest createRaceBest(double distance, BestKind kind, double time) {
    return new RaceBest("A01", StrokeType.FREESTYLE, distance, kind, StartType.DIVE, time, LocalDate.of(2026, 1, 15));
  }

  // stored race best is found
  @Test 
  void raceBestFound() {
    BestTimes bestTimes = new BestTimes();

    bestTimes.addBestTime(createRaceBest(50, BestKind.RACE, 24.00));
    Optional<Double> raceBest = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 50);

   assertTrue(raceBest.isPresent());

   assertEquals(24.00, raceBest.get(), 0.001);
  }

  // with no race best, the test best is returned
  @Test 
  void onlyTestBestFound() {
    BestTimes bestTimes = new BestTimes();

    bestTimes.addBestTime(createRaceBest(100, BestKind.TEST, 47.00));

    Optional<Double> testBest = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 100);

    assertTrue(testBest.isPresent());

    assertEquals(47.00, testBest.get(), 0.001);
  }

  // race best is chosen even when the test best is faster
  @Test 
  void raceWinsOverTest() {
    BestTimes bestTimes = new BestTimes();

    bestTimes.addBestTime(createRaceBest(50, BestKind.RACE, 22.80));
    bestTimes.addBestTime(createRaceBest(50, BestKind.TEST, 21.00));

    Optional<Double> bestTime = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 50);

    assertTrue(bestTime.isPresent());

    assertEquals(22.80, bestTime.get(), 0.001);

    assertEquals(2, bestTimes.bestTimesCount());
  }

  // a different stroke finds nothing
  @Test 
  void wrongStrokeNotFound() {
    BestTimes bestTimes = new BestTimes();

    bestTimes.addBestTime(createRaceBest(100, BestKind.RACE, 50.60));

    Optional<Double> bestTime = bestTimes.findReferenceTime("A01", StrokeType.BACKSTROKE, 100);

    assertTrue(bestTime.isEmpty());
  }

  // another athlete cannot use A01's best
  @Test
  void wrongAthleteNotFound() {
    BestTimes bestTimes = new BestTimes();

    bestTimes.addBestTime(createRaceBest(50, BestKind.RACE, 25.00));

    Optional<Double> bestTime = bestTimes.findReferenceTime("A02", StrokeType.FREESTYLE, 50);

    assertTrue(bestTime.isEmpty());
  }

  // two athletes with the same time get separate entries, both found
  @Test 
  void sameTimeDifferentAthletes() {
    BestTimes bestTimes = new BestTimes();

    RaceBest athlete1 = new RaceBest("A01", StrokeType.FREESTYLE, 50.0, BestKind.RACE, StartType.DIVE, 23.00, LocalDate.of(2026, 1, 15));

    RaceBest athlete2 = new RaceBest("A02", StrokeType.FREESTYLE, 50.0, BestKind.RACE, StartType.DIVE, 23.00, LocalDate.of(2026, 1, 15));

    bestTimes.addBestTime(athlete1);
    bestTimes.addBestTime(athlete2);

    Optional<Double> bestTimeA1 = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 50);
    Optional<Double> bestTimeA2 = bestTimes.findReferenceTime("A02", StrokeType.FREESTYLE, 50);

    assertTrue(bestTimeA1.isPresent());
    assertTrue(bestTimeA2.isPresent());

    assertEquals(2, bestTimes.bestTimesCount());

    assertEquals(23.00, bestTimeA1.get(), 0.001);
    assertEquals(23.00, bestTimeA2.get(), 0.001);
  }

  // a different distance finds nothing 
  @Test 
  void wrongDistanceNotFound() {
    BestTimes bestTimes = new BestTimes();

    bestTimes.addBestTime(createRaceBest(50, BestKind.RACE, 24.00));

    Optional<Double> bestTime = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 100);

    assertTrue(bestTime.isEmpty());
  }

  // adding a faster time replaces the slower one, checks for only one entry as well.
  @Test 
  void fasterTimeReplacesSlower() {
    BestTimes bestTimes = new BestTimes();

    bestTimes.addBestTime(createRaceBest(50, BestKind.RACE, 24.50));
    bestTimes.addBestTime(createRaceBest(50, BestKind.RACE, 24.00));

    Optional<Double> bestTimeCheck = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 50);

    assertTrue(bestTimeCheck.isPresent());

    assertEquals(24.00, bestTimeCheck.get(), 0.001);

    assertEquals(1, bestTimes.bestTimesCount());
  }

  // adding a slower time doesn't replace the faster one
  @Test 
  void slowerTimeIgnored() {
    BestTimes bestTimes = new BestTimes();

    bestTimes.addBestTime(createRaceBest(100, BestKind.RACE, 49.00));
    bestTimes.addBestTime(createRaceBest(100, BestKind.RACE, 49.20));

    Optional<Double> bestTimeCheck = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 100);

    assertTrue(bestTimeCheck.isPresent());

    assertEquals(49.00, bestTimeCheck.get(), 0.001);

    assertEquals(1, bestTimes.bestTimesCount());
  }

  // an empty BestTimes finds nothing and has 0 entries
  @Test 
  void emptyBestTimesFindsNothing() {
    BestTimes bestTimes = new BestTimes();

    Optional<Double> bestTime = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 100);

    assertTrue(bestTime.isEmpty());

    assertEquals(0, bestTimes.bestTimesCount());
  }

  // adding null throws IllegalArgumentException
  @Test 
  void addNullRejected() {
    BestTimes bestTimes = new BestTimes();

    assertThrows(IllegalArgumentException.class, () -> bestTimes.addBestTime(null));
  }
}
