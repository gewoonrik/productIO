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

    val breads = Bread.getBreads
    println(breads.size)
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

  }
}
