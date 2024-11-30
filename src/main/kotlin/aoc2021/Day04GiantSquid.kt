package aoc2021

import InputParser
import Solver

// https://adventofcode.com/2021/day/4

class Day04GiantSquid(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    private val numbers: List<Int> = input.first().split(",").map { it.toInt() }
    private val boards = mutableListOf<Board>()

    init {
        var boardStartIndex = 2
        while (boardStartIndex < input.size) {
            val boardLines = input.slice(boardStartIndex..boardStartIndex + 4)
            boards.add(Board(boardLines))
            boardStartIndex += 6
        }
    }

    override fun solvePart1(): String {
        val numbersDrawn = mutableSetOf<Int>()

        for (number in numbers) {
            numbersDrawn.add(number)
            for (board in boards) {
                if (board.isWinning(numbersDrawn)) {
                    return board.score(numbersDrawn, number).toString()
                }
            }
        }

        return ""
    }

    override fun solvePart2(): String {
        var boardsToCheck = boards.toList()
        val numbersDrawn = mutableSetOf<Int>()

        for (number in numbers) {
            numbersDrawn.add(number)
            if (boardsToCheck.size == 1) {
                val board = boardsToCheck.first()
                if (board.isWinning(numbersDrawn)) {
                    return board.score(numbersDrawn, number).toString()
                }
            } else {
                boardsToCheck = boardsToCheck.filterNot { board -> board.isWinning(numbersDrawn) }
            }
        }

        return ""
    }
}

class Board(input: List<String>) {
    private val board: List<List<Int>> =
        input.map { it.split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() } }
    private val numbers = board.flatten()
    private val rows = board.map { row -> row.toSet() }
    private val columns = (0..4).map { col -> board.map { row -> row[col] }.toSet() }
    private val lines = rows + columns

    fun isWinning(numbersDrawn: Set<Int>): Boolean = lines.any { line -> numbersDrawn.containsAll(line) }

    fun score(numbersDrawn: Set<Int>, lastCalled: Int): Int =
        lastCalled * numbers.filterNot { number -> numbersDrawn.contains(number) }.sum()
}

fun main() {
    Solver.run { Day04GiantSquid("input/2021/04.test.txt") }
    Solver.run { Day04GiantSquid("input/2021/04.txt") }
}
