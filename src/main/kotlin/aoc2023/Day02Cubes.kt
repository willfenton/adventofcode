package aoc2023

import InputParser
import Solver

// https://adventofcode.com/2023/day/2

data class Game(val id: Int, val numberColourPairs: List<NumberColourPair>)

data class NumberColourPair(val number: Int, val colour: String)

class Day02Cubes(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)
    private val games = parseGames()

    override fun solvePart1(): String {
        val counts = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
        )
        val legalGames = games.filter { game ->
            game.numberColourPairs.all { (number, colour) -> number <= counts[colour]!! }
        }
        return legalGames.sumOf { game -> game.id }.toString()
    }

    override fun solvePart2(): String = games.sumOf { game ->
        val minRed = game.numberColourPairs.filter { pair -> pair.colour == "red" }.maxOf { pair -> pair.number }
        val minGreen = game.numberColourPairs.filter { pair -> pair.colour == "green" }.maxOf { pair -> pair.number }
        val minBlue = game.numberColourPairs.filter { pair -> pair.colour == "blue" }.maxOf { pair -> pair.number }
        minRed * minGreen * minBlue
    }.toString()

    private fun parseGames(): List<Game> = input.map { line -> Game(
        id = line.substringBefore(":").substringAfter(" ").toInt(),
        numberColourPairs = line
            .substringAfter(":")
            .trim()
            .split("; ", ", ")
            .map { numberColourPair -> NumberColourPair(
                number = numberColourPair.substringBefore(" ").toInt(),
                colour = numberColourPair.substringAfter(" ")
            )}
    )}
}

fun main() {
    Day02Cubes("input/2023/02.test.txt").run(part1ExpectedSolution = "8", part2ExpectedSolution = "2286")
    Day02Cubes("input/2023/02.txt").run()
}
