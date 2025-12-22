package aoc2025

import Direction
import Grid
import Position
import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2025/day/7

enum class Day07Tile(val char: Char) {
    EMPTY('.'),
    SPLITTER('^'),
    START('S'),
    BEAM('|')
}

data class Day07TileData(var type: Day07Tile, var paths: Long = 0, var splitterSplit: Boolean = false)

class Day07(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val grid = Grid<Day07TileData>()

    init {
        for (y in input.indices) {
            val line = input[y]
            for (x in line.indices) {
                val tile = Day07Tile.entries.find { it.char ==  input[y][x] }!!
                grid[Position(x, y)] = Day07TileData(tile)
            }
        }
        grid.init()
        simulate()
    }

    private fun simulate() {
        val startTile = grid.cells.values.find { it.data.type == Day07Tile.START }!!
        val firstBeam = startTile.neighbor(Direction.DOWN)
        firstBeam!!.data.type = Day07Tile.BEAM
        firstBeam.data.paths = 1

        do {
            val allBeams = grid.cells.values.filter { it.data.type == Day07Tile.BEAM && !it.visited }

            for (beam in allBeams) {
                beam.visited = true
                val down = beam.neighbor(Direction.DOWN) ?: continue
                when (down.data.type) {
                    Day07Tile.EMPTY -> {
                        down.data = beam.data.copy()
                    }
                    Day07Tile.SPLITTER -> {
                        down.data.splitterSplit = true
                        val left = down.neighbor(Direction.LEFT)
                        val right = down.neighbor(Direction.RIGHT)

                        if (left?.data?.type == Day07Tile.EMPTY) {
                            left.data = beam.data.copy()
                        }
                        else if (left?.data?.type == Day07Tile.BEAM) {
                            left.data.paths += beam.data.paths
                        }

                        if (right?.data?.type == Day07Tile.EMPTY) {
                            right.data = beam.data.copy()
                        }
                        else if (right?.data?.type == Day07Tile.BEAM) {
                            right.data.paths += beam.data.paths
                        }
                    }
                    Day07Tile.BEAM -> {
                        down.data.paths += beam.data.paths
                    }
                    else -> {}
                }
            }
        } while (allBeams.isNotEmpty())
    }

    override fun solvePart1(): String {
        return grid.cells.values.count { it.data.type == Day07Tile.SPLITTER && it.data.splitterSplit }.toString()
    }

    override fun solvePart2(): String {
        return grid.cells.values.filter { it.pos.y == grid.maxY && it.data.type == Day07Tile.BEAM }.sumOf { it.data.paths }.toString()
    }
}

fun main() {
    Day07(
        inputFile(
            """
            .......S.......
            ...............
            .......^.......
            ...............
            ......^.^......
            ...............
            .....^.^.^.....
            ...............
            ....^.^...^....
            ...............
            ...^.^...^.^...
            ...............
            ..^...^.....^..
            ...............
            .^.^.^.^.^...^.
            ...............
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "21", part2ExpectedSolution = "40")

    Day07(downloadInput(2025, 7)).run(part1ExpectedSolution = "1550", part2ExpectedSolution = "9897897326778")
}
