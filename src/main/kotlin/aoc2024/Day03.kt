package aoc2024

import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/3

class Day03(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val mulRegex = Regex("mul\\((\\d+),(\\d+)\\)")
    private val doRegex = Regex("do\\(\\)")
    private val dontRegex = Regex("don't\\(\\)")

    override fun solvePart1(): String {
        var count = 0
        for (line in input) {
            val matches = mulRegex.findAll(line)
            for (match in matches) {
                val x = match.groupValues[1].toInt()
                val y = match.groupValues[2].toInt()
                count += x * y
            }
        }
        return count.toString()
    }

    enum class MatchType {
        MUL,
        DO,
        DONT
    }

    data class Match(val matchType: MatchType, val match: MatchResult)

    override fun solvePart2(): String {
        var count = 0
        var enabled = true
        for (line in input) {
            val allMatches: MutableList<Match> = mutableListOf()

            for (match in mulRegex.findAll(line)) {
                allMatches.add(Match(MatchType.MUL, match))
            }
            for (match in doRegex.findAll(line)) {
                allMatches.add(Match(MatchType.DO, match))
            }
            for (match in dontRegex.findAll(line)) {
                allMatches.add(Match(MatchType.DONT, match))
            }

            allMatches.sortBy { it.match.range.first }

            for (match in allMatches) {
                println(match)
            }

            for (match in allMatches) {
                when (match.matchType) {
                    MatchType.MUL -> {
                        val x = match.match.groupValues[1].toInt()
                        val y = match.match.groupValues[2].toInt()
                        if (enabled) count += x * y
                    }
                    MatchType.DO -> enabled = true
                    MatchType.DONT -> enabled = false
                }
            }
        }
        return count.toString()
    }
}

fun main() {
    Day03(
        inputFile(
            """
            xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "161", part2ExpectedSolution = null)

    Day03(
        inputFile(
            """
            xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = null, part2ExpectedSolution = "48")

    Day03(downloadInput(2024, 3)).run()
}
