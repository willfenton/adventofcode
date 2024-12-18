package aoc2024

import Direction
import Grid
import Position
import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/15

class Day15(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private lateinit var grid: Grid<Boolean>
    private lateinit var robotPosition: Position
    private lateinit var boxes: MutableSet<Position>
    private lateinit var instructions: MutableList<Direction>

    private fun init() {
        grid = Grid()
        boxes = mutableSetOf()
        instructions = mutableListOf()

        val gridLines = input.takeWhile { it.isNotBlank() }
        val instructionLines = input.takeLastWhile { it.isNotBlank() }

        for (y in gridLines.indices) {
            for (x in gridLines[y].indices) {
                val pos = Position(x, y)
                val c = gridLines[y][x]

                // true = wall
                grid[pos] = (c == '#')

                if (c == 'O') {
                    boxes.add(pos)
                } else if (c == '@') {
                    robotPosition = pos
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
                        pos in boxes -> 'O'
                        tile.data -> '#'
                        else -> '.'
                    }
                )
            }
            println()
        }
        println()
    }

    private fun boxGpsCoordinate(boxPosition: Position): Int = (100 * boxPosition.y) + boxPosition.x

    override fun solvePart1(): String {
        init()

        while (instructions.isNotEmpty()) {
            val instruction = instructions.removeFirst()
            val nextRobotPosition = robotPosition + instruction.position
            val nextIsWall = grid[nextRobotPosition]!!.data

            println("Instruction: ${instruction.name}")
//            printGrid()

            if (nextIsWall) {
                println("Blocked by a wall")
            } else if (nextRobotPosition !in boxes) {
                println("Moving into an open space")
                robotPosition = nextRobotPosition
            } else {
                println("Tile in front is a box")
                val boxChain = mutableListOf(nextRobotPosition.copy())
                var nextBoxPosition = nextRobotPosition + instruction.position
                while (nextBoxPosition in boxes) {
                    boxChain.add(nextBoxPosition.copy())
                    nextBoxPosition += instruction.position
                }

                println("Box chain (front to back):")
                for (box in boxChain) {
                    println(box)
                }

                val tileAfterBoxesIsWall = grid[nextBoxPosition]!!.data
                if (tileAfterBoxesIsWall) {
                    println("Tile after boxes is a wall")
                } else {
                    println("Tile after boxes is an open space")
                    println("Pushing boxes")
                    val firstBox = boxChain.first()
                    boxes.remove(firstBox)
                    boxes.add(nextBoxPosition)
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

    override fun solvePart2(): String {
        init()

        // abc
        return ""
    }
}

fun main() {
    Day15(
        inputFile(
            """
            ########
            #..O.O.#
            ##@.O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########
            
            <^^>>>vv<v>>v<<
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "2028", part2ExpectedSolution = null)

    Day15(
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
    ).run(part1ExpectedSolution = "10092", part2ExpectedSolution = null)

    Day15(downloadInput(2024, 15)).run()
}
