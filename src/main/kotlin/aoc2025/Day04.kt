package aoc2025

import Grid
import Position
import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2025/day/4

enum class Tile {
    EMPTY,
    PAPER
}

class Day04(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val grid = Grid<Tile>()

    // number of papers removed at each step
    private val papersRemoved = mutableListOf<Int>()

    init {
        for (y in input.indices) {
            val line = input[y]
            for (x in line.indices) {
                val tile = when (input[y][x]) {
                    '.' -> Tile.EMPTY
                    '@' -> Tile.PAPER
                    else -> throw IllegalArgumentException()
                }
                grid[Position(x, y)] = tile
            }
        }
        grid.init()

        do {
            papersRemoved.add(removePaper())
        } while (papersRemoved.last() > 0)
    }

    private fun removePaper(): Int {
        val paperCells = grid.cells.values.filter { it.data == Tile.PAPER }
        val cellsToRemove = paperCells.filter { gridCell ->
            val neighbors = gridCell.neighbors(directions = Direction.ALL)
            neighbors.count { it.data == Tile.PAPER } < 4
        }
        cellsToRemove.forEach { gridCell ->
            gridCell.data = Tile.EMPTY
        }
        return cellsToRemove.size
    }

    override fun solvePart1(): String = papersRemoved.first().toString()

    override fun solvePart2(): String = papersRemoved.sum().toString()
}

fun main() {
    Day04(
        inputFile(
            """
            ..@@.@@@@.
            @@@.@.@.@@
            @@@@@.@.@@
            @.@@@@..@.
            @@.@@@@.@@
            .@@@@@@@.@
            .@.@.@.@@@
            @.@@@.@@@@
            .@@@@@@@@.
            @.@.@@@.@.
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "13", part2ExpectedSolution = "43")

    Day04(downloadInput(2025, 4)).run(part1ExpectedSolution = "1516", part2ExpectedSolution = "9122")
}
