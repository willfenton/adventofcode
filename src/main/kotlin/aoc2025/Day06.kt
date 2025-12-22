package aoc2025

import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2025/day/6

class Day06(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val rows = input.map { it.split(" ").filter{ it.isNotBlank() }.map { it.trim() } }

    override fun solvePart1(): String {
        var total = 0L

        for (col in rows.first().indices) {
            val list = rows.map { it[col] }.toMutableList()
            val operator = list.removeLast()
            val operands = list.map { it.toLong() }
            val result: Long = when (operator) {
                "+" -> operands.sum()
                "*" -> operands.reduce { acc, l -> acc * l }
                else -> throw Exception()
            }
            total += result
        }

        return total.toString()
    }

    override fun solvePart2(): String {
        val operandLine = input.last()
        val operatorLines = input.take(input.size - 1)

        var total = 0L

        var operator: Char? = null
        val operands = mutableListOf<Long>()

        fun compute() {
            total += when (operator) {
                '+' -> operands.sum()
                '*' -> operands.reduce { acc, l -> acc * l }
                else -> 0
            }
        }

        fun addOperandAtColumn(i: Int) {
            val string = operatorLines.map { it[i] }.joinToString("").trim()
            if (string.isNotBlank()) {
                operands.add(string.toLong())
            }
        }

        for ((i, c) in operandLine.withIndex()) {
            if (c != ' ') {
                compute()
                operator = c
                operands.clear()
            }
            addOperandAtColumn(i)
        }

        compute()

        return total.toString()
    }
}

fun main() {
    Day06(
        inputFile(
            """
            123 328  51 64 
             45 64  387 23 
              6 98  215 314
            *   +   *   +  
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "4277556", part2ExpectedSolution = "3263827")

    Day06(downloadInput(2025, 6)).run(part1ExpectedSolution = "7326876294741", part2ExpectedSolution = "10756006415204")
}
