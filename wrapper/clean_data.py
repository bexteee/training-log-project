import csv
from datetime import datetime

# function that cleans each best time raw row and writes it to a clean file, it skips blank rows
def write_each_best_times_clean_row(raw_file_path, clean_file_path):
  with open(raw_file_path, newline="") as inFile, open(clean_file_path, "w", newline="") as outFile:
    reader = csv.DictReader(inFile)

    writer = csv.DictWriter(outFile, fieldnames=["swimmer_id", "stroke", "distance", "kind", "start", "best_time_in_seconds", "date_swum"])
    
    writer.writeheader()
  
    for row in reader:
      if any(row.values()):
        writer.writerow(best_times_clean_row(row))
        
# function that cleans each set log raw row and writes it to a clean file, it skips 3 kinds - blank rows, if equipment was used and if start is missing
def write_each_set_log_clean_row(raw_file_path, clean_file_path):
  with open(raw_file_path, newline="") as inFile, open(clean_file_path, "w", newline="") as outFile:
    
    reader = csv.DictReader(inFile)
    
    writer = csv.DictWriter(outFile, fieldnames=['swimmer_id', 'practice_date', 'set_number', 'stroke', 'start', 'rep_distance', 'send_off_in_seconds', 'rep_times'])
    
    writer.writeheader()
    
    for row in reader:
      if any(row.values()):
        
        if row['Did you use fins, paddles or other equipment?'].strip() == 'Yes':
          print(f"Skipped {row['Swimmer ID']} {row['Practice date']} set {row['Set number today']}: equipment used")

        elif row['Start'].strip() == "":
          print(f"Skipped {row['Swimmer ID']} {row['Practice date']} set {row['Set number today']}: start is missing")
        
        else:
          writer.writerow(set_clean_row(row))

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

# function that takes a row from the best times csv file and turns it to a cleaned dictionary
def best_times_clean_row(row: dict) -> dict:
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

# function that takes a row from the set csv file ad turns it to a cleaned dictionary
def set_clean_row(row: dict) -> dict:
  list_rep_times = times_from_txt_to_list(row['Rep times, in order, separated by commas'])
  
  txt_rep_times = [str(rep_times) for rep_times in list_rep_times]
  
  clean_dict = {
    "swimmer_id" : row["Swimmer ID"].strip(),
    "practice_date" : datetime_from_str_to_iso_text(row["Practice date"]),
    "set_number" : row["Set number today"].strip(),
    "stroke" : row["Stroke"].upper().strip(),
    "start" : row['Start'].strip().upper(),
    "rep_distance" : row["Rep distance (yards)"].strip(),
    "send_off_in_seconds" : time_from_txt_to_float(row['Send-off interval (the "@")']),
    "rep_times" : ";".join(txt_rep_times)
  }
  
  return clean_dict
  
if __name__ == "__main__":
  write_each_best_times_clean_row("data/raw/best_times_raw.csv", "data/clean/best_times.csv")
  
  write_each_set_log_clean_row("data/raw/set_log_raw.csv", "data/clean/sets.csv")