package edu.augustana.traininglog.io;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.io.IOException;
import java.util.List;

import edu.augustana.traininglog.model.BestKind;
import edu.augustana.traininglog.model.BestTimes;
import edu.augustana.traininglog.model.RaceBest;
import edu.augustana.traininglog.model.StartType;
import edu.augustana.traininglog.model.StrokeType;

public class BestTimesReader {

  public static BestTimes readBestTimes(String filePath) throws IOException {

    List<String> bestTimesLines = Files.readAllLines(Path.of(filePath));

    BestTimes bestTimes = new BestTimes();

    for(int line = 1; line < bestTimesLines.size(); line++) {
      String[] fields = bestTimesLines.get(line).split(",");

      String athleteId = fields[0];
      StrokeType stroke = StrokeType.valueOf(fields[1]);
      double distance = Double.parseDouble(fields[2]);
      BestKind kind = BestKind.valueOf(fields[3]);
      StartType start = StartType.valueOf(fields[4]);
      double time = Double.parseDouble(fields[5]);
      LocalDate date = LocalDate.parse(fields[6]);

      RaceBest raceBest = new RaceBest(athleteId, stroke, distance, kind, start, time, date);

      bestTimes.addBestTime(raceBest);

    }

    return bestTimes;
  }
}
