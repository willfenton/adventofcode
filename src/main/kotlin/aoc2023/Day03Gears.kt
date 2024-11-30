package aoc2023

import InputParser
import Solver
import kotlin.math.abs

// https://adventofcode.com/2023/day/3

data class Point(val x: Int, val y: Int)

data class PartNumber(val number: Int, val points: List<Point>)

data class Symbol(val character: String, val point: Point)

class Day03Gears(val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)
    private val partNumbers: List<PartNumber> = parsePartNumbers()
    private val symbols: List<Symbol> = parseSymbols()

    override fun solvePart1(): String {
        val validPartNumbers = partNumbers.filter { (_, points) ->
            points.any { p1 ->
                symbols.any { (_, p2) ->
                    val xDist = abs(p1.x - p2.x)
                    val yDist = abs(p1.y - p2.y)
                    xDist <= 1 && yDist <= 1
                }
            }
        }
        return validPartNumbers.sumOf { it.number }.toString()
    }

    override fun solvePart2(): String {
        var gearRatioSum = 0

        symbols.filter { it.character == "*" }.forEach { (_, p1) ->
            val adjacentNumbers = partNumbers.filter { (_, points) ->
                points.any { p2 ->
                    val xDist = abs(p1.x - p2.x)
                    val yDist = abs(p1.y - p2.y)
                    xDist <= 1 && yDist <= 1
                }
            }
            if (adjacentNumbers.size == 2) {
                val gearRatio = adjacentNumbers[0].number * adjacentNumbers[1].number
                gearRatioSum += gearRatio
            }
        }

        return gearRatioSum.toString()
    }

    private fun parsePartNumbers() = input
        .mapIndexed { y, line ->
            Regex("([0-9]+)").findAll(line).toList().map { match ->
                val group = match.groups.first()
                PartNumber(
                    number = group!!.value.toInt(),
                    points = group.range.map { x -> Point(x, y) }
                )
            }
        }.flatten()

    private fun parseSymbols() = input
        .mapIndexed { y, line ->
            Regex("([^0-9.])").findAll(line).toList().map { match ->
                val group = match.groups.first()
                Symbol(
                    character = group!!.value,
                    point = Point(group.range.first, y)
                )
            }
        }.flatten()
}

fun main() {
    Day03Gears("input/2023/03.test.txt").run(part1ExpectedSolution = "4361", part2ExpectedSolution = "467835")
    Day03Gears("input/2023/03.txt").run()
}
