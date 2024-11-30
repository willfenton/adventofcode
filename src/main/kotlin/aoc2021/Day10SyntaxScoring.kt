package aoc2021

import InputParser
import Solver

// https://adventofcode.com/2021/day/10

class CorruptedLineException(val lastToken: Char) : Exception()

class Day10SyntaxScoring(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    private val openTokens =
        mapOf(
            '(' to 1,
            '[' to 2,
            '{' to 3,
            '<' to 4
        )
    private val closeTokens =
        mapOf(
            ')' to 3,
            ']' to 57,
            '}' to 1197,
            '>' to 25137
        )
    private val mapping = openTokens.keys.zip(closeTokens.keys).associate { (open, close) -> open to close }
//    private val syntaxErrorScores =

    override fun solvePart1(): String {
        var totalSyntaxErrorScore = 0

        for (line in input) {
            try {
                parseSyntax(line)
            } catch (e: CorruptedLineException) {
                totalSyntaxErrorScore += closeTokens[e.lastToken]!!
            }
        }

        return totalSyntaxErrorScore.toString()
    }

    override fun solvePart2(): String {
        val autoCompleteScores = mutableListOf<Long>()

        for (line in input) {
            try {
                val score = parseSyntax(line)
                autoCompleteScores.add(score)
            } catch (e: CorruptedLineException) {
                continue
            }
        }

        autoCompleteScores.sort()

        println(autoCompleteScores)

        return autoCompleteScores[(autoCompleteScores.size - 1) / 2].toString()
    }

    // return completion score
    private fun parseSyntax(line: String): Long {
        val tokens = line.toList()

        val stack = mutableListOf<Char>()

        for (token in tokens) {
            if (openTokens.containsKey(token)) {
                stack.add(token)
            } else if (closeTokens.containsKey(token)) {
                val openToken = stack.removeLast()
                if (mapping[openToken]!! != token) {
                    throw CorruptedLineException(token)
                }
            }
        }

        var score: Long = 0
        while (stack.isNotEmpty()) {
            val token = stack.removeLast()
            score *= 5
            score += openTokens[token]!!
        }

        return score
    }
}

fun main() {
    Solver.run { Day10SyntaxScoring("input/2021/10.test.txt") }
    Solver.run { Day10SyntaxScoring("input/2021/10.txt") }
}
