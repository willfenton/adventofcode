package aoc2023

import InputParser
import Solver

// https://adventofcode.com/2023/day/13

class Day13(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String {
        return ""
    }

    override fun solvePart2(): String {
        return ""
    }
}

fun main() {
    Day13("input/2023/13.test.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = null)
    Day13("input/2023/13.txt").run()
}
