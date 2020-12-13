# 2SAT
Papadimitriou
The original algorithm implementation has problem when n is larger than 4000.
The solution is to apply domain knowledge. In this case, most of the clauses are redundant, so we can remove most of the clauses first. By doing the reduction, we can significantly reduce the problem size.
