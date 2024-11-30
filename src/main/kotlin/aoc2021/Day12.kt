package aoc2021

import InputParser
import Solver

// https://adventofcode.com/2021/day/12

class Day12(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String = ""

    override fun solvePart2(): String = ""
}

fun main() {
    Solver.run { Day12("input/2021/12.test.txt") }
    Solver.run { Day12("input/2021/12.txt") }
}
