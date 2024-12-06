package aoc2024

import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/10

class Day10(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    override fun solvePart1(): String {
        // x
        return ""
    }

    override fun solvePart2(): String {
        // x
        return ""
    }
}

fun main() {
    Day10(
        inputFile(
            """
            abc
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = null, part2ExpectedSolution = null)

    Day10(downloadInput(2024, 10)).run()
}
