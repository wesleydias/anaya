package org.apache.spark.sql.anaya

import anaya._
import anaya.catalyst._
import org.apache.spark.sql.Column
import org.apache.spark.sql.catalyst.expressions._
import org.apache.spark.sql.functions._

package object dsl {
  trait ImplicitOperators {

    def expr: Expression

    def within(other: Expression): Expression = Within(expr, other)

    def within(other: Column): Column = Column(Within(expr, other.expr))

    def intersects(other: Expression): Expression = Intersects(expr, other)

    def intersects(other: Shape): Column = Column(Intersects(expr, ShapeLiteral(other)))

    def >?(other: Expression): Expression = Within(other, expr)

    def >?(other: Column): Column = Column(Within(other.expr, expr))

    def apply(other: Any): Expression = GetMapValue(expr, lit(other).expr)

    def apply(other: Expression): Expression = GetMapValue(expr, other)

    def transform(fn: Point => Point) = Transformer(expr, fn)

  }

  trait ExpressionConversions {

    implicit class DslExpression(e: Expression) extends ImplicitOperators {
      def expr: Expression = e
    }

    implicit class DslColumn(c: Column) {
      def col: Column = c

      def within(other: Column): Column = Column(Within(col.expr, other.expr))

      def intersects(other: Column): Column = Column(Intersects(c.expr, other.expr))

      def intersects(other: Shape): Column = Column(Intersects(c.expr, ShapeLiteral(other)))

      def >?(other: Column): Column = Column(Within(other.expr, col.expr))

      def >?(other: Expression): Column = Column(Within(other, col.expr))

      def apply(other: Any): Column = Column(GetMapValue(col.expr, lit(other).expr))

      def apply(other: Expression): Column = Column(GetMapValue(col.expr, other))

      def transform(fn: Point => Point): Column = Column(Transformer(c.expr, fn))

    }

    implicit def point(x: Expression, y: Expression) = PointConverter(x, y)

    implicit def point(x: Column, y: Column) = Column(PointConverter(x.expr, y.expr))

    implicit def shape(x: Shape) = Column(ShapeLiteral(x))

  }


  object expressions extends ExpressionConversions  // scalastyle:ignore

}

