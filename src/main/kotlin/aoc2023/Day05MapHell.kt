package aoc2023

import InputParser
import Solver
import kotlin.math.max
import kotlin.math.min

// https://adventofcode.com/2023/day/5

data class RangeMap(val sourceRange: LongRange, val destinationRange: LongRange) {
    fun map(n: Long): Long {
        return (n - sourceRange.first) + (destinationRange.first)
    }

    fun mapRange(range: LongRange): LongRange? {
        if (range.first > sourceRange.last) return null
        if (range.last < sourceRange.first) return null
        val sourceRangeIntersection = max(range.first, sourceRange.first)..min(range.last, sourceRange.last)
        return map(sourceRangeIntersection.first)..map(sourceRangeIntersection.last)
    }
}

fun List<RangeMap>.map(n: Long): Long {
    for (rangeMap in this) {
        if (rangeMap.sourceRange.contains(n)) return rangeMap.map(n)
    }
    return n
}

fun List<RangeMap>.mapRange(ranges: List<LongRange>): List<LongRange> {
    val unmappedRanges: MutableList<LongRange> = ranges.toMutableList()
    val mappedRanges: MutableList<LongRange> = mutableListOf()
    val unmappableRanges: MutableList<LongRange> = mutableListOf()

    while (unmappedRanges.isNotEmpty()) {
        val rangeToMap = unmappedRanges.removeFirst()
        var mapped = false

        for (rangeMap in this) {
            val mappedRange = rangeMap.mapRange(rangeToMap) // don't @ me
            if (mappedRange != null) {
                mappedRanges.add(mappedRange)
                if (rangeToMap.first < rangeMap.sourceRange.first) {
                    unmappedRanges.add(rangeToMap.first..<rangeMap.sourceRange.first)
                }
                if (rangeToMap.last > rangeMap.sourceRange.last) {
                    unmappedRanges.add(rangeMap.sourceRange.last + 1..rangeToMap.last)
                }
                mapped = true
                break
            }
        }

        if (!mapped) unmappableRanges.add(rangeToMap)
    }

    return mappedRanges + unmappableRanges
}

class Day05MapHell(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)
    private val sections: List<List<RangeMap>> = parseSections()

    init {
        parseSections()
    }

    override fun solvePart1(): String {
        val seeds = input[0]
            .substringAfter("seeds: ")
            .split(" ")
            .filter { it.isNotBlank() }
            .map { it.trim().toLong() }

        val locations = seeds.map { seed ->
            var currentValue = seed
            for (section in sections) {
                currentValue = section.map(currentValue)
            }
            currentValue
        }

        return locations.min().toString()
    }

    override fun solvePart2(): String {
        val seedRanges: List<LongRange> = input[0]
            .substringAfter("seeds: ")
            .split(" ")
            .filter { it.isNotBlank() }
            .map { it.trim().toLong() }
            .chunked(2)
            .map { (rangeStart, rangeLength) -> rangeStart..<rangeStart + rangeLength }

        var currentRanges = seedRanges
        for (section in sections) {
            currentRanges = section.mapRange(currentRanges)
        }

        return currentRanges.minOf { range -> range.first }.toString()
    }

    private fun parseSections(): List<List<RangeMap>> = input
        .drop(2)
        .joinToString("|")
        .split("||")
        .map { section -> section
            .substringAfter("|")
            .split("|")
        }
        .map { section -> parseSection(section) }

    private fun parseSection(map: List<String>): List<RangeMap> = map.map { line ->
        val (destRangeStart, sourceRangeStart, rangeLength) = line
            .split(" ")
            .filter { it.isNotBlank() }
            .map { it.trim().toLong() }
        RangeMap(
            sourceRange = sourceRangeStart..<sourceRangeStart + rangeLength,
            destinationRange = destRangeStart..<destRangeStart + rangeLength
        )
    }
}

fun main() {
    Day05MapHell("input/2023/05.test.txt").run(part1ExpectedSolution = "35", part2ExpectedSolution = "46")
    Day05MapHell("input/2023/05.txt").run(part1ExpectedSolution = "379811651")
}
