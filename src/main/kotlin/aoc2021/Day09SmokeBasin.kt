package aoc2021

import InputParser
import Solver

// https://adventofcode.com/2021/day/9

class Day09SmokeBasin(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)
    private val heightMap: MutableMap<Point, Int> = mutableMapOf()

    init {
        for ((x, line) in input.withIndex()) {
            for ((y, char) in line.toList().withIndex()) {
                val point = Point(x, y)
                heightMap[point] = char.digitToInt()
            }
        }
    }

    override fun solvePart1(): String = heightMap
        .filter { isLowPoint(it.key) }
        .values
        .sumOf { it + 1 }
        .toString()

    override fun solvePart2(): String {
        val explored = mutableSetOf<Point>()
        val basinSizes = mutableListOf<Int>()

        for ((point, height) in heightMap.entries) {
            if (height == 9 || explored.contains(point)) continue

            // new basin
            var basinSize = 0

            val frontier = mutableListOf(point)
            while (frontier.isNotEmpty()) {
                val frontierPoint = frontier.removeFirst()

                frontier.addAll(
                    frontierPoint.neighbors.filter {
                        !explored.contains(it) && !frontier.contains(it) && heightMap.containsKey(it) && heightMap[it]!! < 9
                    }
                )

                explored.add(frontierPoint)

                basinSize += 1
            }

            basinSizes.add(basinSize)
        }

        return basinSizes
            .sortedDescending()
            .take(3)
            .reduce { acc, i -> acc * i }
            .toString()
    }

    private fun isLowPoint(point: Point): Boolean {
        val height = heightMap[point]!!

        for (neighbor in point.neighbors) {
            if (heightMap.getOrDefault(neighbor, height + 1) <= height) return false
        }

        return true
    }
}

fun main() {
    Solver.run { Day09SmokeBasin("input/2021/09.test.txt") }
    Solver.run { Day09SmokeBasin("input/2021/09.txt") }
}
