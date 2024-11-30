package aoc2023

import InputParser
import Solver

// https://adventofcode.com/2023/day/17

class Day17(val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String = ""

    override fun solvePart2(): String = ""
}

fun main() {
    Day17("input/2023/17.test.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = null)
    Day17("input/2023/17.txt").run()
}
