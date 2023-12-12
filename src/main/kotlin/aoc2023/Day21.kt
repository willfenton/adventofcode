package aoc2023

import InputParser
import Solver

// https://adventofcode.com/2023/day/21

class Day21(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String {
        return ""
    }

    override fun solvePart2(): String {
        return ""
    }
}

fun main() {
    Day21("input/2023/21.test.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = null)
    Day21("input/2023/21.txt").run()
}
