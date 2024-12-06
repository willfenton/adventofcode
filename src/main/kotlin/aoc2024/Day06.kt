package aoc2024

import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/6

enum class Day6Direction(val x: Int, val y: Int) {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    fun rotate(): Day6Direction = when (this) {
        UP -> RIGHT
        DOWN -> LEFT
        LEFT -> UP
        RIGHT -> DOWN
    }
}

class Day06(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val width = input.first().length
    private val height = input.size

    private var guardY: Int = 0
    private var guardX: Int = 0
    private lateinit var guardDirection: Day6Direction
    private lateinit var obstacles: MutableSet<Pair<Int, Int>>
    private lateinit var guardVisited: MutableSet<Pair<Int, Int>>

    private fun init() {
        guardY = input.indexOfFirst { it.contains("^") }
        guardX = input[guardY].indexOfFirst { it == '^' }
        guardDirection = Day6Direction.UP
        obstacles = mutableSetOf()
        guardVisited = mutableSetOf(guardX to guardY)

        for (y in input.indices) {
            for (x in input[y].indices) {
                if (input[y][x] == '#') {
                    obstacles.add(x to y)
                }
            }
        }
    }

    private fun step(): Boolean {
        guardVisited.add(guardX to guardY)

        var nextX = guardX + guardDirection.x
        var nextY = guardY + guardDirection.y

        while (obstacles.contains(nextX to nextY)) {
            guardDirection = guardDirection.rotate()
            nextX = guardX + guardDirection.x
            nextY = guardY + guardDirection.y
        }

        if (nextX < 0 || nextX >= width || nextY < 0 || nextY >= height) {
            return false
        }

        guardX = nextX
        guardY = nextY

        return true
    }

    override fun solvePart1(): String {
        init()
        while (step()) {
            continue
        }
        return guardVisited.size.toString()
    }

    override fun solvePart2(): String {
        var loopCount = 0

        for (y in input.indices) {
            for (x in input[y].indices) {
                init()

                if (obstacles.contains(x to y)) continue
                if (x == guardX && y == guardY) continue

                obstacles.add(x to y)

                val visited: MutableSet<Triple<Int, Int, Day6Direction>> = mutableSetOf(Triple(guardX, guardY, guardDirection))

                while (true) {
                    if (!step()) {
                        break
                    }
                    if (visited.contains(Triple(guardX, guardY, guardDirection))) {
                        loopCount += 1
                        break
                    }
                    visited.add(Triple(guardX, guardY, guardDirection))
                }
            }
        }

        return loopCount.toString()
    }
}

fun main() {
    Day06(
        inputFile(
            """
            ....#.....
            .........#
            ..........
            ..#.......
            .......#..
            ..........
            .#..^.....
            ........#.
            #.........
            ......#...
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "41", part2ExpectedSolution = "6")

    Day06(downloadInput(2024, 6)).run()
}
