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
    val joltageCounters: List<Int>,
    val buttonPresses: Int = 0
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
        return buttons.map { button ->
            val newState = joltageCounters.toMutableList()
            val maxPresses = button.minOf { joltageRequirements[it] - newState[it] }
            for (i in button) {
                newState[i] = newState[i] + maxPresses
            }
            val newButtons = buttons.toMutableList()
            newButtons.remove(button)
            this.copy(buttons = newButtons, joltageCounters = newState, buttonPresses = buttonPresses + maxPresses)
        }
    }

    fun isSolved(): Boolean = currentState == targetState

    fun isJoltageSolved(): Boolean = joltageCounters == joltageRequirements

    val joltageInfo: String = "${joltageRequirements}, ${joltageCounters}, ${buttonPresses}, $buttons"
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
    }.sortedBy { it.buttons.size }

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

    // TODO should never return null
    private fun minJoltagePresses(machine: Machine): Int? {
        println(machine)
        val unsolvedMachines = mutableListOf(machine)
        val solvedMachines = mutableListOf<Machine>()

        while (unsolvedMachines.isNotEmpty()) {
//            println(machine.joltageInfo)
            val machine = unsolvedMachines.removeLast()
            if (machine.isJoltageSolved()) {
                solvedMachines.add(machine)
            } else {
                unsolvedMachines.addAll(machine.pressAllJoltageButtonsWithPruning())
            }
//            for (m in machines) {
//                if (m.isJoltageSolved()) {
//                    solvedMachines.add(m)
//                }
//            }
//            machines = machines.map { it.pressAllJoltageButtonsWithPruning() }.flatten()
        }

        println(solvedMachines.minOfOrNull { it.buttonPresses })
        println()
        return solvedMachines.minOfOrNull { it.buttonPresses }
    }

    override fun solvePart1(): String {
        return machines.sumOf { machine -> minButtonPresses(machine) }.toString()
    }

    override fun solvePart2(): String {
        //return minJoltagePresses(Machine(targetState=listOf(false, true, true, true, true, true, false, false, true, true), buttons=listOf(listOf(0, 1, 3, 4, 5, 6, 8, 9), listOf(0, 2, 3, 4, 6, 7, 8, 9), listOf(9), listOf(0, 1, 2, 6, 7), listOf(4, 5), listOf(1, 2, 5, 8, 9), listOf(1, 5, 6, 8), listOf(0, 3, 5, 9), listOf(0, 1, 2, 5, 7, 8), listOf(0, 3, 5, 8)), joltageRequirements=listOf(56, 49, 25, 47, 23, 67, 40, 13, 63, 54), currentState=listOf(false, false, false, false, false, false, false, false, false, false), joltageCounters=listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0), buttonPresses=0)).toString()
        return machines.sumOf { machine -> minJoltagePresses(machine) ?: 0 }.toString()
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
