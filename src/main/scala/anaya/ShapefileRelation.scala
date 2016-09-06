package anaya

import scala.collection.JavaConversions._

import com.google.common.base.Objects
import org.apache.hadoop.io.{MapWritable, Text}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext

import anaya.io._
import anaya.mapreduce._

/**
 * A Shapefile relation is the entry point for working with Shapefile formats.
 */
case class ShapeFileRelation(path: String)(@transient val sqlContext: SQLContext)
  extends SpatialRelation {

  protected override def _buildScan(): RDD[(Shape, Option[Map[String, String]])] = {

    val shapefileRdd = sqlContext.sparkContext.newAPIHadoopFile(
      path + "/*.shp",
      classOf[ShapeInputFormat],
      classOf[ShapeKey],
      classOf[ShapeWritable]
    )

    val dbaseRdd = sqlContext.sparkContext.newAPIHadoopFile(
      path + "/*.dbf",
      classOf[DBInputFormat],
      classOf[ShapeKey],
      classOf[MapWritable]
    )

    val dataRdd = shapefileRdd.map { case (k, v) =>
      ((k.getFileNamePrefix(), k.getRecordIndex()), v.shape)
    }

    val metadataRdd = dbaseRdd.map { case (k, v) =>
      val meta = v.entrySet().map { kv =>
        val k = kv.getKey.asInstanceOf[Text].toString
        val v = kv.getValue.asInstanceOf[Text].toString
        (k, v)
      }.toMap
      ((k.getFileNamePrefix(), k.getRecordIndex()), meta)
    }
    dataRdd.leftOuterJoin(metadataRdd).map(f => f._2)
  }

  override def hashCode(): Int = Objects.hashCode(path, schema)
}
