package anaya

import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.sources.{BaseRelation, SchemaRelationProvider, RelationProvider}

/**
 * Provides access to Shapefile Formatted data from pure SQL statements.
 */
class DefaultSource extends RelationProvider
  with SchemaRelationProvider {

  override def createRelation(sqlContext: SQLContext,
    parameters: Map[String, String]): BaseRelation = createRelation(sqlContext, parameters, null)

  override def createRelation(sqlContext: SQLContext,
    parameters: Map[String, String], schema: StructType): BaseRelation = {
    val path = parameters.getOrElse("path", sys.error("'path' must be specified for Shapefiles."))
    val t = parameters.getOrElse("type", "shapefile")
    t match {
      case "shapefile" => new ShapeFileRelation(path)(sqlContext)
      case _ => ???
    }
  }

}
