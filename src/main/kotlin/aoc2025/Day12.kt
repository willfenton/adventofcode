package aoc2025

import Position
import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2025/day/12

data class Day12Shape(val shapeIndex: Int, val tiles: Set<Position>) {
    val permutations: MutableSet<Set<Position>> = mutableSetOf()

    init {
        fun flipX(tiles: Set<Position>): Set<Position> {
            return tiles.map { Position(2 - it.x, it.y) }.toSet()
        }
        fun flipY(tiles: Set<Position>): Set<Position> {
            return tiles.map { Position(it.x, 2 - it.y) }.toSet()
        }
        fun rotate90(tiles: Set<Position>): Set<Position> {
            return tiles.map { Position(it.y, 2 - it.x) }.toSet()
        }

        val base = tiles
        val r90 = rotate90(base)
        val r180 = rotate90(r90)
        val r270 = rotate90(r180)

        listOf(base, r90, r180, r270).forEach { tiles ->
            permutations.add(tiles)
            permutations.add(flipX(tiles))
            permutations.add(flipY(tiles))
        }
    }
}

data class Day12Region(val width: Int, val height: Int, val shapeCounts: Map<Int, Int>)

class Day12(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val shapes = mutableListOf<Day12Shape>()
    private val regions = mutableListOf<Day12Region>()

    init {
        val lastBlankLineIndex = input.indexOfLast { it.isBlank() }
        val shapeLines = input.subList(0, lastBlankLineIndex)
        val regionLines = input.subList(lastBlankLineIndex + 1, input.size)

        for (shapeChunk in shapeLines.chunked(5)) {
            val tiles = mutableSetOf<Position>()
            for (y in 0..2) {
                for (x in 0..2) {
                    if (shapeChunk[y + 1][x] == '#') {
                        tiles.add(Position(x, y))
                    }
                }
            }
            shapes.add(Day12Shape(shapes.size, tiles))
        }

        for (line in regionLines) {
            val width = line.substringBefore("x").toInt()
            val height = line.substringAfter("x").substringBefore(":").toInt()
            val shapeCounts = line.substringAfter(": ").split(" ").map { it.toInt() }.withIndex().associate { it.index to it.value }
            regions.add(Day12Region(width, height, shapeCounts))
        }
    }

    override fun solvePart1(): String {
        return regions.count { isValidRegion(it.width, it.height, emptySet(), it.shapeCounts) }.toString()
    }

    private fun isValidRegion(width: Int, height: Int, filled: Set<Position>, shapeCounts: Map<Int, Int>): Boolean {
        if (filled.isEmpty()) {
            val naiveSpaces = (width / 3) * (height / 3)
            if (naiveSpaces > shapeCounts.values.sum()) {
                return true
            }
        }

        val remainingEmptySpaces = (width * height) - filled.size
        val remainingSpacesRequired = shapeCounts.map { (i, count) -> shapes[i].tiles.size * count }.sum()
        if (remainingSpacesRequired > remainingEmptySpaces) {
            return false
        }

        if (shapeCounts.values.all { it == 0 }) return true

        val (shapeIndex, count) = shapeCounts.entries.firstOrNull { it.value > 0 } ?: return true
        val shape = shapes[shapeIndex]

        for (y in 0 until height) {
            for (x in 0 until width) {
                for (permutation in shape.permutations) {
                    val positions = permutation.map { Position(it.x + x, it.y + y) }.toSet()
                    if (positions.all { it.x < width && it.y < height && it !in filled }) {
                        if (isValidRegion(width, height, filled + positions, shapeCounts + (shapeIndex to count - 1))) {
                            return true
                        }
                    }
                }
            }
        }
        return false
    }

    override fun solvePart2(): String {
        return "1"
    }
}

fun main() {
    Day12(
        inputFile(
            """
            0:
            ###
            ##.
            ##.
            
            1:
            ###
            ##.
            .##
            
            2:
            .##
            ###
            ##.
            
            3:
            ##.
            ###
            ##.
            
            4:
            ###
            #..
            ###
            
            5:
            ###
            .#.
            ###
            
            4x4: 0 0 0 0 2 0
            12x5: 1 0 1 0 2 2
            12x5: 1 0 1 0 3 2
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "2", part2ExpectedSolution = null)

    Day12(downloadInput(2025, 12)).run(part1ExpectedSolution = null, part2ExpectedSolution = null)
}
