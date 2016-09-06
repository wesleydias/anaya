package anaya.io

import java.io.{DataInput, DataOutput}

import anaya.Shape
import org.apache.commons.io.EndianUtils
import org.apache.hadoop.io.Writable

private[anaya] class ShapeWritable(shapeType: Int) extends Writable {

  var shape: Shape = _

  override def write(dataOutput: DataOutput): Unit = {
    ???
  }

  override def readFields(dataInput: DataInput): Unit = {
    val shapeType = EndianUtils.swapInteger(dataInput.readInt())
    // all records share the same type or nullshape.
    require(this.shapeType == shapeType || shapeType == 0)
    val h = shapeType match {
      case 0 => new NullShapeReader()
      case 1 => new PointReader()
      case 3 => new PolyLineReader()
      case 5 => new PolygonReader()
      case 13 => new PolyLineZReader()
      case _ => ???
    }
    shape = h.readFields(dataInput)
  }

}
