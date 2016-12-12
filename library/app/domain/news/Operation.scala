package domain.news

sealed trait Operation

object MasterOperation extends Operation

object ContentOperation extends Operation

