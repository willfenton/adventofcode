package aoc2023

import InputParser
import Solver

// https://adventofcode.com/2023/day/16

class Day16(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String {
        return ""
    }

    override fun solvePart2(): String {
        return ""
    }
}

fun main() {
    Day16("input/2023/16.test.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = null)
    Day16("input/2023/16.txt").run()
}
