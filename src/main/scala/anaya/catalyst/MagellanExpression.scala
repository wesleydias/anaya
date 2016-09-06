package anaya.catalyst

import anaya._
import org.apache.spark.sql.catalyst.InternalRow

trait MagellanExpression {

  private val SERIALIZERS = Map(
    1 -> new PointUDT,
    2 -> new LineUDT,
    3 -> new PolyLineUDT,
    5  -> new PolygonUDT)

  def newInstance(row: InternalRow): Shape = {
    SERIALIZERS.get(row.getInt(0)).fold(NullShape.asInstanceOf[Shape])(_.deserialize(row))
  }

  def serialize(shape: Shape): Any = {
    SERIALIZERS.get(shape.getType()).get.serialize(shape)
  }

}

