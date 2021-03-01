package fuzzyMath

import org.junit.jupiter.api.Test

internal class MatrixTest {

    @Test
    fun getM() {
    }

    @Test
    fun getN() {
    }

    @Test
    fun row() {
    }

    @Test
    fun column() {
    }

    @Test
    fun getElements() {
    }

    @Test
    fun multiply() {
        val matrixA = Matrix(listOf(
            listOf(2.0, 4.0, 8.0),
            listOf(3.0, 5.0, 9.0),
        ))
        val matrixB = Matrix(listOf(
            listOf(1.0, 2.0, 4.0),
            listOf(1.5, 2.5, 4.5),
            listOf(2.0, 3.0, 5.0),
        ))
        val matrixC = Matrix(listOf(
            listOf(24.0, 38.0, 66.0),
            listOf(28.5, 45.5, 79.5),
        ))
        val result = matrixA * matrixB
        assert((result).elements == matrixC.elements)
    }
}