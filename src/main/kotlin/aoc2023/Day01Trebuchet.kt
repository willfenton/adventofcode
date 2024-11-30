package aoc2023

import InputParser
import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2023/day/1

val DIGITS_REGULAR = mapOf("1" to "1", "2" to "2", "3" to "3", "4" to "4", "5" to "5", "6" to "6", "7" to "7", "8" to "8", "9" to "9")
val DIGITS_SPELLED_OUT = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9"
)

class Day01Trebuchet(filePath: Path) : Solver {
    private val input = InputParser.parseLines(filePath.toString())

    override fun solvePart1(): String = solve(DIGITS_REGULAR)

    override fun solvePart2(): String = solve(DIGITS_REGULAR + DIGITS_SPELLED_OUT)

    private fun solve(digits: Map<String, String>): String = input
        .sumOf { line ->
            val firstDigit = digits[line.findAnyOf(digits.keys)?.second!!]!!
            val lastDigit = digits[line.findLastAnyOf(digits.keys)?.second!!]!!
            (firstDigit + lastDigit).toInt()
        }.toString()
}

fun main() {
    Day01Trebuchet(
        inputFile(
            """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "142")

    Day01Trebuchet(
        inputFile(
            """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
            """.trimIndent()
        )
    ).run(part2ExpectedSolution = "281", swallowExceptions = true)

    Day01Trebuchet(downloadInput(2023, 1)).run()
}
