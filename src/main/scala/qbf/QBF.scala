package qbf

enum QBF:
  case Var(name: String)
  case And(a: QBF, b: QBF)
  case Or(a: QBF, b: QBF)
  case Not(a: QBF)
  case Implies(ante: QBF, cons: QBF)
  case Forall(name: String, a: QBF)
  case Exists(name: String, a: QBF)
