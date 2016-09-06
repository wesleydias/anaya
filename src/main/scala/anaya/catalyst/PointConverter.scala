package anaya.catalyst

import anaya._
import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.expressions.{Expression, BinaryExpression}
import org.apache.spark.sql.catalyst.expressions.codegen.{GeneratedExpressionCode, CodeGenContext, CodegenFallback}
import org.apache.spark.sql.types.DataType

/**
 * Convert x and y coordinates to a `Point`
 *
 * @param left
 * @param right
 */
case class PointConverter(override val left: Expression,
    override val right: Expression) extends BinaryExpression {


  override def nullable: Boolean = false

  override val dataType = new PointUDT

  override def nullSafeEval(leftEval: Any, rightEval: Any): Any = {
    val x = leftEval.asInstanceOf[Double]
    val y = rightEval.asInstanceOf[Double]
    dataType.serialize(x, y)
  }

  override def genCode(ctx: CodeGenContext, ev: GeneratedExpressionCode): String = {
    ctx.addMutableState(classOf[PointUDT].getName, "pointUDT", "pointUDT = new anaya.PointUDT();")
    defineCodeGen(ctx, ev, (c1, c2) => s"pointUDT.serialize($c1, $c2)")
  }
}
