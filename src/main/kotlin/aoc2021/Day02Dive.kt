package aoc2021

import InputParser
import Solver

// https://adventofcode.com/2021/day/2

class Day02Dive(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String {
        var horizontalPosition = 0
        var depth = 0

        input
            .map { line -> line.split(" ") }
            .forEach { (direction, valueString) ->
                val value = valueString.toInt()
                when (direction) {
                    "forward" -> horizontalPosition += value
                    "up" -> depth -= value
                    "down" -> depth += value
                }
            }

        return (horizontalPosition * depth).toString()
    }

    override fun solvePart2(): String {
        var horizontalPosition = 0
        var depth = 0
        var aim = 0

        input
            .map { line -> line.split(" ") }
            .forEach { (direction, valueString) ->
                val value = valueString.toInt()
                when (direction) {
                    "forward" -> {
                        horizontalPosition += value
                        depth += aim * value
                    }
                    "up" -> aim -= value
                    "down" -> aim += value
                }
            }

        return (horizontalPosition * depth).toString()
    }
}

fun main() {
    Solver.run { Day02Dive("input/2021/02.test.txt") }
    Solver.run { Day02Dive("input/2021/02.txt") }
}
