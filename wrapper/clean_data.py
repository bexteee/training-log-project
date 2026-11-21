# method to convert time data from text to float/double in Java
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

# method to covert times data from text to a list of times
def times_from_txt_to_list(times: str) -> list[float]:
  splitted_times = times.split(",")
  times_list = [time_from_txt_to_float(time) for time in splitted_times]

  return times_list


