@file:Suppress("DuplicatedCode")

package aoc2024

import Grid
import GridCell
import Position
import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/10

class Day10(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val grid = Grid<Int>()

    init {
        for (y in input.indices) {
            for (x in input[y].indices) {
                try {
                    grid[Position(x, y)] = input[y][x].toString().toInt()
                } catch (e: Exception) {
                    grid[Position(x, y)] = -1
                }
            }
        }
    }

    override fun solvePart1(): String {
        val trailheads = grid.cells.values.filter { it.data == 0 }
        return trailheads.sumOf { trailhead -> trailBfs(trailhead).first.count { it.data == 9 } }.toString()
    }

    override fun solvePart2(): String {
        val trailheads = grid.cells.values.filter { it.data == 0 }
        var totalScore = 0
        for (trailhead in trailheads) {
            val (visited, previous) = trailBfs(trailhead)
            totalScore += (1 + countDistinctTrails(previous, trailhead))
        }
        return totalScore.toString()
    }

    fun trailBfs(trailhead: GridCell<Int>): Pair<Set<GridCell<Int>>, Map<GridCell<Int>, Set<GridCell<Int>>>> {
        val neighborDirections = Direction.CARDINAL
        val isConnected: (cell: GridCell<Int>, other: GridCell<Int>) -> Boolean = { cell, other -> other.data == cell.data + 1 }

        val visited: MutableSet<GridCell<Int>> = mutableSetOf(trailhead)
        val queue: MutableList<GridCell<Int>> = mutableListOf(trailhead)
        val previous = mutableMapOf<GridCell<Int>, MutableSet<GridCell<Int>>>()

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            visited.add(current)

            val connectedNeighbors = current.connectedNeighbors(neighborDirections, isConnected)

            for (neighbor in connectedNeighbors) {
                queue.add(neighbor)
                if (current in previous.keys) {
                    previous[current]!!.add(neighbor)
                } else {
                    previous[current] = mutableSetOf(neighbor)
                }

                if (neighbor !in previous) {
                    previous[neighbor] = mutableSetOf()
                }
            }
        }

        return Pair(visited, previous)
    }

    fun countDistinctTrails(trails: Map<GridCell<Int>, Set<GridCell<Int>>>, current: GridCell<Int>): Int {
        if (current.data == 9) {
            return 0
        }
        val neighbors = trails[current]!!
        return (neighbors.size - 1) + neighbors.sumOf { countDistinctTrails(trails, it) }
    }
}

fun main() {
    Day10(
        inputFile(
            """
            .....0.
            ..4321.
            ..5..2.
            ..6543.
            ..7..4.
            ..8765.
            ..9....
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = null, part2ExpectedSolution = "3")

    Day10(
        inputFile(
            """
            ..90..9
            ...1.98
            ...2..7
            6543456
            765.987
            876....
            987....
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = null, part2ExpectedSolution = "13")

    Day10(
        inputFile(
            """
            89010123
            78121874
            87430965
            96549874
            45678903
            32019012
            01329801
            10456732
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "36", part2ExpectedSolution = "81")

    Day10(downloadInput(2024, 10)).run()
}
