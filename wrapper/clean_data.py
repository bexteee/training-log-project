import csv
from datetime import datetime

# method that takes the file's path and prints each row
def print_each_row(path):
  
  with open(path, newline="") as file:
    reader = csv.DictReader(file)
    for row in reader:
      print(row["Swimmer ID"], row["Date swum"])

# method to convert time data from text to float
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

# method to convert times data from text to a list of times
def times_from_txt_to_list(times: str) -> list[float]:
  splitted_times = times.split(",")
  times_list = [time_from_txt_to_float(time) for time in splitted_times]

  return times_list

# method to convert date from string to ISO text
def datetime_from_str_to_iso_text(date_time: str) -> str:
  date_time = date_time.strip()
  
  if date_time == "":
    raise ValueError("date is empty")
  
  parsed = datetime.strptime(date_time, "%m/%d/%Y")
  
  return parsed.date().isoformat()
  
if __name__ == "__main__":
  print_each_row("data/raw/best_times_raw.csv")