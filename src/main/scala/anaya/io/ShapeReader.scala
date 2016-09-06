package anaya.io

import java.io.DataInput

import scala.collection.mutable.ArrayBuffer

import org.apache.commons.io.EndianUtils

import anaya._

private[anaya] trait ShapeReader {

  def readFields(dataInput: DataInput): Shape

}

class NullShapeReader extends ShapeReader {

  override def readFields(dataInput: DataInput): Shape = ???

}

private[anaya] class PointReader extends ShapeReader {

  override def readFields(dataInput: DataInput): Shape = {
    val x = EndianUtils.swapDouble(dataInput.readDouble())
    val y = EndianUtils.swapDouble(dataInput.readDouble())
    Point(x, y)
  }

}

private[anaya] class PolygonReader extends ShapeReader {

  override def readFields(dataInput: DataInput): Shape = {
    val (indices, points) = extract(dataInput)
    Polygon(indices, points)
  }

  def extract(dataInput: DataInput): (Array[Int], Array[Point]) = {
    // extract bounding box.
    (0 until 4).foreach { _ => EndianUtils.swapDouble(dataInput.readDouble())}

    // numRings
    val numRings = EndianUtils.swapInteger(dataInput.readInt())
    val numPoints = EndianUtils.swapInteger(dataInput.readInt())

    val indices = Array.fill(numRings)(-1)

    def tryl2b(l: Integer): Int = {
      if ((0 <= l) && (l < numRings)) {
        l
      } else {
        EndianUtils.swapInteger(l)
      }
    }

    for (ring <- 0 until numRings) {
      val s = tryl2b(dataInput.readInt())
      indices(ring) = s
    }
    val points = ArrayBuffer[Point]()
    for (_ <- 0 until numPoints) {
      points.+= {
        val x = EndianUtils.swapDouble(dataInput.readDouble())
        val y = EndianUtils.swapDouble(dataInput.readDouble())
        Point(x, y)
      }
    }
    (indices, points.toArray)
  }
}

private[anaya] class PolyLineReader extends ShapeReader {

  def extract(dataInput: DataInput): (Array[Int], Array[Point]) = {
    // extract bounding box.
    (0 until 4).foreach { _ => EndianUtils.swapDouble(dataInput.readDouble())}

    // numRings
    val numRings = EndianUtils.swapInteger(dataInput.readInt())
    val numPoints = EndianUtils.swapInteger(dataInput.readInt())

    val indices = Array.fill(numRings)(-1)

    def tryl2b(l: Integer): Int = {
      if ((0 <= l) && (l < numRings)) {
        l
      } else {
        EndianUtils.swapInteger(l)
      }
    }

    for (ring <- 0 until numRings) {
      val s = tryl2b(dataInput.readInt())
      indices(ring) = s
    }
    val points = ArrayBuffer[Point]()
    for (_ <- 0 until numPoints) {
      points.+= {
        val x = EndianUtils.swapDouble(dataInput.readDouble())
        val y = EndianUtils.swapDouble(dataInput.readDouble())
        Point(x, y)
      }
    }
    (indices, points.toArray)
  }

  override def readFields(dataInput: DataInput): Shape = {
    val (indices, points) = extract(dataInput)
    PolyLine(indices, points)
  }
}

private[anaya] class PolyLineZReader extends PolyLineReader {

  override def readFields(dataInput: DataInput): Shape = {
    val (indices, points) = extract(dataInput)
    // throw away the Z and M values
    val size = points.length
    (0 until (4 + 2 * size)).foreach(_ => dataInput.readDouble())
    if(indices.size != points.size)
      PolyLine( new Array[Int](points.size), points)
    else
      PolyLine(indices, points)
  }
}
