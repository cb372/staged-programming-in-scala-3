package unless

import scala.quoted.*

def unlessImpl(pred: Expr[Boolean], f: Expr[Unit])(using Quotes): Expr[Unit] =
  '{
    if (!$pred) $f
  }

inline def unless(pred: Boolean)(inline f: Unit) = ${ unlessImpl('pred, 'f) }
