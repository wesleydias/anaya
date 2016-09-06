package anaya.mapreduce

import org.apache.hadoop.fs.Path
import org.apache.hadoop.mapreduce.lib.input._
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, TaskAttemptContext}

import anaya.io.{ShapeWritable, ShapeKey}

private[anaya] class ShapeInputFormat extends FileInputFormat[ShapeKey, ShapeWritable] {

  override def createRecordReader(inputSplit: InputSplit,
    taskAttemptContext: TaskAttemptContext) = {
    new ShapefileReader
  }

  // TODO: Use DBIndex to figure out how to efficiently split files.
  override def isSplitable(context: JobContext, filename: Path): Boolean = false

}
