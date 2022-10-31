package brains.neat

class WeightedRandomSelector<T> {
    private val objects = arrayListOf<T>()
    private val scores = arrayListOf<Double>()
    private val totalScore
        get() = scores.sum()

    fun add(it: T, score: Double) {
        objects.add(it)
        scores.add(score)
    }

    fun select(): T {
        val value = Math.random() * totalScore
        var count = 0.0
        (0 until objects.size).forEach { i ->
            count += scores[i]
            if ( count > value ) {
//                println("returning $i out of ${objects.size}")
                return objects[i]
            }
        }
        return objects.first()
    }
}