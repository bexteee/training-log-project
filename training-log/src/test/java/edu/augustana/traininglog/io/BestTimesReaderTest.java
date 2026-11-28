package edu.augustana.traininglog.io;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import edu.augustana.traininglog.model.BestTimes;
import edu.augustana.traininglog.model.ReferenceTime;
import edu.augustana.traininglog.model.StartType;
import edu.augustana.traininglog.model.StrokeType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

class BestTimesReaderTest {

  @TempDir
  Path tempDir;
  
  // it writes a temporary CSV with the real header plus the given rows and returns the file's path
  private String writesBestTimes(String...rows) throws IOException {
    String csvText = "swimmer_id,stroke,distance,kind,start,best_time_in_seconds,date_swum" + "\n" + String.join("\n", rows);

    Path csvFile = Files.writeString(tempDir.resolve("best_times.csv"), csvText);

    return csvFile.toString();
  }

  // one row is read into the right fields (it can be found by athlete, stroke, distance and start)
  @Test 
  void readsOneRow() throws IOException {
    String path = writesBestTimes("A01,FREESTYLE,50,RACE,DIVE,23.00,2026-02-07");

    BestTimes bestTimes = BestTimesReader.readBestTimes(path);

    Optional<ReferenceTime> bestTime = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 50, StartType.DIVE);

    assertTrue(bestTime.isPresent());

    assertEquals(23.00, bestTime.get().time(), 0.001);

    assertTrue(bestTime.get().startMatch());

    assertEquals(1, bestTimes.bestTimesCount());
  }

  // a file with only the header gives an empty BestTimes
  @Test 
  void headerOnlyGivesEmpty() throws IOException {
    String path = writesBestTimes();

    BestTimes bestTimes = BestTimesReader.readBestTimes(path);

    Optional<ReferenceTime> bestTime = bestTimes.findReferenceTime("A01", StrokeType.FREESTYLE, 50, StartType.DIVE);

    assertTrue(bestTime.isEmpty());

    assertEquals(0, bestTimes.bestTimesCount());
  }

  // every data row in the file is loaded (3 different keys -> 3 entries)
  @Test 
  void readsSeveralRows() throws IOException {
    String path = writesBestTimes("A01,FREESTYLE,50,RACE,DIVE,23.00,2026-02-07", "A02,BUTTERFLY,100,RACE,DIVE,50.00,2026-02-07", "A03,FREESTYLE,100,TEST,PUSH,53.00,2026-02-07");

    BestTimes bestTimes = BestTimesReader.readBestTimes(path);

    assertEquals(3, bestTimes.bestTimesCount());
  }

  // a file that does not exist throws IOException, instead of returning an empty BestTimes
  @Test 
  void missingFileThrows() throws IOException {
    assertThrows(IOException.class, 
      () -> BestTimesReader.readBestTimes(tempDir.resolve("missing.csv").toString())
    );
  }
}
