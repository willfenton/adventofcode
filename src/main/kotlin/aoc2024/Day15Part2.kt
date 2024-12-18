package aoc2024

import Direction
import Grid
import Position
import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/15

data class Box(val left: Position, val right: Position) {
    operator fun plus(other: Position): Box = Box(this.left + other, this.right + other)
}

class Day15Part2(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private lateinit var grid: Grid<Boolean>
    private lateinit var robotPosition: Position
    private lateinit var boxes: MutableSet<Box>
    private lateinit var instructions: MutableList<Direction>

    private fun init() {
        grid = Grid()
        boxes = mutableSetOf()
        instructions = mutableListOf()

        val gridLines = input.takeWhile { it.isNotBlank() }
        val instructionLines = input.takeLastWhile { it.isNotBlank() }

        for (y in gridLines.indices) {
            for (x in gridLines[y].indices) {
                val leftX = x * 2
                val rightX = leftX + 1

                val leftPos = Position(leftX, y)
                val rightPos = Position(rightX, y)

                val c = gridLines[y][x]

                // true = wall
                grid[leftPos] = (c == '#')
                grid[rightPos] = (c == '#')

                if (c == 'O') {
                    boxes.add(Box(leftPos, rightPos))
                } else if (c == '@') {
                    robotPosition = leftPos
                }
            }
        }

        grid.init()

        for (line in instructionLines) {
            for (char in line) {
                when (char) {
                    '^' -> instructions.addLast(Direction.UP)
                    '>' -> instructions.addLast(Direction.RIGHT)
                    '<' -> instructions.addLast(Direction.LEFT)
                    'v' -> instructions.addLast(Direction.DOWN)
                    else -> TODO()
                }
            }
        }
    }

    private fun printGrid() {
        for (y in 0..<grid.height) {
            for (x in 0..<grid.width) {
                val pos = Position(x, y)
                val tile = grid[pos]!!
                print(
                    when {
                        robotPosition == pos -> '@'
                        tile.data -> '#'
                        boxes.find { it.left == pos } != null -> '['
                        boxes.find { it.right == pos } != null -> ']'
                        else -> '.'
                    }
                )
            }
            println()
        }
        println()
    }

    private fun boxGpsCoordinate(box: Box): Int = (100 * box.left.y) + box.left.x

    override fun solvePart1(): String = ""

    override fun solvePart2(): String {
        init()

        while (instructions.isNotEmpty()) {
            val instruction = instructions.removeFirst()
            val nextRobotPosition = robotPosition + instruction.position
            val nextIsWall = grid[nextRobotPosition]!!.data
            val nextBox = boxes.find { it.left == nextRobotPosition || it.right == nextRobotPosition }

            println("Instruction: ${instruction.name}")
            // printGrid()

            if (nextIsWall) {
                println("Blocked by a wall")
            } else if (nextBox == null) {
                println("Moving into an open space")
                robotPosition = nextRobotPosition
            } else {
                println("Tile in front is a box")
                val boxesBeingPushed = mutableSetOf(nextBox)

                // really inefficient but whatever
                var wallInFront = false
                while (true) {
                    val initialSize = boxesBeingPushed.size
                    val boxesToAdd = mutableSetOf<Box>()

                    for (box in boxesBeingPushed) {
                        val nextBoxPositions = listOf(box.left, box.right).map { it + instruction.position }.toSet()

                        wallInFront = nextBoxPositions.any { pos -> grid[pos]!!.data }
                        if (wallInFront) break

                        val nextBoxes = boxes.filter { it.left in nextBoxPositions || it.right in nextBoxPositions }
                        boxesToAdd.addAll(nextBoxes)
                    }

                    boxesBeingPushed.addAll(boxesToAdd)

                    if (wallInFront || boxesBeingPushed.size == initialSize) {
                        break
                    }
                }

                println("Boxes being pushed:")
                for (box in boxesBeingPushed) {
                    println(box)
                }

                if (wallInFront) {
                    println("Tile after boxes is a wall")
                } else {
                    println("No boxes being pushed have walls in front")
                    println("Pushing boxes")
                    val pushedBoxes = boxesBeingPushed.map { it + instruction.position }
                    boxes.removeAll(boxesBeingPushed)
                    boxes.addAll(pushedBoxes)
                    robotPosition = nextRobotPosition
                }
            }

            println()
            println()
        }

        println("Final state:")
        printGrid()

        return boxes.sumOf { boxGpsCoordinate(it) }.toString()
    }
}

fun main() {
    Day15Part2(
        inputFile(
            """
            #######
            #...#.#
            #.....#
            #..OO@#
            #..O..#
            #.....#
            #######
            
            <vv<<^^<<^^
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = null, part2ExpectedSolution = null)

    Day15Part2(
        inputFile(
            """
            ##########
            #..O..O.O#
            #......O.#
            #.OO..O.O#
            #..O@..O.#
            #O#..O...#
            #O..O..O.#
            #.OO.O.OO#
            #....O...#
            ##########
            
            <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
            vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
            ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
            <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
            ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
            ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
            >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
            <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
            ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
            v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = null, part2ExpectedSolution = "9021")

    Day15Part2(downloadInput(2024, 15)).run()
}
