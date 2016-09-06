package anaya.mapreduce

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.mapreduce.TaskAttemptContext

private[anaya] object MapReduceUtils {
  def getConfigurationFromContext(context: TaskAttemptContext): Configuration = {
    // Use reflection to get the Configuration. This is necessary because TaskAttemptContext
    // is a class in Hadoop 1.x and an interface in Hadoop 2.x.
    val method = context.getClass.getMethod("getConfiguration")
    method.invoke(context).asInstanceOf[Configuration]
  }
}
