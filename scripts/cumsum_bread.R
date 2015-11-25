# Required:
# - products (from /api/products?format=csv)

# Fetch data
events = products[, c('timestamp','event')]
events$timestamp = as.Date(as.POSIXct(events$timestamp, origin="1970-01-01"))

# Mapping OUT IN to 0 1
eventToValue = c(1,-1) # for some reason R likes it reverse.
names(eventToValue) = c("OUT", "IN")
events$eventValue = eventToValue[events$event]

# cumsum
plot(events$timestamp, cumsum(events$eventValue), type="b", xlab = "Time", ylab = "Number of breads")