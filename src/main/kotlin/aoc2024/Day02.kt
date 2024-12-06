package aoc2024

import Solver
import downloadInput
import inputFile
import java.nio.file.Path
import kotlin.math.abs

// https://adventofcode.com/2024/day/2

class Day02(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val report = input.map { line ->
        line.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
    }

    override fun solvePart1(): String = report.map { levels -> if (checkLevels(levels)) 1 else 0 }.sum().toString()

    override fun solvePart2(): String {
        var count = 0
        for (levels in report) {
            for (i in levels.indices) {
                val oneRemoved = levels.toMutableList()
                oneRemoved.removeAt(i)
                if (checkLevels(oneRemoved)) {
                    count += 1
                    break
                }
            }
        }
        return count.toString()
    }

    private fun checkLevels(levels: List<Int>): Boolean {
        if (levels.sorted() != levels && levels.sortedDescending() != levels) {
            return false
        }
        val differences = levels.windowed(2).map { l -> abs(l[0] - l[1]) }
        return differences.all { it in 1..3 }
    }
}

fun main() {
    Day02(
        inputFile(
            """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "2", part2ExpectedSolution = "4")

    Day02(downloadInput(2024, 2)).run()
}
