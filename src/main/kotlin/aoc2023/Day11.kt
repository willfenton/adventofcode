package aoc2023

import InputParser
import Solver
import kotlin.math.abs

// https://adventofcode.com/2023/day/11

data class Planet(var x: Long, var y: Long)

class Day11(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    private val planets: List<Planet> = input.mapIndexed { y, line ->
        line.split("").filter { it.isNotBlank() }.mapIndexedNotNull { x, s ->
            if (s == "#") Planet(x.toLong(), y.toLong()) else null
        }
    }.flatten().toMutableList()

    override fun solvePart1() = allDistances(expandUniverse(2)).sum().toString()

    override fun solvePart2() = allDistances(expandUniverse(1000000)).sum().toString()

    private fun expandUniverse(expansionSize: Long): List<Planet> {
        val expanded = planets.map { it.copy() }.toList()

        val planetX = expanded.map { it.x }.toSet()
        val planetY = expanded.map { it.y }.toSet()

        val emptyX = (planetX.min()..planetX.max()).minus(planetX).sortedDescending()
        val emptyY = (planetY.min()..planetY.max()).minus(planetY).sortedDescending()

        for (x in emptyX) {
            for (planet in expanded.filter { it.x > x }) {
                planet.x += expansionSize - 1
            }
        }

        for (y in emptyY) {
            for (planet in expanded.filter { it.y > y }) {
                planet.y += expansionSize - 1
            }
        }

        return expanded
    }

    private fun allDistances(planets: List<Planet>): List<Long> {
        val distances = mutableListOf<Long>()
        for (i in planets.indices) {
            for (j in i+1..<planets.size) {
                distances.add(distanceBetweenPlanets(planets[i], planets[j]))
            }
        }
        return distances
    }

    private fun distanceBetweenPlanets(p1: Planet, p2: Planet): Long {
        return abs(p1.x - p2.x) + abs(p1.y - p2.y)
    }
}

fun main() {
    Day11("input/2023/11.test.txt").run(part1ExpectedSolution = "374", part2ExpectedSolution = null)
    Day11("input/2023/11.txt").run()
}
