import java.lang.Exception
import kotlin.time.measureTimedValue

interface Solver {
    fun solvePart1(): String

    fun solvePart2(): String

    fun run(part1ExpectedSolution: String? = null, part2ExpectedSolution: String? = null, swallowExceptions: Boolean = false) {
        val (solution1, duration1) = measureTimedValue {
            try {
                solvePart1()
            } catch (e: Exception) {
                if (!swallowExceptions) throw e else "Swallowed exception"
            }
        }
        println("Part 1: \"${solution1}\" ($duration1)")
        if (part1ExpectedSolution != null) {
            assert(solution1 == part1ExpectedSolution)
            println("Test passed")
        } else {
            println("No test case")
        }

        val (solution2, duration2) =
            measureTimedValue {
                try {
                    solvePart2()
                } catch (e: Exception) {
                    if (!swallowExceptions) throw e else "Swallowed exception"
                }
            }
        println("Part 2: \"${solution2}\" ($duration2)")
        if (part2ExpectedSolution != null) {
            assert(solution2 == part2ExpectedSolution)
            println("Test passed")
        } else {
            println("No test case")
        }

        println()
    }
}
