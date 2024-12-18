package aoc2024

import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/13

data class LongPosition(val x: Long, val y: Long) {
    operator fun plus(other: LongPosition): LongPosition = LongPosition(this.x + other.x, this.y + other.y)

    operator fun minus(other: LongPosition): LongPosition = LongPosition(this.x - other.x, this.y - other.y)

    operator fun times(mult: Long): LongPosition = LongPosition(this.x * mult, this.y * mult)
}

data class ClawMachine(val prize: LongPosition, val buttonA: LongPosition, val buttonB: LongPosition)

class Day13(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString()).filter { it.isNotBlank() }

    private val clawMachines: MutableList<ClawMachine> = mutableListOf()

    init {
        val chunks = input.chunked(3)
        for (chunk in chunks) {
            val buttonA = chunk[0].substringAfter("Button A: ").split(", ")
            val positionA = LongPosition(x = buttonA[0].substringAfter("X+").toLong(), y = buttonA[1].substringAfter("Y+").toLong())

            val buttonB = chunk[1].substringAfter("Button B: ").split(", ")
            val positionB = LongPosition(x = buttonB[0].substringAfter("X+").toLong(), y = buttonB[1].substringAfter("Y+").toLong())

            val prize = chunk[2].substringAfter("Prize: ").split(", ")
            val prizePosition = LongPosition(x = prize[0].substringAfter("X=").toLong(), y = prize[1].substringAfter("Y=").toLong())

            clawMachines.add(ClawMachine(prizePosition, positionA, positionB))
        }
    }

    override fun solvePart1(): String = clawMachines
        .mapNotNull { solveClawMachine(it) }
        .sumOf { solutionCost(it) }
        .toString()

    override fun solvePart2(): String {
        val extraDistance = LongPosition(10000000000000, 10000000000000)
        val clawMachines = clawMachines.map { it.copy(prize = it.prize + extraDistance) }
        return clawMachines
            .mapNotNull { solveClawMachine(it) }
            .sumOf { solutionCost(it) }
            .toString()
    }

    private fun solveClawMachine(clawMachine: ClawMachine): Pair<Long, Long>? {
        val (prize, buttonA, buttonB) = clawMachine

        val aPresses = (prize.y * buttonB.x - prize.x * buttonB.y) / (buttonA.y * buttonB.x - buttonA.x * buttonB.y)
        val bPresses = (prize.x - (buttonA.x * aPresses)) / buttonB.x

        return if ((buttonA * aPresses) + (buttonB * bPresses) == prize) {
            Pair(aPresses, bPresses)
        } else {
            null
        }
    }

    private fun solutionCost(solution: Pair<Long, Long>): Long {
        val (aPresses, bPresses) = solution
        val cost = (aPresses * 3) + (bPresses * 1)
        return cost
    }
}

fun main() {
    Day13(
        inputFile(
            """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400
            
            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176
            
            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450
            
            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "480", part2ExpectedSolution = null)

    Day13(downloadInput(2024, 13)).run()
}
