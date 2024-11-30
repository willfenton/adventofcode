package aoc2021

import InputParser
import Solver

// https://adventofcode.com/2021/day/5

class Day05HydrothermalVenture(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)
    private val lines =
        input.map { line ->
            val (x, y) =
                line
                    .split(" -> ")
                    .map { pointString -> pointString.split(",") }
                    .map { (x, y) -> Point(x.toInt(), y.toInt()) }
            Line(x, y)
        }

    override fun solvePart1(): String {
        val verticalOrHorizontalLines = lines.filter { line -> line.isVerticalOrHorizontal }
        val pointCounts =
            verticalOrHorizontalLines
                .map { it.points }
                .flatten()
                .groupingBy { it }
                .eachCount()
        return pointCounts.count { (point, count) -> count >= 2 }.toString()
    }

    override fun solvePart2(): String {
        val pointCounts =
            lines
                .map { it.points }
                .flatten()
                .groupingBy { it }
                .eachCount()
        return pointCounts.count { (point, count) -> count >= 2 }.toString()
    }
}

data class Line(val p1: Point, val p2: Point) {
    val points: Set<Point>
    val isVerticalOrHorizontal = p1.x == p2.x || p1.y == p2.y

    init {
        val xRange = if (p1.x <= p2.x) p1.x..p2.x else p1.x downTo p2.x
        val yRange = if (p1.y <= p2.y) p1.y..p2.y else p1.y downTo p2.y
        points =
            when {
                p1.x == p2.x -> {
                    yRange.map { y -> Point(p1.x, y) }
                }

                p1.y == p2.y -> {
                    xRange.map { x -> Point(x, p1.y) }
                }

                else -> {
                    xRange.zip(yRange).map { (x, y) -> Point(x, y) }
                }
            }.toSet()
    }
}

fun main() {
    Solver.run { Day05HydrothermalVenture("input/2021/05.test.txt") }
    Solver.run { Day05HydrothermalVenture("input/2021/05.txt") }
}
