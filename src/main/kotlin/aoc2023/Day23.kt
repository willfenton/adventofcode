package aoc2023

import InputParser
import Solver

// https://adventofcode.com/2023/day/23

class Day23(val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String = ""

    override fun solvePart2(): String = ""
}

fun main() {
    Day23("input/2023/23.test.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = null)
    Day23("input/2023/23.txt").run()
}
