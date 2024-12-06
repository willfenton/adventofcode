package aoc2024

import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/4

class Day04(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val horizontalLength = input.first().length
    private val verticalLength = input.size

    override fun solvePart1(): String {
        val horizontalLength = input.first().length
        val verticalLength = input.size

        val horizontals = input
        val verticals = input.first().indices.map { i ->
            input.map { it[i] }.joinToString("")
        }

        val diagonals = mutableListOf<String>()

        for (startX in 0..<horizontalLength) {
            val chars = mutableListOf<Char>()
            var x = startX
            var y = 0
            while (x < horizontalLength && y < verticalLength) {
                chars.add(input[y][x])
                x += 1
                y += 1
            }
            diagonals.add(chars.joinToString(""))
        }

        for (startY in 1..<verticalLength) {
            val chars = mutableListOf<Char>()
            var x = 0
            var y = startY
            while (x < horizontalLength && y < verticalLength) {
                chars.add(input[y][x])
                x += 1
                y += 1
            }
            diagonals.add(chars.joinToString(""))
        }

        for (startX in horizontalLength - 1 downTo 0) {
            val chars = mutableListOf<Char>()
            var x = startX
            var y = 0
            while (x >= 0 && y < verticalLength) {
                chars.add(input[y][x])
                x -= 1
                y += 1
            }
            diagonals.add(chars.joinToString(""))
        }

        for (startY in 1..<verticalLength) {
            val chars = mutableListOf<Char>()
            var x = horizontalLength - 1
            var y = startY
            while (x >= 0 && y < verticalLength) {
                chars.add(input[y][x])
                x -= 1
                y += 1
            }
            diagonals.add(chars.joinToString(""))
        }

        var allLines = horizontals + verticals + diagonals
        allLines = allLines + allLines.map { it.reversed() }

        var count = 0

        for (line in allLines) {
            count += line.windowed(4).count { it == "XMAS" }
        }

        return count.toString()
    }

    override fun solvePart2(): String {
        var count = 0
        for (x in 0..horizontalLength - 3) {
            for (y in 0..verticalLength - 3) {
                val diag1 = listOf(input[y][x], input[y + 1][x + 1], input[y + 2][x + 2]).joinToString("")
                val diag2 = listOf(input[y + 2][x], input[y + 1][x + 1], input[y][x + 2]).joinToString("")

                val match1 = (diag1 == "MAS" || diag1 == "SAM")
                val match2 = (diag2 == "MAS" || diag2 == "SAM")

                if (match1 && match2) count += 1
            }
        }

        return count.toString()
    }
}

fun main() {
    Day04(
        inputFile(
            """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "18", part2ExpectedSolution = "9")

    Day04(downloadInput(2024, 4)).run()
}
