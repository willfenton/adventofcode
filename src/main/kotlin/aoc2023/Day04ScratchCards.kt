package aoc2023

import InputParser
import Solver
import kotlin.math.min
import kotlin.math.pow

// https://adventofcode.com/2023/day/4

data class ScratchCard(val winningNumbers: Set<String>, val numbers: List<String>) {
    val wins: Int = numbers.count { number -> winningNumbers.contains(number) }
}

class Day04ScratchCards(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)
    private val scratchCards = parseScratchCards()

    override fun solvePart1(): String = scratchCards.sumOf { scratchCard ->
        (2.0).pow(scratchCard.wins - 1).toInt()
    }.toString()

    override fun solvePart2(): String {
        val cardCounts: MutableMap<Int, Int> = scratchCards.indices.associateWith { 1 }.toMutableMap()
        scratchCards.forEachIndexed { i, scratchCard ->
            val cardCount = cardCounts[i]!!
            for (j in (i + 1)..<min(i + 1 + scratchCard.wins, scratchCards.size)) {
                cardCounts[j] = cardCounts[j]!! + cardCount
            }
        }
        return cardCounts.values.sum().toString()
    }

    private fun parseScratchCards(): List<ScratchCard> = input.map { line -> ScratchCard(
        winningNumbers = line
            .substringAfter(": ")
            .substringBefore(" |")
            .split(" ")
            .filter { it.isNotEmpty() }
            .map { it.trim() }
            .toSet(),
        numbers = line
            .substringAfter("| ")
            .split(" ")
            .filter { it.isNotEmpty() }
            .map { it.trim() }
    )}
}

fun main() {
    Day04ScratchCards("input/2023/04.test.txt").run(part1ExpectedSolution = "13", part2ExpectedSolution = "30")
    Day04ScratchCards("input/2023/04.txt").run()
}
