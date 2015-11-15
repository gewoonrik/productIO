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
roommatesByDay = aggregate(nonEmptyMacs$macCount, list(Time = format(nonEmptyMacs$time, "%Y-%m-%d")), max)
roommatesByDay$Time = as.POSIXct(roommatesByDay$Time, origin="1970-01-01")
plot(roommatesByDay$Time, roommatesByDay$x, type="s", main = "Roommates at home by day", xlab = "Time", ylab = "# of roommates")

# group by hour
roommatesByHour = aggregate(nonEmptyMacs$macCount, list(Time = format(nonEmptyMacs$time, "%Y-%m-%d %H:00:00")), max)
roommatesByHour$Time = strptime(roommatesByHour$Time,"%Y-%m-%d %H:%M:%S")
plot(roommatesByHour$Time, roommatesByHour$x, type="h", main = "Roommates at home by hour", xlab = "Time", ylab = "# of roommates")
