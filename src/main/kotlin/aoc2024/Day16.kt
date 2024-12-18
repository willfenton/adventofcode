package aoc2024

import Direction
import Grid
import Position
import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/16

private data class PosAndDir(val position: Position, val direction: Direction)

private const val WALL = true

class Day16(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val grid: Grid<Boolean> = Grid()
    private lateinit var startPosition: Position
    private lateinit var endPosition: Position
    private val startDirection = Direction.RIGHT

    init {
        for (y in input.indices) {
            for (x in input[y].indices) {
                val pos = Position(x, y)
                val c = input[y][x]

                // true = wall
                grid[pos] = (c == '#')

                if (c == 'S') {
                    startPosition = pos
                } else if (c == 'E') {
                    endPosition = pos
                }
            }
        }
        grid.init()
    }

    override fun solvePart1(): String = dijkstra().first.toString()

    private fun dijkstra(part2: Boolean = false): Pair<Int, Int> {
        val start = PosAndDir(startPosition, startDirection)
        val visited = mutableSetOf<PosAndDir>()
        val queue = mutableListOf(start)
        val previous = mutableMapOf<PosAndDir, MutableList<PosAndDir>>()
        val distances = mutableMapOf(start to 0)

        while (queue.isNotEmpty()) {
            val minDistanceIndex = queue.withIndex().minBy { (index, node) -> distances[node] ?: Int.MAX_VALUE }.index
            val current = queue.removeAt(minDistanceIndex)

            visited.add(current)

            val neighbors = getNeighbors(current).filter { it !in visited }

            for (neighbor in neighbors) {
                val addedScore = if (neighbor.position == current.position) 1000 else 1
                val newDistance = distances[current]!! + addedScore

                val currentDistance = distances[neighbor]
                if (currentDistance == null || newDistance < currentDistance) {
                    distances[neighbor] = newDistance
                    previous[neighbor] = mutableListOf(current)
                } else if (newDistance == currentDistance) {
                    previous[neighbor]!!.add(current)
                }

                if (neighbor !in queue) {
                    queue.add(neighbor)
                }
            }
        }

        val minScore = distances
            .filter { it.key.position == endPosition }
            .values
            .min()

        return minScore to countTilesInBestPath(previous, distances, part2)
    }

    private fun countTilesInBestPath(
        previous: Map<PosAndDir, List<PosAndDir>>,
        distances: Map<PosAndDir, Int>,
        part2: Boolean = false
    ): Int {
        val minScore = distances
            .filter { it.key.position == endPosition }
            .values
            .min()
        val minScoreEnds = distances.filter { it.value == minScore && it.key.position == endPosition }.keys

        val visited: MutableSet<PosAndDir> = minScoreEnds.toMutableSet()
        val queue: MutableList<PosAndDir> = minScoreEnds.toMutableList()

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            try {
                val previousPositions = previous[current]!!
                val unvisitedPreviousPositions = previousPositions.filter { it !in visited }

                for (previousPosition in unvisitedPreviousPositions) {
                    queue.add(previousPosition)
                    visited.add(previousPosition)
                }
            } catch (e: Exception) {
                println(e)
                println(current)
            }
        }

        val tilesInBestPath = visited.map { it.position }.toSet()

        if (part2) {
            for (y in 0..<grid.height) {
                for (x in 0..<grid.width) {
                    val pos = Position(x, y)
                    val tile = grid[pos]!!
                    print(
                        when {
                            tile.data == WALL -> "#"
                            tile.pos == startPosition -> 'S'
                            tile.pos == endPosition -> 'E'
                            tile.pos in tilesInBestPath -> 'O'
                            else -> '.'
                        }
                    )
                }
                println()
            }
            println()
        }

        return visited.map { it.position }.toSet().size
    }

    private fun getNeighbors(posAndDir: PosAndDir): List<PosAndDir> {
        val (position, direction) = posAndDir

        val neighbors = mutableListOf(
            posAndDir.copy(direction = direction.rotate90Left()),
            posAndDir.copy(direction = direction.rotate90Right())
        )

        val inFront = grid[position + direction.position]!!
        if (inFront.data != WALL) {
            neighbors.add(posAndDir.copy(position = inFront.pos))
        }

        return neighbors
    }

    override fun solvePart2(): String = dijkstra(part2 = true).second.toString()
}

fun main() {
    Day16(
        inputFile(
            """
            ###############
            #.......#....E#
            #.#.###.#.###.#
            #.....#.#...#.#
            #.###.#####.#.#
            #.#.#.......#.#
            #.#.#####.###.#
            #...........#.#
            ###.#.#####.#.#
            #...#.....#.#.#
            #.#.#.###.#.#.#
            #.....#...#.#.#
            #.###.#.#.#.#.#
            #S..#.....#...#
            ###############
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "7036", part2ExpectedSolution = "45")

    Day16(
        inputFile(
            """
            #################
            #...#...#...#..E#
            #.#.#.#.#.#.#.#.#
            #.#.#.#...#...#.#
            #.#.#.#.###.#.#.#
            #...#.#.#.....#.#
            #.#.#.#.#.#####.#
            #.#...#.#.#.....#
            #.#.#####.#.###.#
            #.#.#.......#...#
            #.#.###.#####.###
            #.#.#...#.....#.#
            #.#.#.#####.###.#
            #.#.#.........#.#
            #.#.#.#########.#
            #S#.............#
            #################
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "11048", part2ExpectedSolution = "64")

    Day16(downloadInput(2024, 16)).run()
}
