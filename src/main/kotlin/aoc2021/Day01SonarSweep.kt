package aoc2021

import InputParser
import Solver

// https://adventofcode.com/2021/day/1

class Day01SonarSweep(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String {
        val depthMeasurements = input.map { line -> line.toInt() }

        return countIncreases(depthMeasurements).toString()
    }

    override fun solvePart2(): String {
        val depthMeasurements =
            input
                .map { line -> line.toInt() }
                .windowed(3)
                .map { trio -> trio.sum() }

        return countIncreases(depthMeasurements).toString()
    }

    private fun countIncreases(depthMeasurements: List<Int>): Int = depthMeasurements
        .windowed(2)
        .count { (prev, next) -> next > prev }
}

fun main() {
    Solver.run { Day01SonarSweep("input/2021/01.test.txt") }
    Solver.run { Day01SonarSweep("input/2021/01.txt") }
}
