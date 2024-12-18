package aoc2024

import Solver
import downloadInput
import java.nio.file.Path
import kotlin.math.pow

// https://adventofcode.com/2024/day/17

private enum class OperandType {
    LITERAL,
    COMBO
}

private enum class Instruction(val opcode: Int, val operandType: OperandType) {
    ADV(0, OperandType.COMBO),
    BXL(1, OperandType.LITERAL),
    BST(2, OperandType.COMBO),
    JNZ(3, OperandType.LITERAL),
    BXC(4, OperandType.LITERAL), // Ignores operand
    OUT(5, OperandType.COMBO),
    BDV(6, OperandType.COMBO),
    CDV(7, OperandType.COMBO)
}

private val instructionsByOpcode = Instruction.entries.associateBy { it.opcode }

class Day17(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private var instructionPointer: Int = 0
    private var registerA: ULong = 0UL
    private var registerB: ULong = 0UL
    private var registerC: ULong = 0UL
    private val programInstructions: List<Int> = input[4].substringAfter(": ").split(",").map { it.toInt() }
    private val programOutput: MutableList<Int> = mutableListOf()

    private fun init() {
        instructionPointer = 0
        registerA = input[0].substringAfter(": ").toULong()
        registerB = input[1].substringAfter(": ").toULong()
        registerC = input[2].substringAfter(": ").toULong()
        programOutput.clear()
    }

    override fun solvePart1(): String {
        init()
        executeProgram()
        return programOutput.joinToString(",")
    }

    override fun solvePart2(): String {
        val stack = mutableListOf<Pair<ULong, Int>>()
        for (a in 63UL downTo 0UL) {
            stack.add(a to 6)
        }
        while (stack.isNotEmpty()) {
            val (base, bits) = stack.removeLast()
            if (bits > 64) {
                continue
            }
            for (next in 7UL downTo 0UL) {
                val combined = base + next.shl(bits)
                val result = evaluateRegisterAValue(combined)
                when (result) {
                    Result.VALID -> return combined.toString()
                    Result.POTENTIALLY_VALID -> stack.addLast(combined to bits + 3)
                    Result.INVALID -> continue
                }
            }
        }
        return ""
    }

    private fun executeProgram() {
        while (instructionPointer < programInstructions.size) {
            val opcode = programInstructions[instructionPointer]
            val instruction = instructionsByOpcode[opcode]!!

            val operandInt = programInstructions[instructionPointer + 1]
            val operand = when (instruction.operandType) {
                OperandType.LITERAL -> operandInt.toULong()
                OperandType.COMBO -> when (operandInt) {
                    in 0..3 -> operandInt.toULong()
                    4 -> registerA
                    5 -> registerB
                    6 -> registerC
                    else -> TODO()
                }
            }

            when (instruction) {
                in listOf(Instruction.ADV, Instruction.BDV, Instruction.CDV) -> {
                    val numerator = registerA.toDouble()
                    val denominator = (2.0).pow(operand.toInt())
                    val result = (numerator / denominator).toULong()
                    when (instruction) {
                        Instruction.ADV -> registerA = result
                        Instruction.BDV -> registerB = result
                        Instruction.CDV -> registerC = result
                        else -> TODO()
                    }
                }
                Instruction.BXL -> registerB = registerB.xor(operand)
                Instruction.BST -> registerB = (operand % 8UL)
                Instruction.JNZ -> if (registerA != 0UL) {
                    instructionPointer = operand.toInt()
                    continue
                }
                Instruction.BXC -> registerB = registerB.xor(registerC)
                Instruction.OUT -> {
                    val output = if (operand % 8UL in 0UL..7UL) operand % 8UL else 8UL
                    programOutput.add(output.toInt())
                }
                else -> TODO()
            }

            instructionPointer += 2
        }
    }

    private enum class Result {
        VALID,
        POTENTIALLY_VALID,
        INVALID
    }

    private fun evaluateRegisterAValue(initialA: ULong): Result {
        init()

        registerA = initialA
        registerB = 0UL
        registerC = 0UL

        executeProgram()

        return when {
            programOutput == programInstructions -> Result.VALID
            programOutput.size <= 3 -> Result.POTENTIALLY_VALID
            // idk why the last few values aren't consistent and it's driving me crazy
            programOutput.take(programOutput.size - 3) == programInstructions.take(programOutput.size - 3) -> Result.POTENTIALLY_VALID
            else -> Result.INVALID
        }
    }
}

fun main() {
    Day17(downloadInput(2024, 17)).run(part1ExpectedSolution = "3,6,7,0,5,7,3,1,4")
}
