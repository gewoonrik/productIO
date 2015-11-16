# required:
# - roommmatesByHour (from macs_graph.R)
# - events (from cumsum_bread.R)

# assign importance to hours (breakfast and lunch get higher importance)
hourImportance = c(0,0,0,0,0,0,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0)
# Normalize it
hourImportance = hourImportance/max(hourImportance)

# weight  the roommate-count based on the hour importance
roommatesByHour$hour = as.numeric(format(roommatesByHour$time,"%H"))
roommatesByHour$weight = unlist(Map(function(x) hourImportance[(x + 1)], roommatesByHour$hour))

# calculate weightedRoommate by day
roommatesByHour$weightedCount = roommatesByHour$weight * roommatesByHour$count
weightedRoommate = aggregate(roommatesByHour$weightedCount, list(date = format(roommatesByHour$time, "%Y-%m-%d")), function(x) sum(x)/length(x))
weightedRoommate$date = as.POSIXct(weightedRoommate$date, origin="1970-01-01")
names(weightedRoommate)[names(weightedRoommate)=="x"] = "count"