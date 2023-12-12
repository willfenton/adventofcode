package aoc2023

import InputParser
import Solver

// https://adventofcode.com/2023/day/15

class Day15(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String {
        return ""
    }

    override fun solvePart2(): String {
        return ""
    }
}

fun main() {
    Day15("input/2023/15.test.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = null)
    Day15("input/2023/15.txt").run()
}
