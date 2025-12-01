package aoc2025

import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2025/day/12

class Day12(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    override fun solvePart1(): String {
        return "1"
    }

    override fun solvePart2(): String {
        return "1"
    }
}

fun main() {
    Day12(
        inputFile(
            """
            input
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "", part2ExpectedSolution = null)

    Day12(downloadInput(2025, 12)).run(part1ExpectedSolution = null, part2ExpectedSolution = null)
}
