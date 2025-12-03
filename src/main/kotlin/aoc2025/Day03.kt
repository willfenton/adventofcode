package aoc2025

import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2025/day/3

class Day03(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val banks = input.map { it.chunked(1).toList() }

    override fun solvePart1(): String = banks.sumOf { bank -> maxBankJoltage(bank, 2) }.toString()

    override fun solvePart2(): String = banks.sumOf { bank -> maxBankJoltage(bank, 12) }.toString()

    private fun maxBankJoltage(bank: List<String>, numDigits: Int): Long {
        var currentBestIndices = bank.indices.take(numDigits)
        var currentBestSum = getSum(bank, currentBestIndices)

        for (i in numDigits..<bank.size) {
            var newBestSum = 0L
            var newBestIndices = mutableListOf<Int>()
            for (j in currentBestIndices.indices) {
                val newIndices = currentBestIndices.toMutableList()
                newIndices.removeAt(j)
                newIndices.add(i)
                val newSum = getSum(bank, newIndices)
                if (newSum > newBestSum) {
                    newBestSum = newSum
                    newBestIndices = newIndices
                }
            }
            if (newBestSum > currentBestSum) {
                currentBestSum = newBestSum
                currentBestIndices = newBestIndices
            }
        }

        return currentBestSum
    }

    private fun getSum(bank: List<String>, indices: List<Int>): Long = indices.joinToString("") { bank[it] }.toLong()
}

fun main() {
    Day03(
        inputFile(
            """
            987654321111111
            811111111111119
            234234234234278
            818181911112111
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "357", part2ExpectedSolution = "3121910778619")

    Day03(downloadInput(2025, 3)).run(part1ExpectedSolution = "17107", part2ExpectedSolution = null)
}
