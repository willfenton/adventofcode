package aoc2021

import InputParser
import Solver

// https://adventofcode.com/2021/day/8

class Day08SevenSegmentSearch(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)

    override fun solvePart1(): String {
        val digits = input.map { line -> line.substringAfter("|").trim().split(" ") }.flatten()
        val uniqueLengths = setOf(2, 3, 4, 7)
        return digits.count { digit -> uniqueLengths.contains(digit.length) }.toString()
    }

    override fun solvePart2(): String {
        val decodedNumbers =
            input.map { line ->
                val (wireCombinations, encodedDigits) =
                    line
                        .split(
                            "|"
                        ).map { it.trim().split(" ").map { it.split("").filter { it.isNotBlank() }.toSet() } }
                val mapping = decodeMapping(wireCombinations)
                val decodedDigits = encodedDigits.map { encodedDigit -> mapping[encodedDigit]!! }
                val number = decodedDigits.joinToString("").toInt()
                number
            }
        return decodedNumbers.sum().toString()
    }

    private fun decodeMapping(wireCombinations: List<Set<String>>): Map<Set<String>, String> {
        val one = wireCombinations.find { it.size == 2 }!!
        val seven = wireCombinations.find { it.size == 3 }!!
        val four = wireCombinations.find { it.size == 4 }!!
        val eight = wireCombinations.find { it.size == 7 }!!

        val remaining = wireCombinations.toMutableSet()
        remaining.removeAll(setOf(one, seven, four, eight))

        val nine = remaining.find { it.size == 6 && it.containsAll(four) }!!
        remaining.remove(nine)

        val three = remaining.find { nine.containsAll(it) && it.containsAll(seven) }!!
        remaining.remove(three)

        val five = remaining.find { nine.containsAll(it) }!!
        remaining.remove(five)

        val two = remaining.find { it.size == 5 }!!
        remaining.remove(two)

        val six = remaining.find { it.containsAll(five) }!!
        remaining.remove(six)

        val zero = remaining.single()

        return mapOf(
            zero to "0",
            one to "1",
            two to "2",
            three to "3",
            four to "4",
            five to "5",
            six to "6",
            seven to "7",
            eight to "8",
            nine to "9"
        )
    }
}

fun main() {
    Solver.run { Day08SevenSegmentSearch("input/2021/08.test2.txt") }
    Solver.run { Day08SevenSegmentSearch("input/2021/08.test.txt") }
    Solver.run { Day08SevenSegmentSearch("input/2021/08.txt") }
}
