package aoc2025

import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2025/day/1

enum class Day01Direction {
    LEFT,
    RIGHT
}

class Day01(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val pairs = input.map { line ->
        if (line.startsWith("L")) {
            Pair(Day01Direction.LEFT, line.substringAfter("L").toInt())
        } else {
            Pair(Day01Direction.RIGHT, line.substringAfter("R").toInt())
        }
    }

    override fun solvePart1(): String {
        var dial = 50
        var zeroCount = 0
        for ((direction, amount) in pairs) {
            dial = when (direction) {
                Day01Direction.LEFT -> (dial - amount) % 100
                Day01Direction.RIGHT -> (dial + amount) % 100
            }
            if (dial == 0) {
                zeroCount += 1
            }
        }
        return zeroCount.toString()
    }

    override fun solvePart2(): String {
        var dial = 50
        var zeroCount = 0

        for ((direction, amount) in pairs) {
            repeat(amount) {
                dial = when (direction) {
                    Day01Direction.LEFT -> (dial - 1) % 100
                    Day01Direction.RIGHT -> (dial + 1) % 100
                }
                if (dial == 0) {
                    zeroCount += 1
                }
            }
        }

        return zeroCount.toString()
    }
}

fun main() {
    Day01(
        inputFile(
            """
            L68
            L30
            R48
            L5
            R60
            L55
            L1
            L99
            R14
            L82
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "3", part2ExpectedSolution = "6")

    Day01(downloadInput(2025, 1)).run(part1ExpectedSolution = "1076", part2ExpectedSolution = "6379")
}
