package nl.tudelft.server.models

import org.bson.types.{BSONTimestamp, ObjectId}

abstract class Event {
  def _id : ObjectId
  def time : BSONTimestamp
  def user : String
}
