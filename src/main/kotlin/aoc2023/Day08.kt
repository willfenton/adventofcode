package aoc2023

import InputParser
import Solver

// https://adventofcode.com/2023/day/8

data class Node(val id: String, val left: String, val right: String)

class Day08(val filename: String) : Solver {
    private val input = InputParser.parseLines(filename)
    private val instructions = input[0].split("").filter { it.isNotBlank() }
    private val nodes = input.drop(2).map { line ->
        val id = line.substringBefore(" =")
        val left = line.substringAfter("(").substringBefore(",")
        val right = line.substringAfter(", ").substringBefore(")")
        Node(id, left, right)
    }
    private val nodeMap = nodes.associateBy { it.id }

    override fun solvePart1(): String {
        var currentNode = nodeMap["AAA"]!!
        val endNode = nodeMap["ZZZ"]!!

        var steps = 0
        while (currentNode != endNode) {
            val instruction = instructions[steps % instructions.size]
            when (instruction) {
                "L" -> currentNode = nodeMap[currentNode.left]!!
                "R" -> currentNode = nodeMap[currentNode.right]!!
            }
            steps += 1
        }

        return steps.toString()
    }

    override fun solvePart2(): String {
        val startNodes = nodes.filter { it.id.endsWith("A") }
        val stepsByNode = mutableMapOf<String, Long>()

        for (startNode in startNodes) {
            var steps = 0
            var currentNode = startNode
            while (!currentNode.id.endsWith("Z")) {
                val instruction = instructions[steps % instructions.size]
                when (instruction) {
                    "L" -> currentNode = nodeMap[currentNode.left]!!
                    "R" -> currentNode = nodeMap[currentNode.right]!!
                }
                steps += 1
            }
            stepsByNode[startNode.id] = steps.toLong()
        }

//        var answer = 0
//        while (!numbers.all { answer % it == 0L }) {
//            answer += 1
//        }

        var answer = stepsByNode.values.first()
        for (number in stepsByNode.values) {
            answer = lcm(answer, number)
        }

        return answer.toString()
    }

    // https://en.wikipedia.org/wiki/Euclidean_algorithm
    private fun gcd(a: Long, b: Long): Long {
        if (b == 0L) return a
        return gcd(b, a % b)
    }

    // https://en.wikipedia.org/wiki/Least_common_multiple#Using_the_greatest_common_divisor
    private fun lcm(a: Long, b: Long): Long = (a * b) / gcd(a, b)

//    override fun solvePart2Bruteforce(): String {
//        var currentNodes = nodeMap.values.filter { it.id.endsWith("A") }
//
//        var steps = 0
//        while (!currentNodes.all { it.id.endsWith("Z") }) {
//            val instruction = instructions[steps % instructions.size]
//            currentNodes = when (instruction) {
//                "L" -> currentNodes.map { node -> nodeMap[node.left]!! }
//                "R" -> currentNodes.map { node -> nodeMap[node.right]!! }
//                else -> throw Exception("bad code")
//            }
//            steps += 1
//        }
//
//        return steps.toString()
//    }
}

fun main() {
    Day08("input/2023/08.test.txt").run(part1ExpectedSolution = "2", part2ExpectedSolution = null)
    Day08("input/2023/08.test2.txt").run(part1ExpectedSolution = "6", part2ExpectedSolution = null)
    Day08("input/2023/08.test3.txt").run(part1ExpectedSolution = null, part2ExpectedSolution = "6", swallowExceptions = true)
    Day08("input/2023/08.txt").run(part1ExpectedSolution = "16897")
}
