package aoc2025

import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2025/day/2

class Day02(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val ranges = input.single().split(",").map {
        val split = it.split("-")
        Pair(split[0], split[1])
    }

    override fun solvePart1(): String {
        var invalidSum = 0L
        for ((a, b) in ranges) {
            for (n in a.toLong()..b.toLong()) {
                val s = n.toString()
                if (s.length % 2 != 0) {
                    continue
                }
                if (s.take(s.length / 2) == s.takeLast(s.length / 2)) {
                    invalidSum += n
                }
            }
        }
        return invalidSum.toString()
    }

    override fun solvePart2(): String {
        var invalidSum = 0L
        for ((a, b) in ranges) {
            for (n in a.toLong()..b.toLong()) {
                val s = n.toString()
                for (i in 1..<s.length) {
                    if (s.length % i != 0) {
                        continue
                    }
                    val chunks = s.chunked(i)
                    if (chunks.toSet().size == 1) {
                        invalidSum += n
                        break
                    }
                }
            }
        }
        return invalidSum.toString()
    }
}

fun main() {
    Day02(
        inputFile(
            """
            11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "1227775554", part2ExpectedSolution = "4174379265")

    Day02(downloadInput(2025, 2)).run(part1ExpectedSolution = "29940924880", part2ExpectedSolution = "48631958998")
}
