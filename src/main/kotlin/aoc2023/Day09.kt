package aoc2023

import InputParser
import Solver

// https://adventofcode.com/2023/day/9

class Day09(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)
    private val sequences = input.map { line -> line
        .split(Regex("\\s+"))
        .map { it.toInt() }
    }

    override fun solvePart1() = sequences.sumOf { nextValueInSequence(it) }.toString()

    override fun solvePart2() = sequences.sumOf { nextValueInSequence(it.reversed()) }.toString()

    private fun nextValueInSequence(sequence: List<Int>): Int {
        if (sequence.all { it == 0 }) { return 0 }
        val differences = sequence.windowed(2).map { (l, r) -> r - l }
        return sequence.last() + nextValueInSequence(differences)
    }
}

fun main() {
    Day09("input/2023/09.test.txt").run(part1ExpectedSolution = "114", part2ExpectedSolution = null)
    Day09("input/2023/09.txt").run()
}
