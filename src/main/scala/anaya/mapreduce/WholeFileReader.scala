package anaya.mapreduce

import org.apache.commons.io.IOUtils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FSDataInputStream, FileSystem}
import org.apache.hadoop.io.{NullWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.FileSplit
import org.apache.hadoop.mapreduce.{InputSplit, RecordReader, TaskAttemptContext}

class WholeFileReader extends RecordReader[NullWritable, Text] {

  private val key = NullWritable.get()
  private val value = new Text()
  private var split: FileSplit = _
  private var conf: Configuration = _
  private var done: Boolean = false

  override def getProgress: Float = ???

  override def nextKeyValue(): Boolean = {
    if (done){
      false
    } else {
      val len = split.getLength.toInt
      val result = new Array[Byte](len)
      val fs = FileSystem.get(conf)
      var is: FSDataInputStream = null
      try {
        is = fs.open(split.getPath)
        IOUtils.readFully(is, result)
        value.clear()
        value.set(result)
        done = true
        true
      } finally {
        if (is != null) {
          IOUtils.closeQuietly(is)
        }
      }
    }
  }

  override def getCurrentValue: Text = value

  override def initialize(inputSplit: InputSplit,
    taskAttemptContext: TaskAttemptContext): Unit = {
    this.split = inputSplit.asInstanceOf[FileSplit]
    this.conf = MapReduceUtils.getConfigurationFromContext(taskAttemptContext)
  }

  override def getCurrentKey: NullWritable = key

  override def close() {}
}
