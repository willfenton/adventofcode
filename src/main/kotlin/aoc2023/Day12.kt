package aoc2023

import InputParser
import Solver

// https://adventofcode.com/2023/day/12

class Day12(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String {
        return ""
    }

    override fun solvePart2(): String {
        return ""
    }
}

fun main() {
    Day12("input/2023/12.test.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = null)
    Day12("input/2023/12.txt").run()
}
