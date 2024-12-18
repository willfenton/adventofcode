package aoc2024

import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/7

class Day07(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val equations: List<Pair<List<Long>, Long>> = input.map { line ->
        val split = line.split(": ")
        val result = split[0].toLong()
        val operands = split[1].split(" ").map { it.toLong() }
        Pair(operands, result)
    }

    override fun solvePart1(): String = equations
        .filter { (operands, result) -> isTrue(result, operands, part2 = false) }
        .sumOf { (operands, result) -> result }
        .toString()

    override fun solvePart2(): String = equations
        .filter { (operands, result) -> isTrue(result, operands, part2 = true) }
        .sumOf { (operands, result) -> result }
        .toString()

    private fun isTrue(result: Long, operands: List<Long>, part2: Boolean): Boolean {
        val operandsMutable = operands.toMutableList()
        val operand1 = operandsMutable.removeFirst()
        val operand2 = operandsMutable.removeFirst()

        val addition = operand1 + operand2
        val multiplication = operand1 * operand2
        val concatenation = (operand1.toString() + operand2.toString()).toLong()

        // base case
        if (operandsMutable.size == 0) {
            if ((addition == result) || (multiplication == result)) return true
            return if (part2) concatenation == result else false
        }

        // recursive addition
        if (isTrue(result, listOf(addition) + operandsMutable, part2)) return true

        // recursive multiplication
        if (isTrue(result, listOf(multiplication) + operandsMutable, part2)) return true

        return if (part2) {
            // recursive concatenation
            isTrue(result, listOf(concatenation) + operandsMutable, part2)
        } else {
            false
        }
    }
}

fun main() {
    Day07(
        inputFile(
            """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "3749", part2ExpectedSolution = "11387")

    Day07(downloadInput(2024, 7)).run()
}
