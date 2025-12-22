package aoc2025

import Direction
import Grid
import Position
import Solver
import aoc2023.Day10Point
import aoc2023.Tile
import downloadInput
import inputFile
import java.nio.file.Path
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

// https://adventofcode.com/2025/day/9

enum class Day09Tile{
    RED,
    GREEN,
    OTHER
}

class Day09(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val redTiles: List<Position> = input.map { line ->
        val coords = line.split(",").map { it.toInt() }
        Position(coords[0], coords[1])
    }

    private val grid = Grid<Day09Tile>()

    private val verticalLines = mutableListOf<Pair<Position, Position>>()
    private val horizontalLines = mutableListOf<Pair<Position, Position>>()

    init {
        for (tile in redTiles) {
            grid[tile] = Day09Tile.RED
        }
        grid.init()

        val wrapped = redTiles.toMutableList()
        wrapped.add(redTiles.first())
        for ((p1, p2) in wrapped.windowed(2)) {
            if (p1.x == p2.x) {
                val yRange = IntRange(min(p1.y, p2.y), max(p1.y, p2.y))
                verticalLines.add(Position(p1.x, yRange.first) to Position(p1.x, yRange.last))
                for (y in yRange) {
                    val pos = Position(p1.x, y)
                    val existing = grid[pos]
                    if (existing == null) {
                        grid[pos] = Day09Tile.GREEN
                    }
                }
            } else if (p1.y == p2.y) {
                val xRange = IntRange(min(p1.x, p2.x), max(p1.x, p2.x))
                horizontalLines.add(Position(xRange.first, p1.y) to Position(xRange.last, p1.y))
                for (x in xRange) {
                    val pos = Position(x, p1.y)
                    val existing = grid[pos]
                    if (existing == null) {
                        grid[pos] = Day09Tile.GREEN
                    }
                }
            } else {
                throw Exception()
            }
        }

        // tryFloodFill()

        grid.init()
    }

    override fun solvePart1(): String {
        val rectSizes = mutableListOf<Long>()
        for (c1 in redTiles) {
            for (c2 in redTiles) {
                if (c1 != c2) {
                    rectSizes += (abs(c1.x - c2.x) + 1).toLong() * (abs(c1.y - c2.y) + 1).toLong()
                }
            }
        }
        return rectSizes.max().toString()
    }

    override fun solvePart2(): String {
        val rectSizes = mutableListOf<Long>()
        for (c1 in redTiles) {
            for (c2 in redTiles) {
                if (c1 != c2) {
                    if (isValidRectP2(c1, c2)) {
                        rectSizes += (abs(c1.x - c2.x) + 1).toLong() * (abs(c1.y - c2.y) + 1).toLong()
                    }
                }
            }
        }
        return rectSizes.max().toString()
    }

    private fun isValidRectP2(c1: Position, c2: Position): Boolean {
        val xMin = min(c1.x, c2.x)
        val xMax = max(c1.x, c2.x)
        val yMin = min(c1.y, c2.y)
        val yMax = max(c1.y, c2.y)

        val intersectingVerticalLines = verticalLines.filter { (p1, p2) ->
            val lineX = p1.x
            val lineYMin = min(p1.y, p2.y)
            val lineYMax = max(p1.y, p2.y)
            val xOverlaps = lineX in (xMin + 1)..(xMax - 1)
            val yOverlaps = lineYMax > yMin && lineYMin < yMax
            xOverlaps && yOverlaps
        }

        val intersectingHorizontalLines = horizontalLines.filter { (p1, p2) ->
            val lineY = p1.y
            val lineXMin = min(p1.x, p2.x)
            val lineXMax = max(p1.x, p2.x)
            val yOverlaps = lineY in (yMin + 1)..(yMax - 1)
            val xOverlaps = lineXMax > xMin && lineXMin < xMax
            xOverlaps && yOverlaps
        }

        return intersectingVerticalLines.isEmpty() && intersectingHorizontalLines.isEmpty()
    }

// Obviously not gonna work for the big grid...
//    private fun printGrid() {
//        for (y in grid.minY..grid.maxY) {
//            for (x in grid.minX..grid.maxX) {
//                val tile = grid[Position(x, y)]
//                when (tile?.data) {
//                    Day09Tile.RED -> print('R')
//                    Day09Tile.GREEN -> print('G')
//                    Day09Tile.OTHER -> print('.')
//                    null -> print('.')
//                }
//            }
//            println()
//        }
//        println()
//    }

// Bruteforce approach, gave up on it after it ran for an hour then OOM'd
//    private fun tryFloodFill() {
//        val averageX = redTiles.take(3).map { it.x }.average().toInt()
//        val averageY = redTiles.take(3).map { it.y }.average().toInt()
//        val startPos = Position(averageX, averageY)
//
//        val visited: MutableSet<Position> = mutableSetOf(startPos)
//        val queue: MutableList<Position> = mutableListOf(startPos)
//
//        println("Starting flood fill")
//
//        while (queue.isNotEmpty()) {
//            println(queue.size)
//            val currentPos = queue.removeFirst()
//            grid[currentPos] = Day09Tile.GREEN
//
//            val unvisitedNeighbors = currentPos.neighbors(Direction.CARDINAL).filter { it !in visited && grid[it] == null }
//
//            for (neighbor in unvisitedNeighbors) {
//                queue.add(neighbor)
//                visited.add(neighbor)
//            }
//        }
//
//        println("Done flood fill")
//    }
}

fun main() {
    Day09(
        inputFile(
            """
            7,1
            11,1
            11,7
            9,7
            9,5
            2,5
            2,3
            7,3
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "50", part2ExpectedSolution = "24")

    Day09(downloadInput(2025, 9)).run(part1ExpectedSolution = "4782151432", part2ExpectedSolution = "1450414119")
}
