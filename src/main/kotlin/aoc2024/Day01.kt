package aoc2024

import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/1

class Day01(filePath: Path) : Solver {
    private val input = InputParser.parseLines(filePath.toString())

    override fun solvePart1(): String = ""

    override fun solvePart2(): String = ""
}

fun main() {
    Day01(
        inputFile(
            """
            abc
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = null, part2ExpectedSolution = null)

    Day01(downloadInput(2024, 1)).run()
}
