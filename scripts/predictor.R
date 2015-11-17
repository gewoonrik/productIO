# Datasets:
# - roommateDayDist (From )
# - breads: id;timestamp_begin;timestamp_end

breadDuration = breads
roommatePredictor = roommateDayDist

# Calculate delta
deltaPerDay = roommatePredictor$x; # todo do not use mean
names(deltaPerDay) = roommatePredictor$day

# get days of bread
breadDuration$days = apply(breadDuration, 1, function(e) {
  start = as.Date(as.POSIXct(e['timestampStart'], origin="1970-01-01"))
  end = as.Date(as.POSIXct(e['timestampEnd'], origin="1970-01-01"))
  duration = c()
  roommateSum = 0
  while(start <= end) {
    weekday = weekdays(start)
    duration = c(duration, weekday)
    roommateSum = roommateSum + deltaPerDay[weekday]
    start = start + 1
  }
  c(duration, roommateSum)
})