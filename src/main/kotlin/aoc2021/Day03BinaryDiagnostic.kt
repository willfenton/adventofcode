package aoc2021

import InputParser
import Solver

// https://adventofcode.com/2021/day/3

class Day03BinaryDiagnostic(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)
    private val length = input.first().length

    override fun solvePart1(): String {
        val bitCountsByPosition =
            (0..<length).map { pos ->
                input
                    .map { line -> line[pos].digitToInt() }
                    .groupingBy { it }
                    .eachCount()
            }

        val gammaRate =
            bitCountsByPosition
                .map { countsAtPosition -> countsAtPosition.maxBy { it.value }.key }
                .joinToString("")
                .toUInt(2)
        val epsilonRate =
            bitCountsByPosition
                .map { countsAtPosition -> countsAtPosition.minBy { it.value }.key }
                .joinToString("")
                .toUInt(2)

        return (gammaRate * epsilonRate).toString()
    }

    override fun solvePart2(): String {
        var oxygenCandidates = input
        for (i in 0..<length) {
            oxygenCandidates = oxygenCandidates.filter { it[i] == mostCommonAtPosition(oxygenCandidates, i) }
            if (oxygenCandidates.size == 1) {
                break
            }
        }

        var co2Candidates = input
        for (i in 0..<length) {
            val mostCommon = mostCommonAtPosition(co2Candidates, i)
            val leastCommon = if (mostCommon == '1') '0' else '1'
            co2Candidates = co2Candidates.filter { it[i] == leastCommon }
            if (co2Candidates.size == 1) {
                break
            }
        }

        val oxygenGeneratorRating = oxygenCandidates.single().toUInt(2)
        val co2ScrubberRating = co2Candidates.single().toUInt(2)

        return (oxygenGeneratorRating * co2ScrubberRating).toString()
    }

    private fun mostCommonAtPosition(lines: List<String>, pos: Int): Char {
        val counts = lines.map { it[pos] }.groupingBy { it }.eachCount()
        return when {
            counts.containsKey('1') && !counts.containsKey('0') -> '1'
            counts.containsKey('0') && !counts.containsKey('1') -> '0'
            counts['1']!! > counts['0']!! -> '1'
            counts['0']!! > counts['1']!! -> '0'
            else -> '1'
        }
    }
}

fun main() {
    Solver.run { Day03BinaryDiagnostic("input/2021/03.test.txt") }
    Solver.run { Day03BinaryDiagnostic("input/2021/03.txt") }
}
