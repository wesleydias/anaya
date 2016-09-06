package anaya.mapreduce

import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{NullWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, TaskAttemptContext}

class WholeFileInputFormat extends FileInputFormat[NullWritable, Text] {

  override def createRecordReader(inputSplit: InputSplit,
    taskAttemptContext: TaskAttemptContext) = {
    new WholeFileReader()
  }

  override def isSplitable(context: JobContext, filename: Path): Boolean = false

}
