package aoc2024

import Grid
import Position
import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/14

data class Robot(var position: Position, val velocity: Position, val grid: Grid<Int>) {
    fun step() {
        val next = this.position + this.velocity

        while (next.x < 0) {
            next.x += grid.width
        }
        while (next.y < 0) {
            next.y += grid.height
        }

        next.x %= grid.width
        next.y %= grid.height

        this.position = next
    }
}

class Day14(file: Path, val width: Int = 101, val height: Int = 103) : Solver {
    private val input = InputParser.parseLines(file.toString())

    // data doesn't matter
    private var grid: Grid<Int> = Grid()

    private var robots: MutableList<Robot> = mutableListOf()

    fun init() {
        grid = Grid()
        robots = mutableListOf()

        // grid
        for (x in 0..<width) {
            for (y in 0..<height) {
                grid[Position(x, y)] = 0
            }
        }
        grid.init()

        // robots
        for (line in input) {
            val (p, v) = line.split(" ").map { it.substringAfter("p=").substringAfter("v=") }
            val (px, py) = p.split(",").map { it.toInt() }
            val (vx, vy) = v.split(",").map { it.toInt() }
            val position = Position(px, py)
            val velocity = Position(vx, vy)
            val robot = Robot(position, velocity, grid)
            robots.add(robot)
        }
    }

    private fun printGrid() {
        for (y in 0..<grid.height) {
            for (x in 0..<grid.width) {
                val robotsInSquare = robots.count { it.position == Position(x, y) }
                print(
                    when (robotsInSquare) {
                        0 -> "."
                        else -> robotsInSquare.toString()
                    }
                )
            }
            println()
        }
        println()
    }

    override fun solvePart1(): String {
        init()
        repeat(100) {
            robots.forEach { it.step() }
        }

        val midX = grid.width / 2
        val midY = grid.height / 2

        val xFilters: List<(Position) -> Boolean> = listOf(
            { pos -> pos.x < midX },
            { pos -> pos.x > midX }
        )
        val yFilters: List<(Position) -> Boolean> = listOf(
            { pos -> pos.y < midY },
            { pos -> pos.y > midY }
        )

        val quadrants = xFilters.flatMap { xFilter ->
            yFilters.map { yFilter ->
                xFilter to yFilter
            }
        }

        val quadrantCounts = quadrants.map { (xFilter, yFilter) ->
            robots.count { xFilter(it.position) && yFilter(it.position) }
        }

        return quadrantCounts.reduce { acc, next -> acc * next }.toString()
    }

    override fun solvePart2(): String {
        init()

        var biggestGroup = 0
        var biggestGroupIteration = 0

        repeat(10000) { i ->
            robots.forEach { it.step() }
            val robotPositions = robots.map { it.position }.toSet()
            val groupSizes = mutableListOf<Int>()

            // flood fill connected robots and look for the biggest group, that's probably the Christmas tree
            for (robot in robots) {
                val visited: MutableSet<Position> = mutableSetOf(robot.position)
                val queue: MutableList<Position> = mutableListOf(robot.position)

                while (queue.isNotEmpty()) {
                    val current = queue.removeFirst()

                    val unvisitedNeighbors = current
                        .neighbors()
                        .filter { it in robotPositions }
                        .filter { it !in visited }

                    for (neighbor in unvisitedNeighbors) {
                        queue.add(neighbor)
                        visited.add(neighbor)
                    }
                }

                groupSizes.add(visited.size)
            }

            val groupSize = groupSizes.max()
            if (groupSize > biggestGroup) {
                println("Iteration $i")
                println("Size=$groupSize (previous=$biggestGroup)")
                printGrid()

                biggestGroup = groupSize
                biggestGroupIteration = i
            }
        }

        return (biggestGroupIteration + 1).toString()
    }
}

fun main() {
    Day14(
        inputFile(
            """
            p=0,4 v=3,-3
            p=6,3 v=-1,-3
            p=10,3 v=-1,2
            p=2,0 v=2,-1
            p=0,0 v=1,3
            p=3,0 v=-2,-2
            p=7,6 v=-1,-3
            p=3,0 v=-1,-2
            p=9,3 v=2,3
            p=7,3 v=-1,2
            p=2,4 v=2,-3
            p=9,5 v=-3,-3
            """.trimIndent()
        ),
        width = 11,
        height = 7
    ).run(part1ExpectedSolution = "12", part2ExpectedSolution = null)

    Day14(downloadInput(2024, 14)).run()
}
