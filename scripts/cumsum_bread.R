# Fetch data
timestamps = products["timestamp"]
events = products["event"]
xs = c(t(timestamps))
ys = c(t(events))

# Mapping OUT IN to 0 1
val_event=c(-1,1)
names(val_event)=c("OUT", "IN")
ysm = val_event[ys]
plot(xs, ysm)

# cumsum
plot(xs, cumsum(ysm), type="b", main = "Bread in household over time", xlab = "Time", ylab = "# of bread")
