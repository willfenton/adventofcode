package aoc2025

import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2025/day/10

data class Machine(
    val targetState: List<Boolean>,
    val buttons: List<List<Int>>,
    val joltageRequirements: List<Int>,
    val currentState: List<Boolean>,
    val joltageCounters: List<Int>
) {
    fun pressAllButtons(): List<Machine> {
        return buttons.map { button ->
            val newState = currentState.toMutableList()
            for (i in button) {
                newState[i] = !newState[i]
            }
            this.copy(currentState = newState)
        }
    }

    fun pressAllJoltageButtonsWithPruning(): List<Machine> {
        return buttons.mapNotNull { button ->
            val newState = joltageCounters.toMutableList()
            var isValid = true
            for (i in button) {
                newState[i] = newState[i] + 1
                if (newState[i] > joltageRequirements[i]) {
                    isValid = false
                }
            }
            if (isValid) this.copy(joltageCounters = newState) else null
        }
    }

    fun isSolved(): Boolean = currentState == targetState

    fun isJoltageSolved(): Boolean = joltageCounters == joltageRequirements
}

class Day10(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val machines: List<Machine> = input.map { line ->
        val targetState = line.substringAfter("[").substringBefore("]").toCharArray().map { c -> when (c) {
            '.' -> false
            '#' -> true
            else -> throw IllegalArgumentException()
        } }

        val buttons = line
            .substringAfter("] ")
            .substringBefore(" {")
            .replace("(", "")
            .replace(")", "")
            .split(" ")
            .map { s -> s.split(",").map { it.toInt() } }

        val joltageRequirements = line.substringAfter("{").substringBefore("}").split(",").map { it.toInt() }

        println(targetState)
        println(buttons)
        println(joltageRequirements)
        println()

        Machine(targetState, buttons, joltageRequirements, currentState = targetState.map { false }, joltageCounters = joltageRequirements.map { 0 })
    }

    private fun minButtonPresses(machine: Machine): Int {
        var iteration = 0
        var machines = listOf(machine)

        while (true) {
            for (m in machines) {
                if (m.isSolved()) {
                    return iteration
                }
            }
            iteration += 1
            machines = machines.map { it.pressAllButtons() }.flatten()
        }
    }

    private fun minJoltagePresses(machine: Machine): Int {
        var iteration = 0
        var machines = listOf(machine)

        while (true) {
            println(iteration)
            println(machines.size)
            println()

            for (m in machines) {
                if (m.isJoltageSolved()) {
                    return iteration
                }
            }
            iteration += 1
            machines = machines.map { it.pressAllJoltageButtonsWithPruning() }.flatten()
        }
    }

    override fun solvePart1(): String {
        return machines.sumOf { machine -> minButtonPresses(machine) }.toString()
    }

    override fun solvePart2(): String {
        return machines.sumOf { machine -> minJoltagePresses(machine) }.toString()
    }
}

fun main() {
    Day10(
        inputFile(
            """
            [.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
            [...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
            [.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "7", part2ExpectedSolution = "33")

    Day10(downloadInput(2025, 10)).run(part1ExpectedSolution = "436", part2ExpectedSolution = null)
}
