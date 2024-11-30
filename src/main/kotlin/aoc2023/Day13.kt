package aoc2023

import InputParser
import Solver
import kotlin.math.min

// https://adventofcode.com/2023/day/13

class Day13(val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)
    private val patterns = input.joinToString("\n").split("\n\n").map { it.lines() }

    override fun solvePart1(): String = patterns.sumOf { pattern -> patternValues(pattern).sum() }.toString()

    override fun solvePart2(): String = patterns
        .sumOf { pattern ->
            val originalValues = patternValues(pattern).toSet()
            val xyPairs = pattern[0]
                .indices
                .map { x ->
                    pattern.indices.map { y ->
                        Pair(x, y)
                    }
                }.flatten()
            val unsmudgedPatterns = xyPairs.map { (x, y) -> unsmudgePattern(pattern, x, y) }
            val values = unsmudgedPatterns.map { unsmudged -> patternValues(unsmudged) }.flatten()
            values.toSet().minus(originalValues).sum()
        }.toString()

    private fun patternValues(pattern: List<String>): List<Long> {
        val rotatedPattern = rotatePattern(pattern)

        val horizontalMirrorIndices = getMirrorIndices(pattern)
        val verticalMirrorIndices = getMirrorIndices(rotatedPattern)

        return horizontalMirrorIndices.map { it * 100 } + verticalMirrorIndices
    }

    // checks for horizontal mirror
    // null if no mirror
    private fun getMirrorIndices(pattern: List<String>, ignoreIndex: Int? = null): List<Long> {
        val mirrorIndices = mutableListOf<Long>()
        for (i in 1..<pattern.size) {
            val minRows = min(i, pattern.size - i)
            val rowsAbove = pattern.take(i).reversed().take(minRows)
            val rowsBelow = pattern.drop(i).take(minRows)
            if (rowsAbove == rowsBelow) {
                mirrorIndices.add(i.toLong())
            }
        }
        return mirrorIndices
    }

    private fun rotatePattern(pattern: List<String>): List<String> = pattern[0].indices.map { i ->
        pattern.map { line -> line[i] }.joinToString("")
    }

    private fun unsmudgePattern(pattern: List<String>, x: Int, y: Int): List<String> {
        val newChar = when (pattern[y][x]) {
            '#' -> "."
            '.' -> "#"
            else -> throw Exception()
        }
        val unsmudged = pattern.toMutableList()
        unsmudged[y] = unsmudged[y].replaceRange(x, x + 1, newChar)
        return unsmudged
    }
}

fun main() {
    Day13("input/2023/13.test.txt").run(part1ExpectedSolution = "405", part2ExpectedSolution = "400")
    Day13("input/2023/13.txt").run(part1ExpectedSolution = "33975")
}
