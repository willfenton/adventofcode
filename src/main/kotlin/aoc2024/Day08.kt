package aoc2024

import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/8

class Day08(file: Path) : Solver {
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
    Day08(
        inputFile(
            """
            abc
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = null, part2ExpectedSolution = null)

    Day08(downloadInput(2024, 8)).run()
}