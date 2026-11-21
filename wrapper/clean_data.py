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



