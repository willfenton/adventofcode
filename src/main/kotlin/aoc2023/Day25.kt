package aoc2023

import InputParser
import Solver

// https://adventofcode.com/2023/day/25

class Day25(val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String = ""

    override fun solvePart2(): String = ""
}

fun main() {
    Day25("input/2023/25.test.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = null)
    Day25("input/2023/25.txt").run()
}
