package aoc2021

import InputParser
import Solver
import java.math.BigInteger

// https://adventofcode.com/2021/day/6

class Day06Lanternfish(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)
    private val inputFish = input.single().split(",").map { it.toInt() }

    override fun solvePart1(): String = getFishCount(inputFish, 80).toString()

    override fun solvePart2(): String = getFishCount(inputFish, 256).toString()

    private fun getFishCount(initialState: List<Int>, days: Int): BigInteger {
        val fishByTimer = (0..8).associateWith { timer -> initialState.count { it == timer }.toBigInteger() }.toMutableMap()

        repeat(days) {
            val spawns = fishByTimer[0]!!

            for (i in 0..7) {
                fishByTimer[i] = fishByTimer[i + 1]!!
            }

            fishByTimer[6] = fishByTimer[6]!! + spawns
            fishByTimer[8] = spawns
        }

        return fishByTimer.values.reduce { acc, fish -> acc + fish }
    }
}

fun main() {
    Solver.run { Day06Lanternfish("input/2021/06.test.txt") }
    Solver.run { Day06Lanternfish("input/2021/06.txt") }
}
