package aoc2025

import DirectedWeightedGraph
import Edge
import Solver
import downloadInput
import inputFile
import java.nio.file.Path

// https://adventofcode.com/2025/day/11

class Day11(file: Path) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val graph = DirectedWeightedGraph<String>()

    init {
        val connections: Map<String, Set<String>> = input.associate { line ->
            val src = line.substringBefore(":")
            val dest = line.substringAfter(":").trim().split(" ").toSet()
            src to dest
        }
        val allNodes = connections.keys + connections.values.flatten()
        for (node in allNodes) {
            graph.addNode(node)
        }
        for ((node, otherNodes) in connections) {
            for (otherNode in otherNodes) {
                graph.addEdge(Edge(node, otherNode))
            }
        }
    }

    private fun p1Search(node: String): Long {
        if (node == "out") {
            return 1
        }
        val edges = graph.nodesToEdges[node] ?: return 0
        return edges.sumOf { edge -> p1Search(edge.dest) }
    }

    // <node, visitedDac, visitedFft>
    private val cache: MutableMap<Triple<String, Boolean, Boolean>, Long> = mutableMapOf()

    private fun p2Search(node: String, visitedDac: Boolean, visitedFft: Boolean): Long {
        val state = Triple(node, visitedDac, visitedFft)
        return when {
            state in cache -> cache[state]!!
            node == "out" && visitedDac && visitedFft -> 1
            node == "out" -> 0
            else -> {
                val edges = graph.nodesToEdges[node] ?: return 0
                val total = edges.sumOf { edge ->
                    p2Search(
                        edge.dest,
                        visitedDac = visitedDac || edge.dest == "dac",
                        visitedFft = visitedFft || edge.dest == "fft"
                    )
                }
                cache[state] = total
                total
            }
        }
    }

    override fun solvePart1(): String {
        return p1Search("you").toString()
    }

    override fun solvePart2(): String {
        return p2Search("svr", visitedDac = false, visitedFft = false).toString()
    }
}

fun main() {
    Day11(
        inputFile(
            """
            aaa: you hhh
            you: bbb ccc
            bbb: ddd eee
            ccc: ddd eee fff
            ddd: ggg
            eee: out
            fff: out
            ggg: out
            hhh: ccc fff iii
            iii: out
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = "5", part2ExpectedSolution = null)
    Day11(
        inputFile(
            """
            svr: aaa bbb
            aaa: fft
            fft: ccc
            bbb: tty
            tty: ccc
            ccc: ddd eee
            ddd: hub
            hub: fff
            eee: dac
            dac: fff
            fff: ggg hhh
            ggg: out
            hhh: out
            """.trimIndent()
        )
    ).run(part1ExpectedSolution = null, part2ExpectedSolution = "2")
    Day11(downloadInput(2025, 11)).run(part1ExpectedSolution = "668", part2ExpectedSolution = "294310962265680")
}
