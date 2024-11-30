package aoc2021

import InputParser
import Solver

// https://adventofcode.com/2021/day/13

class Day13(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String = ""

    override fun solvePart2(): String = ""
}

fun main() {
    Solver.run { Day13("input/2021/13.test.txt") }
    Solver.run { Day13("input/2021/13.txt") }
}
