import Online.Day
import Bread.Bread
import org.joda.time.{LocalTime, LocalDate}

/**
 * Created by rik on 17/11/15.
 */
object Main {

  def main(args: Array[String]) {
    val days  =
      Day.getDays
        .map(day => {
          day.realDay -> day
        }).toMap



    println(Day.getDays
      .groupBy(day => day.realDay.getDayOfWeek)
      .map(
        tuple => (tuple._1, tuple._2.map(day => day.getDistinctPeopleHomeBetween(7, 14)))
      ))



    /* println(Day.getDays
       .groupBy(day => day.realDay.getDayOfWeek)
       .map(
         tuple => (tuple._1, tuple._2.map(day => Math.round(day.getAveragePeopleHomeBetween(7, 9)*100)/100.0))
       ))
*/
     val breads = Bread.getBreads
    val manHoursPerBreadDistinct =
    //get al breads that have online information for all days the bread has been eaten
      breads.filter(bread =>
        bread.getDaysEaten.forall(day => days.get(new LocalDate(day)).isDefined)
      )
        //map them to manhours
        .map(bread => {
        bread.getDaysEaten
          //get the corresponding online data for each day
          .map(day => days.get(new LocalDate(day)).get)
          //for each day calculate the manhours eating that day
          .map(day => {
          day.getDistinctPeopleHomeBetween(7, 14)
        })
          .sum
      }
        )

    println(manHoursPerBreadDistinct)



    //calculate days between check-in and check-out grouped by day of week
    println("days between check-in and check-out grouped by day of week ")
    println(Bread.getBreads
       .groupBy(bread => bread.getDateTimeIn.getDayOfWeek)
       .map {
       case (day, breads_) =>
         (day, breads_.map(bread => bread.getDaysEaten.size))
       })
    /*
         val manHoursPerBread =
           //get al breads that have online information for all days the bread has been eaten
           breads.filter(bread =>
             bread.getDaysEaten.forall(day => days.get(new LocalDate(day)).isDefined)
           )
           //map them to manhours
           .map(bread => {
             bread.getDaysEaten
               //get the corresponding online data for each day
               .map(day => days.get(new LocalDate(day)).get)
               //for each day calculate the manhours eating that day
               .map(day => {
                 day.getAveragePeopleHomeBetween(7, 14)
               })
               .sum
           }
           )
         println(manHoursPerBread)
     */
  }
}
