package fuzzyMath

class Matrix(val elements: List<List<Double>>) {

    // matrix of m rows by n columns
    // [row][column]
    // this is confusing because in math its easier to be able to think of a matrix
    // as either rows of elements or columns of elements depending on context.
    // but here we have to just pick one and stick with it.

    // m is number of rows (height), n is number of columns (width). this is common matrix nomenclature
    val m: Int
    val n: Int

    init {
        m = elements.size
        n = elements[0].size
    }

    // this seems superfluous, but its so that grabbing rows/columns has consistent syntax
    fun row(i: Int) = elements[i]

    fun column(i: Int) = elements.map { row: List<Double> -> row[i] }
}

operator fun Matrix.times(other: Matrix): Matrix {
    if (n != other.m) {
        throw IllegalArgumentException("Matrices are not compatible sizes. The width of the first matrix must be equal to the height of the second matrix for a valid matrix multiplication")
    }

    return Matrix((List(m) { List(other.n) { 0.0 } }).mapIndexed { rowIndex: Int, row: List<Double> ->
        (0 until row.size).map { columnIndex: Int ->
            slam(this.row(rowIndex), other.column(columnIndex))
        }
    })
}

// multiply corresponding elements of two lists, and sum the results
private fun slam(a: List<Double>, b: List<Double>) = (0 until a.size).map{ a[it] * b[it]}.sum()