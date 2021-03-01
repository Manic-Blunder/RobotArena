package brains

import com.komputation.cpu.network.network
import com.komputation.demos.lines.LinesData
import com.komputation.initialization.uniformInitialization
import com.komputation.instructions.continuation.activation.Activation
import com.komputation.instructions.continuation.activation.relu
import com.komputation.instructions.continuation.activation.sigmoid
import com.komputation.instructions.continuation.convolution.convolution
import com.komputation.instructions.continuation.dense.dense
import com.komputation.instructions.entry.input
import com.komputation.instructions.loss.crossEntropyLoss
import com.komputation.loss.printLoss
import com.komputation.optimization.stochasticGradientDescent
import java.util.*

fun main() {
    val numberOfRows = 3
    val numberOfColumns = 3
    val filterWidth = 3
    val filterHeight = 1
    val numberFilters = 6
    val outputDimension = 2
    val random = Random(1)
    val initialization = uniformInitialization(random, -0.05f, 0.05f)
    val optimization = stochasticGradientDescent(0.01f)
    val maxBatchSize = 1

    network(
        100,
        input(numberOfRows, numberOfColumns),
        convolution(numberFilters, filterWidth, filterHeight, initialization, optimization),
        sigmoid(),
        dense(outputDimension, Activation.Softmax, initialization, optimization)
    ).training(
        LinesData.inputs,
        LinesData.targets,
        30_000,
        crossEntropyLoss(),
        printLoss
    ).run()
}