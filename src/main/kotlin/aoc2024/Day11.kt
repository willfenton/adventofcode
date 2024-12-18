package aoc2024

import Solver
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/11

private val splits = mutableMapOf<Long, List<Long>>()

class Day11(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private fun split(stone: Long): List<Long> {
        if (splits.containsKey(stone)) return splits[stone]!!

        val stoneString = stone.toString()
        val split = if (stone == 0L) {
            listOf(1L)
        } else if (stoneString.length % 2 == 0) {
            val left = stoneString.take(stoneString.length / 2)
            val right = stoneString.takeLast(stoneString.length / 2)
            listOf(left.toLong(), right.toLong())
        } else {
            listOf(stone * 2024)
        }
        splits[stone] = split
        return split
    }

    override fun solvePart1(): String {
        var stones = input
            .single()
            .split(" ")
            .map { it.toLong() }
            .toMutableList()

        repeat(25) {
            val newStones = mutableListOf<Long>()
            for (stone in stones) {
                val stoneString = stone.toString()
                if (stone == 0L) {
                    newStones.add(1)
                } else if (stoneString.length % 2 == 0) {
                    val left = stoneString.take(stoneString.length / 2)
                    val right = stoneString.takeLast(stoneString.length / 2)
                    newStones.add(left.toLong())
                    newStones.add(right.toLong())
                } else {
                    newStones.add(stone * 2024)
                }
            }
            stones = newStones
        }

        return stones.size.toString()
    }

    override fun solvePart2(): String {
        val stonesList = input
            .single()
            .split(" ")
            .map { it.toLong() }
            .toMutableList()

        var stoneMap = stonesList
            .groupBy { it }
            .map { it.key to it.value.size.toLong() }
            .toMap()
            .toMutableMap()

        repeat(75) {
            val newStoneMap = stoneMap.toList().toMap().toMutableMap()
            for ((stone, count) in stoneMap.entries) {
                for (split in split(stone)) {
                    val existing = newStoneMap[split] ?: 0L
                    newStoneMap[split] = existing + count
                }
                val existing = newStoneMap[stone] ?: 0L
                newStoneMap[stone] = existing - count
            }
            stoneMap = newStoneMap
        }

        return stoneMap.values.sum().toString()
    }
}

fun main() {
    Day11(
        inputFile(
            """
            125 17
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "55312", part2ExpectedSolution = null)

    Day11(
        inputFile(
            """
            4 4841539 66 5279 49207 134 609568 0
            """.trimIndent()
        )
    ).run()
}
