package aoc2021

import InputParser
import Solver

// https://adventofcode.com/2021/day/11

class Day11DumboOctopus(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String {
        var state = parseState(input)
        var totalFlashCount = 0

        repeat(100) {
            val (newState, flashCount) = step(state)
            state = newState
            totalFlashCount += flashCount
        }

        return totalFlashCount.toString()
    }

    override fun solvePart2(): String {
        var state = parseState(input)

        var step = 0
        while (true) {
            val (newState, flashCount) = step(state)
            state = newState
            step += 1

            if (flashCount == state.size) {
                return step.toString()
            }
        }
    }

    private fun parseState(input: List<String>): Map<Point, Int> {
        val state: MutableMap<Point, Int> = mutableMapOf()

        for ((x, line) in input.withIndex()) {
            for ((y, char) in line.toList().withIndex()) {
                val point = Point(x, y)
                state[point] = char.digitToInt()
            }
        }

        return state
    }

    // return updated map and flash count for that step
    private fun step(initialState: Map<Point, Int>): Pair<Map<Point, Int>, Int> {
        val state = initialState.toMutableMap()
        var flashCount = 0

        // First, the energy level of each octopus increases by 1.
        for ((point, energy) in state) {
            state[point] = energy + 1
        }

        // Then, any octopus with an energy level greater than 9 flashes.
        // This increases the energy level of all adjacent octopuses by 1, including octopuses that are diagonally adjacent.
        // If this causes an octopus to have an energy level greater than 9, it also flashes.
        // This process continues as long as new octopuses keep having their energy level increased beyond 9.
        // An octopus can only flash at most once per step.
        val alreadyFlashed = mutableSetOf<Point>()
        while (true) {
            val flashing = state.filter { (point, energy) -> energy > 9 && !alreadyFlashed.contains(point) }.keys
            if (flashing.isEmpty()) break

            for (point in flashing) {
                for (neighbor in point.neighbors + point.diagonalNeighbors) {
                    if (!state.containsKey(neighbor)) continue
                    state[neighbor] = state[neighbor]!! + 1
                }
            }

            alreadyFlashed.addAll(flashing)
        }

        // Finally, any octopus that flashed during this step has its energy level set to 0, as it used all of its energy to flash.
        for ((point, energy) in state) {
            if (energy > 9) {
                state[point] = 0
                flashCount++
            }
        }

        return Pair(state, flashCount)
    }
}

fun main() {
    Solver.run { Day11DumboOctopus("input/2021/11.test.txt") }
    Solver.run { Day11DumboOctopus("input/2021/11.txt") }
}
