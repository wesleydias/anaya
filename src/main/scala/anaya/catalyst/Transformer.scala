package anaya.catalyst

import anaya._
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.{Expression, UnaryExpression}
import org.apache.spark.sql.catalyst.expressions.codegen.{GeneratedExpressionCode, CodeGenContext, CodegenFallback}
import org.apache.spark.sql.types.DataType

case class Transformer(
    override val child: Expression,
    fn: Point => Point)
  extends UnaryExpression with CodegenFallback with MagellanExpression {

  protected override def nullSafeEval(input: Any): Any = {
    val shape = newInstance(input.asInstanceOf[InternalRow])
    serialize(shape.transform(fn))
  }

  override def nullable: Boolean = child.nullable

  override def dataType: DataType = child.dataType

}
