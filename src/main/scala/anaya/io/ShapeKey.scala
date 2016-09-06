package anaya.io

import java.io.{DataOutput, DataInput}

import org.apache.hadoop.io
import org.apache.hadoop.io.{Text, LongWritable, Writable}

private[anaya] class ShapeKey extends Writable {

  var fileNamePrefix: Text = new Text()
  var recordIndex: LongWritable = new io.LongWritable()

  def setFileNamePrefix(fileNamePrefix: String) = {
    val f = fileNamePrefix.getBytes()
    this.fileNamePrefix.clear()
    this.fileNamePrefix.append(f, 0, f.length)
  }

  def setRecordIndex(recordIndex: Long) = {
    this.recordIndex.set(recordIndex)
  }

  def getRecordIndex(): Long = recordIndex.get()

  def getFileNamePrefix(): String = fileNamePrefix.toString()

  override def write(dataOutput: DataOutput): Unit = ???

  override def readFields(dataInput: DataInput): Unit = ???

}
