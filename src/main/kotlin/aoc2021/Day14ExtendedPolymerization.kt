package aoc2021

import InputParser
import Solver

// https://adventofcode.com/2021/day/14

class Day14ExtendedPolymerization(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)
    private val template = input.first().toList()
    private val rules =
        input.drop(2).map { it.split(" -> ") }.associate { (pair, insertion) ->
            Pair(pair[0], pair[1]) to insertion.toList().single()
        }

    override fun solvePart1(): String {
        val polymer = Polymer(template)
        repeat(10) {
            polymer.step(rules)
        }
        return polymer.score.toString()
    }

    override fun solvePart2(): String {
        val polymer = Polymer(template)
        repeat(40) {
            polymer.step(rules)
        }
        return polymer.score.toString()
    }
}

data class Polymer(private val initialState: List<Char>) {
    private var state =
        initialState
            .windowed(2)
            .map { (first, second) ->
                Pair(first, second)
            }.groupingBy { it }
            .eachCount()
            .mapValues { it.value.toULong() }

    fun step(rules: Map<Pair<Char, Char>, Char>) {
        val newState = mutableMapOf<Pair<Char, Char>, ULong>()
        for ((pair, count) in state) {
            if (rules.containsKey(pair)) {
                val insertion = rules[pair]!!
                val pair1 = Pair(pair.first, insertion)
                val pair2 = Pair(insertion, pair.second)
                newState[pair1] = newState.getOrDefault(pair1, 0uL) + count
                newState[pair2] = newState.getOrDefault(pair2, 0uL) + count
            } else {
                newState[pair] = count
            }
        }
        state = newState
    }

    val score: ULong
        get() {
            val elementCounts = mutableMapOf(initialState[0] to 1uL)
            for ((pair, count) in state) {
                elementCounts[pair.second] = elementCounts.getOrDefault(pair.second, 0u) + count
            }
            return elementCounts.values.max() - elementCounts.values.min()
        }
}

fun main() {
    Solver.run { Day14ExtendedPolymerization("input/2021/14.test.txt") }
    Solver.run { Day14ExtendedPolymerization("input/2021/14.txt") }
}
