package aoc2024

import Grid
import GridCell
import Position
import Solver
import downloadInput
import inputFile
import java.nio.file.Path
import kotlin.math.max

// https://adventofcode.com/2024/day/8

class Day08(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val grid: Grid<Char> = Grid()
    private val antennas: List<GridCell<Char>>
    private val antennaGroups: Map<Char, List<GridCell<Char>>>

    init {
        for (y in input.indices) {
            for (x in input.indices) {
                val char = input[y][x]
                grid[Position(x, y)] = char
            }
        }
        antennas = grid.cells.values.filter { it.data != '.' }
        antennaGroups = antennas.groupBy { it.data }
    }

    override fun solvePart1(): String {
        val antinodeLocations = mutableSetOf<Position>()

        for (group in antennaGroups) {
            for (antenna1 in group.value) {
                for (antenna2 in group.value) {
                    if (antenna1 == antenna2) continue

                    val diff = antenna1.pos - antenna2.pos
                    val antinode = antenna1.pos + diff

                    if (grid.cells.keys.contains(antinode)) {
                        antinodeLocations.add(antinode)
                    }
                }
            }
        }

        return antinodeLocations.size.toString()
    }

    override fun solvePart2(): String {
        val antinodeLocations = mutableSetOf<Position>()

        for (group in antennaGroups) {
            for (antenna1 in group.value) {
                for (antenna2 in group.value) {
                    if (antenna1 == antenna2) continue

                    var diff = antenna1.pos - antenna2.pos

                    // find GCD
                    var gcd = 1
                    for (i in 2..max(diff.x, diff.y)) {
                        if (diff.x % i == 0 && diff.y % i == 0) {
                            gcd = i
                        }
                    }
                    diff = Position(diff.x / gcd, diff.y / gcd)

                    antinodeLocations.add(antenna1.pos)
                    antinodeLocations.add(antenna2.pos)

                    var antinode = antenna1.pos + diff
                    while (grid.cells.keys.contains(antinode)) {
                        antinodeLocations.add(antinode)
                        antinode += diff
                    }

                    antinode = antenna1.pos - diff
                    while (grid.cells.keys.contains(antinode)) {
                        antinodeLocations.add(antinode)
                        antinode -= diff
                    }
                }
            }
        }

        return antinodeLocations.size.toString()
    }
}

fun main() {
    Day08(
        inputFile(
            """
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "14", part2ExpectedSolution = "34")

    Day08(downloadInput(2024, 8)).run()
}
