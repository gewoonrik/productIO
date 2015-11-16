# required:
# - roommmatesByHour (from macs_graph.R)
# - events (from cumsum_bread.R)

# assign importance to hours (breakfast and lunch get higher importance)
hourImportance = c(0,0,0,0,0,0,2,2,2,2,2,2,2,2,0,0,0,0,0,0,0,0,0,0)
# Normalize it
hourImportance = hourImportance/max(hourImportance)

# weight  the roommate-count based on the hour importance
roommatesByHour$hour = as.numeric(format(roommatesByHour$time,"%H"))
roommatesByHour$weightedCount = unlist(Map(function(x) hourImportance[(x + 1)], roommatesByHour$hour))

# calculate weightedRoommate by day
weightedRoommate = aggregate(roommatesByHour$count, list(date = format(roommatesByHour$time, "%Y-%m-%d")), function(x) sum(x)/length(x))
weightedRoommate$date = as.POSIXct(weightedRoommate$date, origin="1970-01-01")
names(weightedRoommate)[names(weightedRoommate)=="x"] = "count"

# Assign weightedRoommate-factors to bread
#breadCheckouts = events[events$event == "OUT", "timestamp"]
#weightedRoommate$breadCheckout = as.Date(unlist(t(Map(function(x) {
#  nextBreadCheckout = subset(breadCheckouts, as.Date(timestamp) > as.Date(x, origin="1970-01-01"))
#  return(nextBreadCheckout$timestamp[1])
#}, weightedRoommate$date))), origin="1970-01-01")