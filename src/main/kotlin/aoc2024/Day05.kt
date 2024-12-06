package aoc2024

import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2024/day/5

data class PageOrderingRule(val page1: Int, val page2: Int)

class Day05(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val pageOrderingRules = mutableListOf<PageOrderingRule>()
    private val updates = mutableListOf<List<Int>>()

    init {
        for (line in input) {
            if (line.contains("|")) {
                val split = line.split("|")
                pageOrderingRules.add(PageOrderingRule(split[0].toInt(), split[1].toInt()))
            } else if (line.isBlank()) {
                continue
            } else {
                val pageNumbers = line.split(",").map { it.toInt() }.toList()
                updates.add(pageNumbers)
            }
        }
    }

    override fun solvePart1(): String {
        val validUpdates = updates.filter { isUpdateValid(it) }
        val middlePageSum = validUpdates.sumOf { it[it.size / 2] }
        return middlePageSum.toString()
    }

    private fun isUpdateValid(update: List<Int>): Boolean {
        for (i in update.indices) {
            val currentPage = update[i]
            val pagesAfter = update.subList(i + 1, update.size)
            for (pageAfter in pagesAfter) {
                val rule = pageOrderingRules.find { it.page1 == pageAfter && it.page2 == currentPage }
                if (rule != null) {
                    return false
                }
            }
        }
        return true
    }

    override fun solvePart2(): String {
        val invalidUpdates = updates.filter { !isUpdateValid(it) }
        val pageComparator = Comparator<Int> { page1, page2 ->
            val rule1 = pageOrderingRules.find { it.page1 == page1 && it.page2 == page2 }
            val rule2 = pageOrderingRules.find { it.page2 == page1 && it.page1 == page2 }

            if (rule1 != null) {
                1
            } else if (rule2 != null) {
                -1
            } else {
                0
            }
        }
        val sortedInvalidUpdates = invalidUpdates.map { invalidUpdate ->
            invalidUpdate.sortedWith(pageComparator)
        }
        val middlePageSum = sortedInvalidUpdates.sumOf { it[it.size / 2] }
        return middlePageSum.toString()
    }
}

fun main() {
    Day05(
        inputFile(
            """
            47|53
            97|13
            97|61
            97|47
            75|29
            61|13
            75|53
            29|13
            97|29
            53|29
            61|53
            97|53
            61|29
            47|13
            75|47
            97|75
            47|61
            75|61
            47|29
            75|13
            53|13
            
            75,47,61,53,29
            97,61,53,29,13
            75,29,13
            75,97,47,61,53
            61,13,29
            97,13,75,29,47
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "143", part2ExpectedSolution = "123")

    Day05(downloadInput(2024, 5)).run()
}
