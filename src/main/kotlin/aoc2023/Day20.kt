package aoc2023

import InputParser
import Solver

// https://adventofcode.com/2023/day/20

class Day20(val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String = ""

    override fun solvePart2(): String = ""
}

fun main() {
    Day20("input/2023/20.test.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = null)
    Day20("input/2023/20.txt").run()
}
