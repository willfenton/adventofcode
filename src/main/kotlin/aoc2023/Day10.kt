package aoc2023

import Grid
import InputParser
import Point
import Solver
import kotlin.math.min

// https://adventofcode.com/2023/day/10

enum class TileType(val string: String) {
    Vertical("|"),
    Horizontal("-"),
    UpRight("└"),
    UpLeft("┘"),
    DownLeft("┐"),
    DownRight("┌"),
    Ground("."),
    StartingPosition("S"),
}

data class Tile(val p: Point, val tileType: TileType) {
    var distanceFromStartingPoint: Int? = null
    var isEnclosed: Boolean = true
    var smallTile: Tile? = null

    val allNeighbors = setOf(p.u, p.d, p.l, p.r)
    private val connectedNeighbors = when (tileType) {
        TileType.Vertical -> setOf(p.u, p.d)
        TileType.Horizontal -> setOf(p.l, p.r)
        TileType.UpRight -> setOf(p.u, p.r)
        TileType.UpLeft -> setOf(p.u, p.l)
        TileType.DownLeft -> setOf(p.d, p.l)
        TileType.DownRight -> setOf(p.d, p.r)
        TileType.Ground -> setOf(p.u, p.d, p.l, p.r)
        TileType.StartingPosition -> setOf(p.u, p.d, p.l, p.r)
    }

    fun connectsTo(otherTile: Tile): Boolean {
        return if (this.tileType == TileType.Ground && otherTile.tileType != TileType.Ground) false
        else if (this.tileType != TileType.Ground && otherTile.tileType == TileType.Ground) false
        else this.connectedNeighbors.contains(otherTile.p) && otherTile.connectedNeighbors.contains(this.p)
    }
}

class Day10(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)
    private lateinit var grid: Grid<Tile>
    private lateinit var bigGrid: Grid<Tile>

    override fun solvePart1(): String {
        grid = parseGrid()
        addExtraGroundTilesToOutsideEdges(grid)

        bfsMainLoop(grid)
        bfsOutsideMainLoop(grid)

        println("Grid:")
        printGrid(grid)

        val mainLoopTiles = grid.values.filter { it.distanceFromStartingPoint != null }
        return mainLoopTiles.maxOf { it.distanceFromStartingPoint!! }.toString()
    }

    override fun solvePart2(): String {
        bigGrid = makeBigGrid()
        addExtraGroundTilesToOutsideEdges(bigGrid)

        bfsMainLoop(bigGrid)
        bfsOutsideMainLoop(bigGrid)

        println("Big grid:")
        printGrid(bigGrid)
        println("Big grid, showing enclosed:")
        printGrid(bigGrid, showEnclosed = true)
        println("Grid, showing enclosed:")
        printGrid(grid, showEnclosed = true)

        for (tile in grid.values) {
            val bigGridTile = bigGrid[Point(tile.p.x * 3, tile.p.y * 3)]!!
            tile.isEnclosed = bigGridTile.isEnclosed
        }

        return grid.values.count { it.isEnclosed && it.distanceFromStartingPoint == null }.toString()
    }

    private fun parseGrid() = input.mapIndexed { y, line ->
        line.split("").filter { it.isNotBlank() }.mapIndexed { x, s ->
            val point = Point(x, y)
            val tileType = when (s) {
                "|" -> TileType.Vertical
                "-" -> TileType.Horizontal
                "L" -> TileType.UpRight
                "J" -> TileType.UpLeft
                "7" -> TileType.DownLeft
                "F" -> TileType.DownRight
                "." -> TileType.Ground
                "S" -> TileType.StartingPosition
                else -> throw Exception("bad code")
            }
            Tile(point, tileType)
        }
    }.flatten().associateBy { tile -> tile.p }.toMutableMap()

    private fun addExtraGroundTilesToOutsideEdges(grid: Grid<Tile>) {
        val minX = grid.keys.minOf { it.x } - 1
        val maxX = grid.keys.maxOf { it.x } + 1
        val minY = grid.keys.minOf { it.y } - 1
        val maxY = grid.keys.maxOf { it.y } + 1
        for (x in minX..maxX) {
            for (y in minY..maxY) {
                if (x == minX || x == maxX || y == minY || y == maxY) {
                    val point = Point(x, y)
                    val tile = Tile(point, TileType.Ground)
                    tile.isEnclosed = false
                    grid[point] = tile
                }
            }
        }
    }

    private fun bfsMainLoop(grid: Grid<Tile>) {
        val startingPosition = grid.values.find { it.tileType == TileType.StartingPosition }!!
        startingPosition.distanceFromStartingPoint = 0

        val visited = mutableSetOf<Tile>()
        val queue = mutableListOf(startingPosition)

        while (queue.isNotEmpty()) {
            val tile = queue.removeFirst()
            visited.add(tile)

            val unvisitedNeighbors = tile.allNeighbors
                .mapNotNull { point -> grid[point] }
                .filter { potentialNeighbor ->
                    tile.connectsTo(potentialNeighbor) && !visited.contains(potentialNeighbor)
                }

            for (neighbor in unvisitedNeighbors) {
                neighbor.distanceFromStartingPoint = min(
                    tile.distanceFromStartingPoint!! + 1,
                    neighbor.distanceFromStartingPoint ?: Int.MAX_VALUE
                )
                if (!queue.contains(neighbor)) {
                    queue.add(neighbor)
                }
            }
        }
    }

    private fun bfsOutsideMainLoop(grid: Grid<Tile>) {
        val visited = mutableSetOf<Tile>()
        val queue = grid.values.filter { !it.isEnclosed }.toMutableList()

        while (queue.isNotEmpty()) {
            val tile = queue.removeFirst()
            visited.add(tile)

            val unvisitedNeighbors = tile.allNeighbors
                .mapNotNull { point -> grid[point] }
                .filter { potentialNeighbor ->
                    potentialNeighbor.distanceFromStartingPoint == null && // not part of main loop
                    !visited.contains(potentialNeighbor)
                }

            for (neighbor in unvisitedNeighbors) {
                neighbor.isEnclosed = false
                if (!queue.contains(neighbor)) {
                    queue.add(neighbor)
                }
            }
        }
    }

    private fun printGrid(grid: Grid<Tile>, showEnclosed: Boolean = false) {
        val minX = grid.keys.minOf { it.x } + 1
        val maxX = grid.keys.maxOf { it.x } - 1
        val minY = grid.keys.minOf { it.y } + 1
        val maxY = grid.keys.maxOf { it.y } - 1
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val tile = grid[Point(x, y)]!!
                if (tile.distanceFromStartingPoint != null || !showEnclosed) {
                    print(tile.tileType.string)
                } else {
                    print(if (tile.isEnclosed) "I" else "O")
                }
            }
            println()
        }
        println()
    }

    private fun makeBigGrid(): Grid<Tile> {
        val bigGrid: Grid<Tile> = mutableMapOf()

        for (tile in grid.values) {
            val threeByThree = when (tile.tileType) {
                TileType.Vertical -> """
                    .|.
                    .|.
                    .|.
                """.trimIndent()
                TileType.Horizontal -> """
                    ...
                    ---
                    ...
                """.trimIndent()
                TileType.UpRight -> """
                    .|.
                    .L-
                    ...
                """.trimIndent()
                TileType.UpLeft -> """
                    .|.
                    -J.
                    ...
                """.trimIndent()
                TileType.DownLeft -> """
                    ...
                    -7.
                    .|.
                """.trimIndent()
                TileType.DownRight -> """
                    ...
                    .F-
                    .|.
                """.trimIndent()
                TileType.Ground -> """
                    ...
                    ...
                    ...
                """.trimIndent()
                TileType.StartingPosition -> """
                    .|.
                    -S-
                    .|.
                """.trimIndent()
            }.lines()
            threeByThree.forEachIndexed { y, line ->
                line.split("").filter { it.isNotBlank() }.forEachIndexed { x, s ->
                    val point = Point((tile.p.x * 3) + x, (tile.p.y * 3) + y)
                    val tileType = when (s) {
                        "|" -> TileType.Vertical
                        "-" -> TileType.Horizontal
                        "L" -> TileType.UpRight
                        "J" -> TileType.UpLeft
                        "7" -> TileType.DownLeft
                        "F" -> TileType.DownRight
                        "." -> TileType.Ground
                        "S" -> TileType.StartingPosition
                        else -> throw Exception("bad code")
                    }
                    val bigTile = Tile(point, tileType)
                    bigTile.smallTile = tile
                    bigGrid[point] = bigTile
                }
            }
        }

        return bigGrid
    }
}

fun main() {
    Day10("input/2023/10.test.txt").run(part1ExpectedSolution = "4", part2ExpectedSolution = "1")
    Day10("input/2023/10.test2.txt").run(part1ExpectedSolution = "8", part2ExpectedSolution = "1")
    Day10("input/2023/10.test3.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = "4")
    Day10("input/2023/10.test4.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = "4")
    Day10("input/2023/10.test5.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = "8")
    Day10("input/2023/10.test6.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = "10")
    Day10("input/2023/10.txt").run(part1ExpectedSolution = "6882", part2ExpectedSolution = "491")
}
