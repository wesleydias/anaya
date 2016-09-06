package anaya.catalyst

import anaya.Shape
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.catalyst.expressions.codegen.{CodeGenContext, GeneratedExpressionCode}
import org.apache.spark.sql.types.DataType

case class ShapeLiteral(shape: Shape) extends LeafExpression with MagellanExpression {

  private val serialized = serialize(shape)

  override def foldable: Boolean = true

  override def nullable: Boolean = false

  override val dataType: DataType = shape

  override def eval(input: InternalRow) = serialized

  override protected def genCode(ctx: CodeGenContext, ev: GeneratedExpressionCode): String = ???
}
