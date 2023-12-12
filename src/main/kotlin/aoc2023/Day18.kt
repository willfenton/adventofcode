package aoc2023

import InputParser
import Solver

// https://adventofcode.com/2023/day/18

class Day18(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String {
        return ""
    }

    override fun solvePart2(): String {
        return ""
    }
}

fun main() {
    Day18("input/2023/18.test.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = null)
    Day18("input/2023/18.txt").run()
}
