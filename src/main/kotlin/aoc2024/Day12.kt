package aoc2024

import Direction
import Grid
import GridCell
import Position
import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/12

class Day12(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val grid = Grid<Char>()
    private val bigGrid = Grid<Char>()

    private val regions: MutableSet<Set<GridCell<Char>>> = mutableSetOf()

    private val isConnected: (cell: GridCell<Char>, other: GridCell<Char>) -> Boolean = { cell, other -> other.data == cell.data }

    init {
        for (y in input.indices) {
            for (x in input[y].indices) {
                grid[Position(x, y)] = input[y][x]
            }
        }

        for (cell in grid.cells.values) {
            val bigCellPos = Position(cell.pos.x * 3, cell.pos.y * 3)
            bigGrid[bigCellPos] = cell.data
            for (direction in Direction.ALL) {
                bigGrid[bigCellPos + direction.position] = cell.data
            }
        }

        for (cell in grid.cells.values) {
            if (cell.data == '.') {
                continue
            }
            if (regions.any { region -> cell in region }) {
                continue
            }
            regions.add(floodFillRegion(cell))
        }

        println("Grid:")
        grid.init()
        for (y in grid.minY..grid.maxY) {
            for (x in grid.minX..grid.maxX) {
                print(grid[Position(x, y)]!!.data)
            }
            println()
        }

        println("Big grid:")
        bigGrid.init()
        for (y in bigGrid.minY..bigGrid.maxY) {
            for (x in bigGrid.minX..bigGrid.maxX) {
                print(bigGrid[Position(x, y)]!!.data)
            }
            println()
        }
    }

    override fun solvePart1(): String = regions.sumOf { scoreRegionP1(it) }.toString()

    override fun solvePart2(): String = regions.sumOf { scoreRegionP2(it) }.toString()

    private fun floodFillRegion(start: GridCell<Char>): Set<GridCell<Char>> {
        val neighborDirections = Direction.CARDINAL

        val visited: MutableSet<GridCell<Char>> = mutableSetOf(start)
        val queue: MutableList<GridCell<Char>> = mutableListOf(start)

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()

            val connectedNeighbors = current.connectedNeighbors(neighborDirections, isConnected)
            val unvisitedNeighbors = connectedNeighbors.filter { it !in visited }

            for (neighbor in unvisitedNeighbors) {
                queue.add(neighbor)
                visited.add(neighbor)
            }
        }

        return visited
    }

    private fun scoreRegionP1(region: Set<GridCell<Char>>): Int {
        val allAdjacentPositions: List<Position> = region
            .flatMap { cell -> cell.pos.neighbors(Direction.CARDINAL) }
            .filterNot { pos -> region.any { it.pos == pos } }

        val area = region.size
        val perimeterLength = allAdjacentPositions.size

        return area * perimeterLength
    }

    private fun scoreRegionP2(region: Set<GridCell<Char>>): Int {
        val arbitraryCell = region.first()
        val bigGridCell = bigGrid[Position(arbitraryCell.pos.x * 3, arbitraryCell.pos.y * 3)]!!
        val bigGridRegion = floodFillRegion(bigGridCell)

        val allAdjacentPositions: List<Position> = bigGridRegion
            .flatMap { cell -> cell.pos.neighbors(Direction.CARDINAL) }
            .filterNot { pos -> bigGridRegion.any { it.pos == pos } }

        var horizontalSides = 0
        var verticalSides = 0

        // all adjacent positions that are directly above or below a position in the region
        val horizontalCandidates = allAdjacentPositions
            .filter { pos ->
                bigGridRegion.any { it.pos.neighbor(Direction.UP) == pos || it.pos.neighbor(Direction.DOWN) == pos }
            }.toMutableSet()
        // all adjacent positions that are directly left or right of a position in the region
        val verticalCandidates = allAdjacentPositions
            .filter { pos ->
                bigGridRegion.any { it.pos.neighbor(Direction.LEFT) == pos || it.pos.neighbor(Direction.RIGHT) == pos }
            }.toMutableSet()

        while (horizontalCandidates.isNotEmpty()) {
            val candidate = horizontalCandidates.random()
            horizontalCandidates.remove(candidate)

            horizontalSides += 1

            var left = candidate.neighbor(Direction.LEFT)
            while (left in horizontalCandidates) {
                horizontalCandidates.remove(left)
                left = left.neighbor(Direction.LEFT)
            }

            var right = candidate.neighbor(Direction.RIGHT)
            while (right in horizontalCandidates) {
                horizontalCandidates.remove(right)
                right = right.neighbor(Direction.RIGHT)
            }
        }

        while (verticalCandidates.isNotEmpty()) {
            val candidate = verticalCandidates.random()
            verticalCandidates.remove(candidate)

            verticalSides += 1

            var up = candidate.neighbor(Direction.UP)
            while (up in verticalCandidates) {
                verticalCandidates.remove(up)
                up = up.neighbor(Direction.UP)
            }

            var down = candidate.neighbor(Direction.DOWN)
            while (down in verticalCandidates) {
                verticalCandidates.remove(down)
                down = down.neighbor(Direction.DOWN)
            }
        }

        val area = region.size
        val numSides = horizontalSides + verticalSides

        return area * numSides
    }
}

fun main() {
    Day12(
        inputFile(
            """
            A
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = null, part2ExpectedSolution = "4")

    Day12(
        inputFile(
            """
            AAAA
            BBCD
            BBCC
            EEEC
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "140", part2ExpectedSolution = "80")

    Day12(
        inputFile(
            """
            EEEEE
            EXXXX
            EEEEE
            EXXXX
            EEEEE
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = null, part2ExpectedSolution = "236")

    Day12(
        inputFile(
            """
            OOOOO
            OXOXO
            OOOOO
            OXOXO
            OOOOO
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = null, part2ExpectedSolution = "436")

    Day12(
        inputFile(
            """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "1930", part2ExpectedSolution = null)

    Day12(downloadInput(2024, 12)).run()
}
