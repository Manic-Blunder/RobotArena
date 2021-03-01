package fuzzyMath.shapes

import org.junit.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

internal class SegmentTest {
    @Test
    fun intersection() {
        val segA = Segment(1, 3, 2, 5)
        val segB = Segment(1, 5, 2, 3)
        val intersection = segA.intersection(segB)
        assertNotNull(intersection)
        assert(intersection.x == 1.5)
        assert(intersection.y == 4.0)
    }

    @Test
    fun intersection_whenOverlapping() {
        val segA = Segment(1, 3, 2, 5)
        val segB = Segment(1, 3, 2, 5)
        val intersection = segA.intersection(segB)
        assertNull(intersection)
    }

    @Test
    fun intersection_partialOverlap() {
        val segA = Segment(2, 2, 0, 0)
        val segB = Segment(1, 1, 3, 3)
        val intersection = segA.intersection(segB)
        assertNull(intersection)
    }

    @Test
    fun intersection_whenParallel() {
        val segA = Segment(1, 3, 2, 5)
        val segB = Segment(2, 4, 3, 6)
        val intersection = segA.intersection(segB)
        assertNull(intersection)
    }
}