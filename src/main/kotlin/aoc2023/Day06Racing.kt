package aoc2023

import InputParser
import Solver

// https://adventofcode.com/2023/day/6

class Day06Racing(val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String {
        val times = input[0]
            .substringAfter(":")
            .trim()
            .split(Regex("\\s+"))
            .map { it.toLong() }
        val records = input[1]
            .substringAfter(":")
            .trim()
            .split(Regex("\\s+"))
            .map { it.toLong() }

        var result = 1
        for ((time, record) in times.zip(records)) {
            var waysToWin = 0
            for (speed in 0..time) {
                if (speedIsWinning(time, record, speed)) {
                    waysToWin += 1
                }
            }
            result *= waysToWin
        }

        return result.toString()
    }

    override fun solvePart2(): String {
        val time = input[0].substringAfter(":").replace(Regex("\\s+"), "").toLong()
        val record = input[1].substringAfter(":").replace(Regex("\\s+"), "").toLong()

        var arbitrarySpeedThatWins: Long = 0

        val samples = 1_000_000L
        for (i in 0..samples) {
            val speed = (time * (i.toDouble() / samples)).toLong()
            if (speedIsWinning(time, record, speed)) {
                println("Found a speed that breaks the record: $speed")
                arbitrarySpeedThatWins = speed
                break
            }
        }

        // binary search for first speed that wins
        var low: Long = 1
        var high: Long = arbitrarySpeedThatWins
        var firstWinningSpeed: Long = 0
        while (low <= high) {
            val speed = low + (high - low) / 2

            val lowWinning = speedIsWinning(time, record, speed - 1)
            val highWinning = speedIsWinning(time, record, speed)

            if (!lowWinning && highWinning) {
                firstWinningSpeed = speed
                break
            } else if (!lowWinning) {
                low = speed + 1
            } else if (highWinning) {
                high = speed - 1
            } else {
                throw Exception("shouldn't happen")
            }
        }
        println("First winning speed: $firstWinningSpeed")

        low = arbitrarySpeedThatWins + 1
        high = time
        var lastWinningSpeed: Long = 0
        while (low <= high) {
            val speed = low + (high - low) / 2

            val lowWinning = speedIsWinning(time, record, speed - 1)
            val highWinning = speedIsWinning(time, record, speed)

            if (lowWinning && !highWinning) {
                lastWinningSpeed = speed
                break
            } else if (lowWinning) {
                low = speed + 1
            } else if (!highWinning) {
                high = speed - 1
            } else {
                throw Exception("shouldn't happen")
            }
        }
        println("Last winning speed: $lastWinningSpeed")

        return (lastWinningSpeed - firstWinningSpeed).toString()
    }

    fun speedIsWinning(time: Long, record: Long, speed: Long): Boolean {
        val distanceTravelled = (time - speed) * speed
        return distanceTravelled > record
    }
}

fun main() {
    Day06Racing("input/2023/06.test.txt").run(part1ExpectedSolution = "288", part2ExpectedSolution = "71503")
    Day06Racing("input/2023/06.txt").run()
}
