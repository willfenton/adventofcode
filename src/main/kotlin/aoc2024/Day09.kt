package aoc2024

import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/9

const val EMPTY = -1

class Day09(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString()).single()

    private val memory: MutableList<Int> = mutableListOf()
    private val fileIDs: MutableSet<Int> = mutableSetOf()

    private fun init() {
        memory.clear()
        fileIDs.clear()
        var id = 0
        for (i in input.indices) {
            val num = input[i].toString().toInt()
            if (i % 2 == 0) {
                // file
                repeat(num) {
                    memory.add(id)
                }
                fileIDs.add(id)
                id += 1
            } else {
                // empty space
                repeat(num) {
                    memory.add(EMPTY)
                }
            }
        }
    }

    override fun solvePart1(): String {
        init()
        while (true) {
            val firstEmptyIndex = memory.indexOfFirst { it == EMPTY }
            val lastFileIndex = memory.indexOfLast { it != EMPTY }
            if (lastFileIndex < firstEmptyIndex) {
                break
            }
            memory[firstEmptyIndex] = memory[lastFileIndex]
            memory[lastFileIndex] = EMPTY
        }
        return checksum().toString()
    }

    override fun solvePart2(): String {
        init()
        while (fileIDs.isNotEmpty()) {
            val maxFileID = fileIDs.max()
            fileIDs.remove(maxFileID)
            val fileSize = memory.count { it == maxFileID }

            val file = memory.withIndex().filter { it.value == maxFileID }

            for (window in memory.withIndex().windowed(fileSize)) {
                // check if we're past the file
                if (window.minOf { it.index } > file.maxOf { it.index }) break

                if (window.all { it.value == EMPTY }) {
                    window.forEach {
                        memory[it.index] = maxFileID
                    }
                    file.forEach {
                        memory[it.index] = EMPTY
                    }
                    break
                }
            }
        }
        return checksum().toString()
    }

    private fun checksum(): Long {
        var checksum = 0L
        for ((index, num) in memory.withIndex()) {
            if (num != EMPTY) {
                checksum += index * num
            }
        }
        return checksum
    }
}

fun main() {
    Day09(
        inputFile(
            """
            2333133121414131402
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "1928", part2ExpectedSolution = "2858")

    Day09(downloadInput(2024, 9)).run()
}
