package aoc2022

import InputParser
import Solver

// https://adventofcode.com/2022/day/22

class Day22(val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String = ""

    override fun solvePart2(): String = ""
}

fun main() {
    Day22("input/2022/22.test.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = null)
    Day22("input/2022/22.txt").run()
}
