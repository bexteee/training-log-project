package edu.augustana.traininglog.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Optional;

class BestTimesTest {
  
  // helper method created to create a RaceBest object and change variables of: distance, kind, time and start for tests
  private RaceBest createRaceBest(double distance, BestKind kind, double time, StartType start) {
    return new RaceBest("A01", StrokeType.FREESTYLE, distance, kind, start, time, LocalDate.of(2026, 1, 15));
  }

  // stored race best is found
  @Test 
  void raceBestFound() {
    BestTimes bestTimes = new BestTimes();

    bestTimes.addBestTime(createRaceBest(50, BestKind.RACE, 24.00, StartType.DIVE));
    Optional<ReferenceTime> raceBest = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 50, StartType.DIVE);

   assertTrue(raceBest.isPresent());

   assertEquals(24.00, raceBest.get().time(), 0.001);

   assertTrue(raceBest.get().startMatch());
  }

  // with no race best, the test best is returned
  @Test 
  void onlyTestBestFound() {
    BestTimes bestTimes = new BestTimes();

    bestTimes.addBestTime(createRaceBest(100, BestKind.TEST, 47.00, StartType.DIVE));

    Optional<ReferenceTime> testBest = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 100, StartType.DIVE);

    assertTrue(testBest.isPresent());

    assertTrue(testBest.get().startMatch());

    assertEquals(47.00, testBest.get().time(), 0.001);
  }

  // race best is chosen even when the test best is faster
  @Test 
  void raceWinsOverTest() {
    BestTimes bestTimes = new BestTimes();

    bestTimes.addBestTime(createRaceBest(50, BestKind.RACE, 22.80, StartType.DIVE));
    bestTimes.addBestTime(createRaceBest(50, BestKind.TEST, 21.00, StartType.DIVE));

    Optional<ReferenceTime> bestTime = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 50, StartType.DIVE);

    assertTrue(bestTime.isPresent());

    assertTrue(bestTime.get().startMatch());

    assertEquals(22.80, bestTime.get().time(), 0.001);

    assertEquals(2, bestTimes.bestTimesCount());
  }

  // a different stroke finds nothing
  @Test 
  void wrongStrokeNotFound() {
    BestTimes bestTimes = new BestTimes();

    bestTimes.addBestTime(createRaceBest(100, BestKind.RACE, 50.60, StartType.DIVE));

    Optional<ReferenceTime> bestTime = bestTimes.findReferenceTime("A01", StrokeType.BACKSTROKE, 100, StartType.DIVE);

    assertTrue(bestTime.isEmpty());
  }

  // another athlete cannot use A01's best
  @Test
  void wrongAthleteNotFound() {
    BestTimes bestTimes = new BestTimes();

    bestTimes.addBestTime(createRaceBest(50, BestKind.RACE, 25.00, StartType.DIVE));

    Optional<ReferenceTime> bestTime = bestTimes.findReferenceTime("A02", StrokeType.FREESTYLE, 50, StartType.DIVE);

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

    Optional<ReferenceTime> bestTimeA1 = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 50, StartType.DIVE);
    Optional<ReferenceTime> bestTimeA2 = bestTimes.findReferenceTime("A02", StrokeType.FREESTYLE, 50, StartType.DIVE);

    assertTrue(bestTimeA1.isPresent());
    assertTrue(bestTimeA2.isPresent());

    assertTrue(bestTimeA1.get().startMatch());
    assertTrue(bestTimeA2.get().startMatch());

    assertEquals(2, bestTimes.bestTimesCount());

    assertEquals(23.00, bestTimeA1.get().time(), 0.001);
    assertEquals(23.00, bestTimeA2.get().time(), 0.001);
  }

  // a different distance finds nothing 
  @Test 
  void wrongDistanceNotFound() {
    BestTimes bestTimes = new BestTimes();

    bestTimes.addBestTime(createRaceBest(50, BestKind.RACE, 24.00, StartType.DIVE));

    Optional<ReferenceTime> bestTime = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 100, StartType.DIVE);

    assertTrue(bestTime.isEmpty());
  }

  // adding a faster time replaces the slower one, checks for only one entry as well.
  @Test 
  void fasterTimeReplacesSlower() {
    BestTimes bestTimes = new BestTimes();

    bestTimes.addBestTime(createRaceBest(50, BestKind.RACE, 24.50, StartType.DIVE));
    bestTimes.addBestTime(createRaceBest(50, BestKind.RACE, 24.00, StartType.DIVE));

    Optional<ReferenceTime> bestTimeCheck = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 50, StartType.DIVE);

    assertTrue(bestTimeCheck.isPresent());

    assertTrue(bestTimeCheck.get().startMatch());

    assertEquals(24.00, bestTimeCheck.get().time(), 0.001);

    assertEquals(1, bestTimes.bestTimesCount());
  }

  // adding a slower time doesn't replace the faster one
  @Test 
  void slowerTimeIgnored() {
    BestTimes bestTimes = new BestTimes();

    bestTimes.addBestTime(createRaceBest(100, BestKind.RACE, 49.00, StartType.DIVE));
    bestTimes.addBestTime(createRaceBest(100, BestKind.RACE, 49.20, StartType.DIVE));

    Optional<ReferenceTime> bestTimeCheck = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 100, StartType.DIVE);

    assertTrue(bestTimeCheck.isPresent());

    assertTrue(bestTimeCheck.get().startMatch());

    assertEquals(49.00, bestTimeCheck.get().time(), 0.001);

    assertEquals(1, bestTimes.bestTimesCount());
  }

  // an empty BestTimes finds nothing and has 0 entries
  @Test 
  void emptyBestTimesFindsNothing() {
    BestTimes bestTimes = new BestTimes();

    Optional<ReferenceTime> bestTime = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 100, StartType.DIVE);

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
