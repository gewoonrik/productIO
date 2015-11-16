# Datasets:
# - weightedRoommate (from weightRoommate.R)

roommateWeekDist = weightedRoommate;

roommateWeekDist$day = weekdays(roommateWeekDist$date)

roommateDayDist = aggregate(roommateWeekDist$count, list(day = roommateWeekDist$day), mean)

write.csv(roommateDayDist, "~/Github/productIO/scripts/data/roommateDistribution.csv")
