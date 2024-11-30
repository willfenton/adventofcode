package aoc2023

import InputParser
import Solver

// https://adventofcode.com/2023/day/19

class Day19(val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String = ""

    override fun solvePart2(): String = ""
}

fun main() {
    Day19("input/2023/19.test.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = null)
    Day19("input/2023/19.txt").run()
}
