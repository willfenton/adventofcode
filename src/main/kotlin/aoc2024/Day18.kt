package aoc2024

import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/18

class Day18(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    override fun solvePart1(): String {
        // abc
        return ""
    }

    override fun solvePart2(): String {
        // abc
        return ""
    }
}

fun main() {
    Day18(
        inputFile(
            """
            abc
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = null, part2ExpectedSolution = null)

    Day18(downloadInput(2024, 18)).run()
}
