package aoc2023

import InputParser
import Solver

// https://adventofcode.com/2023/day/7

enum class Type {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIR,
    ONE_PAIR,
    HIGH_CARD
}

data class Hand(val cards: String, val bid: Int) {
    val cardList: List<String> = cards.split("").filter { it.isNotBlank() }

    val typeWithWildcards: Type get() {
        val cardListWithoutJokers = cardList.filter { it != "J" }
        val numJokers = cardList.count { it == "J" }

        var jokerPermutations: List<List<String>> = listOf(cardListWithoutJokers)
        val allCardsExceptJokers = listOf("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2")
        repeat(numJokers) {
            val newJokerPermutations = jokerPermutations.map { cardList ->
                allCardsExceptJokers.map { card ->
                    cardList + card
                }
            }.flatten()
            jokerPermutations = newJokerPermutations
        }

        var bestType = handType(cardList)
        for (permutation in jokerPermutations) {
            val type = handType(permutation)
            if (type.ordinal < bestType.ordinal) {
                bestType = type
            }
        }

        return bestType
    }
}

fun handType(cardList: List<String>): Type {
    val countByCard = cardList
        .groupingBy { it }
        .eachCount()

    val values = countByCard.values.sortedDescending().toList()

    return if (values.contains(5)) { Type.FIVE_OF_A_KIND }
    else if (values.contains(4)) { Type.FOUR_OF_A_KIND }
    else if (values == listOf(3, 2)) { Type.FULL_HOUSE }
    else if (values.contains(3)) { Type.THREE_OF_A_KIND }
    else if (values == listOf(2, 2, 1)) { Type.TWO_PAIR }
    else if (values.contains(2)) { Type.ONE_PAIR }
    else { Type.HIGH_CARD }
}

class Day07CamelCards(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)
    private val hands = input
        .map { it.split(" ") }
        .map { (hand, bid) -> Hand(hand, bid.toInt()) }

    override fun solvePart1(): String {
        val cardValues: Map<String, Int> = mapOf(
            "A" to 1,
            "K" to 2,
            "Q" to 3,
            "J" to 4,
            "T" to 5,
            "9" to 6,
            "8" to 7,
            "7" to 8,
            "6" to 9,
            "5" to 10,
            "4" to 11,
            "3" to 12,
            "2" to 13,
        )

        val orderedHands = hands.sortedWith(compareBy<Hand> { handType(it.cardList) }
            .thenBy { cardValues[it.cardList[0]]!! }
            .thenBy { cardValues[it.cardList[1]]!! }
            .thenBy { cardValues[it.cardList[2]]!! }
            .thenBy { cardValues[it.cardList[3]]!! }
            .thenBy { cardValues[it.cardList[4]]!! }
        ).reversed()

        val totalWinnings = orderedHands.withIndex().sumOf { (index, hand) -> hand.bid * (index + 1) }

        return totalWinnings.toString()
    }

    override fun solvePart2(): String {
        val cardValues: Map<String, Int> = mapOf(
            "A" to 1,
            "K" to 2,
            "Q" to 3,
            "T" to 4,
            "9" to 5,
            "8" to 6,
            "7" to 7,
            "6" to 8,
            "5" to 9,
            "4" to 10,
            "3" to 11,
            "2" to 12,
            "J" to 13,
        )

        val orderedHands = hands.sortedWith(compareBy<Hand> { it.typeWithWildcards }
            .thenBy { cardValues[it.cardList[0]]!! }
            .thenBy { cardValues[it.cardList[1]]!! }
            .thenBy { cardValues[it.cardList[2]]!! }
            .thenBy { cardValues[it.cardList[3]]!! }
            .thenBy { cardValues[it.cardList[4]]!! }
        ).reversed()

        val totalWinnings = orderedHands.withIndex().sumOf { (index, hand) -> hand.bid * (index + 1) }

        return totalWinnings.toString()
    }
}

fun main() {
    Day07CamelCards("input/2023/07.test.txt").run(part1ExpectedSolution = "6440", part2ExpectedSolution = "5905")
    Day07CamelCards("input/2023/07.txt").run()
}
