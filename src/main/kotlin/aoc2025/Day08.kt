package aoc2025

import Solver
import downloadInput
import inputFile
import java.nio.file.Path
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

// https://adventofcode.com/2025/day/8

data class Position3D(var x: Long, var y: Long, var z: Long) {
    fun euclideanDistance(other: Position3D): Double {
        val distX = (this.x - other.x).toDouble()
        val distY = (this.y - other.y).toDouble()
        val distZ = (this.z - other.z).toDouble()
        return sqrt(distX.pow(2.0) + distY.pow(2.0) + distZ.pow(2.0))
    }
}

class Day08(file: Path, val numP1Connections: Int = 1000) : Solver {
    private val input = InputParser.parseLines(file.toString())

    private val boxes: List<Position3D> = input.map {
        val split = it.split(",")
        val ints = split.map { it.toLong() }
        Position3D(ints[0], ints[1], ints[2])
    }.sortedWith(compareBy<Position3D> { it.x }.thenBy { it.y }.thenBy { it.z })

    private var distances: MutableMap<Pair<Position3D, Position3D>, Double> = mutableMapOf()

    private var connections: MutableList<Pair<Position3D, Position3D>> = mutableListOf()

    init {
        for (b1 in boxes) {
            for (b2 in boxes) {
                if (b1 == b2) {
                    continue
                }
                distances[b1 to b2] = b1.euclideanDistance(b2)
            }
        }
    }

    private fun getCircuits(): Set<Set<Position3D>> {
        val boxToCircuit = mutableMapOf<Position3D, MutableSet<Position3D>>()

        for ((a, b) in connections) {
            val circuitA = boxToCircuit[a]
            val circuitB = boxToCircuit[b]

            when {
                circuitA != null && circuitB != null && circuitA !== circuitB -> {
                    circuitA.addAll(circuitB)
                    circuitB.forEach { boxToCircuit[it] = circuitA }
                }
                circuitA != null -> {
                    circuitA.add(b)
                    boxToCircuit[b] = circuitA
                }
                circuitB != null -> {
                    circuitB.add(a)
                    boxToCircuit[a] = circuitB
                }
                else -> {
                    val newCircuit = mutableSetOf(a, b)
                    boxToCircuit[a] = newCircuit
                    boxToCircuit[b] = newCircuit
                }
            }
        }

        boxes.forEach { box ->
            if (box !in boxToCircuit) {
                boxToCircuit[box] = mutableSetOf(box)
            }
        }

        val circuits = boxToCircuit.values.toSet()
        println(circuits.size)
        return circuits
    }

    override fun solvePart1(): String {
        connections.clear()

        val distancesSorted = distances.entries.sortedBy { it.value }

        while (connections.size < numP1Connections) {
            val nextCircuit = distancesSorted.first {
                val box1 = it.key.first
                val box2 = it.key.second
                (box1 to box2) !in connections && (box2 to box1) !in connections
            }
            connections.add(nextCircuit.key)
        }

        val circuitsBySize = getCircuits().sortedBy { it.size }.reversed()

        return (circuitsBySize[0].size * circuitsBySize[1].size * circuitsBySize[2].size).toString()
    }

    override fun solvePart2(): String {
        connections.clear()

        val distancesSorted = distances.entries.sortedBy { it.value }

        while (getCircuits().size > 1) {
            val nextCircuit = distancesSorted.first {
                val box1 = it.key.first
                val box2 = it.key.second
                (box1 to box2) !in connections && (box2 to box1) !in connections
            }
            connections.add(nextCircuit.key)
        }

        return (connections.last().first.x * connections.last().second.x).toString()

    }
}

fun main() {
    Day08(
        inputFile(
            """
            162,817,812
            57,618,57
            906,360,560
            592,479,940
            352,342,300
            466,668,158
            542,29,236
            431,825,988
            739,650,466
            52,470,668
            216,146,977
            819,987,18
            117,168,530
            805,96,715
            346,949,466
            970,615,88
            941,993,340
            862,61,35
            984,92,344
            425,690,689
            """.trimIndent()
        ),
        numP1Connections = 10
    ).run(part1ExpectedSolution = "40", part2ExpectedSolution = "25272")

    Day08(downloadInput(2025, 8)).run(part1ExpectedSolution = "84968", part2ExpectedSolution = "8663467782")
}
