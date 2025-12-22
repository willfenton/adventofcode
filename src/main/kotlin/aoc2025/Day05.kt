package aoc2025

import Solver
import downloadInput
import inputFile
import java.nio.file.Path
import kotlin.math.max
import kotlin.math.min

// https://adventofcode.com/2025/day/5

class Day05(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private var freshRanges = mutableSetOf<LongRange>()
    private var products = mutableListOf<Long>()

    init {
        for (line in input) {
            if (line.contains("-")) {
                val ints = line.split("-").map { it.toLong() }
                freshRanges.add(ints[0]..ints[1])
            }
            else if (line.isNotBlank()) {
                products.add(line.toLong())
            }
        }
    }

    private fun combineRanges(): Boolean {
        for (r1 in freshRanges) {
            for (r2 in freshRanges) {
                if (r1 == r2) {
                    continue
                }
                if (r1.first in r2 || r1.last in r2 || r2.first in r1 || r2.last in r1) {
                    // combine ranges
                    val combinedRange = min(r1.first, r2.first)..max(r1.last, r2.last)
                    freshRanges.remove(r1)
                    freshRanges.remove(r2)
                    freshRanges.add(combinedRange)
                    return true
                }
            }
        }
        return false
    }

    override fun solvePart1(): String = products.count { i -> freshRanges.any { range -> i in range } }.toString()

    override fun solvePart2(): String {
        while (combineRanges()) {
            continue
        }
        return freshRanges.sumOf { r -> (r.last - r.first) + 1 }.toString()
    }
}

fun main() {
    Day05(
        inputFile(
            """
            3-5
            10-14
            16-20
            12-18
            
            1
            5
            8
            11
            17
            32
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "3", part2ExpectedSolution = "14")

    Day05(downloadInput(2025, 5)).run(part1ExpectedSolution = "577", part2ExpectedSolution = "350513176552950")
}
