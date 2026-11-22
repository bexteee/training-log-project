import csv
from datetime import datetime

# function that cleans each raw row and writes it to the clean file, it skips blank rows
def write_each_clean_row(raw_file_path, clean_file_path):
  with open(raw_file_path, newline="") as inFile, open(clean_file_path, "w", newline="") as outFile:
    reader = csv.DictReader(inFile)

    writer = csv.DictWriter(outFile, fieldnames=["swimmer_id", "stroke", "distance", "kind", "start", "best_time_in_seconds", "date_swum"])
    
    writer.writeheader()
  
    for row in reader:
      if any(row.values()):
        writer.writerow(clean_row(row))

# function to convert time data from text to float
def time_from_txt_to_float(time: str) -> float:
  time_stripped = time.strip()

  if time_stripped == "":
    raise ValueError("time is empty")

  if ":" not in time_stripped:
    return float(time_stripped)
  else:
    time_splitted = time_stripped.split(":")
    minutes = float(time_splitted[0]) * 60
    seconds = float(time_splitted[1])
    return minutes + seconds

# function to convert times data from text to a list of times
def times_from_txt_to_list(times: str) -> list[float]:
  splitted_times = times.split(",")
  times_list = [time_from_txt_to_float(time) for time in splitted_times]

  return times_list

# function to convert date from string to ISO text
def datetime_from_str_to_iso_text(date_time: str) -> str:
  date_time = date_time.strip()
  
  if date_time == "":
    raise ValueError("date is empty")
  
  parsed = datetime.strptime(date_time, "%m/%d/%Y")
  
  return parsed.date().isoformat()

# function that takes a row from the csv file and turns it to a cleaned dictionary
def clean_row(row: dict) -> dict:
  clean_dict = {
    "swimmer_id" : row["Swimmer ID"].strip(),
    "stroke" : row["Stroke"].upper().strip(),
    "distance" : row["Distance (yards)"].strip(),
    "kind" : row["Race or practice test?"].upper().strip(),
    "start" : row["Start"].upper().strip(),
    "best_time_in_seconds" : time_from_txt_to_float(row["Time"]),
    "date_swum" : datetime_from_str_to_iso_text(row["Date swum"])
  }
  
  return clean_dict
  
if __name__ == "__main__":
  write_each_clean_row("data/raw/best_times_raw.csv", "data/clean/best_times.csv")