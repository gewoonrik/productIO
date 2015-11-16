# Datasets:
# - products (IN/OUT event datasets)
# - macs (Mac recording dataset)
# - phoneMacs (list of macs to be filtered on)

# Filter macs (remove empty ones and filter on user)
nonEmptyMacs = subset(macs, macs != "" & user == "Rik")

# Filter macs 
filteredMacsList = unname(Map(function(macLine) {
  unfilteredMacs = strsplit(as.character(macLine), '|', fixed = TRUE)[[1]]
  return(intersect(phoneMacs, unfilteredMacs))
}, nonEmptyMacs[, 'macs']))

nonEmptyMacs[, 'macCount'] = unlist(Map(function(x) length(x), filteredMacsList))

# Convert time
nonEmptyMacs[, 'time'] = as.POSIXct(nonEmptyMacs[,'time'], origin="1970-01-01")

# group by day
roommatesByDay = aggregate(nonEmptyMacs$macCount, list(time = format(nonEmptyMacs$time, "%Y-%m-%d")), max)
roommatesByDay$time = as.POSIXct(roommatesByDay$time, origin="1970-01-01")
names(roommatesByDay)[names(roommatesByDay)=="x"] = "count"
plot(roommatesByDay$time, roommatesByDay$count, type="s", main = "Roommates at home by day", xlab = "Time", ylab = "# of roommates")

# group by hour
roommatesByHour = aggregate(nonEmptyMacs$macCount, list(time = format(nonEmptyMacs$time, "%Y-%m-%d %H:00:00")), max)
roommatesByHour$time = strptime(roommatesByHour$time,"%Y-%m-%d %H:%M:%S")
names(roommatesByHour)[names(roommatesByHour)=="x"] = "count"
plot(roommatesByHour$time, roommatesByHour$count, type="h", main = "Roommates at home by hour", xlab = "time", ylab = "# of roommates")

# Save roommates/hour to file
write.csv(roommatesByHour, file = "~/Github/productIO/scripts/data/roommatesByHour.csv")
