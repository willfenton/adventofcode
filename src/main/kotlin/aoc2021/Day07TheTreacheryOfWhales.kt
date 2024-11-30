package aoc2021

import InputParser
import Solver
import kotlin.math.abs

// https://adventofcode.com/2021/day/7

class Day07TheTreacheryOfWhales(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)
    private val positions = input.single().split(",").map { it.toInt() }
    private val min = positions.min()
    private val max = positions.max()

    override fun solvePart1(): String = (min..max)
        .minOf { position ->
            costForPosition(position) { a, b ->
                abs(a - b)
            }
        }.toString()

    override fun solvePart2(): String = (min..max)
        .minOf { position ->
            costForPosition(position) { a, b ->
                val dist = abs(a - b)
                (dist * (dist + 1)) / 2
            }
        }.toString()

    private fun costForPosition(targetPosition: Int, costFunction: (Int, Int) -> Int): Int = positions.sumOf { position ->
        costFunction(position, targetPosition)
    }
}

fun main() {
    Solver.run { Day07TheTreacheryOfWhales("input/2021/07.test.txt") }
    Solver.run { Day07TheTreacheryOfWhales("input/2021/07.txt") }
}
