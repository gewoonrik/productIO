# Datasets:
# - weightedRoommate (from weightRoommate.R)

roommateWeekDist = weightedRoommate;

roommateWeekDist$day = weekdays(roommateWeekDist$date)

roommateDayDist = aggregate(roommateWeekDist$count, list(day = roommateWeekDist$day), mean)

write.csv(roommateDayDist, "~/Github/productIO/scripts/data/roommateDistribution.csv")

plot(roommateDayDist$x, type="h",  axes=FALSE, xlab = "", ylim = c(0,1), main = "Roommates at home during breakfast/lunch", ylab = "Roommate Factor")
axis(2)
axis(1, at=seq_along(roommateDayDist$x),labels=as.character(roommateDayDist$day), las=2)
