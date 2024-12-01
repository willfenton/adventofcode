package aoc2024

import Solver
import downloadInput
import inputFile
import java.nio.file.Path
import kotlin.math.abs

// https://adventofcode.com/2024/day/1

class Day01(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val pairs = input.map { line ->
        line.split(" ").filter { it.isNotBlank() }
    }
    private val left = pairs.map { it[0].toInt() }.sorted()
    private val right = pairs.map { it[1].toInt() }.sorted()

    override fun solvePart1(): String = left.indices.sumOf { i -> abs(left[i] - right[i]) }.toString()

    override fun solvePart2(): String = left.sumOf { num -> num * right.count { it == num } }.toString()
}

fun main() {
    Day01(
        inputFile(
            """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "11", part2ExpectedSolution = "31")

    Day01(downloadInput(2024, 1)).run()
}
