package aoc2023

import InputParser
import Solver

// https://adventofcode.com/2023/day/14

class Day14(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String {
        return ""
    }

    override fun solvePart2(): String {
        return ""
    }
}

fun main() {
    Day14("input/2023/14.test.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = null)
    Day14("input/2023/14.txt").run()
}
