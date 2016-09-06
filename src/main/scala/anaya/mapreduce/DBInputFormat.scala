package anaya.mapreduce

import java.util

import scala.collection.JavaConversions.seqAsJavaList

import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.MapWritable
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, TaskAttemptContext}

import anaya.io.ShapeKey

private[anaya] class DBInputFormat extends FileInputFormat[ShapeKey, MapWritable] {

  override def createRecordReader(inputSplit: InputSplit,
      taskAttemptContext: TaskAttemptContext) = {
    new DBReader
  }

  override def isSplitable(context: JobContext, filename: Path): Boolean = false

  override def getSplits(job: JobContext): util.List[InputSplit] = {
    try {
      super.getSplits(job)
    }catch {
      case e: Exception => seqAsJavaList(List[InputSplit]())
    }
  }
}
