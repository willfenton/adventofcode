package aoc2021

import InputParser
import Solver
import java.util.concurrent.atomic.AtomicInteger

// https://adventofcode.com/2021/day/16

@OptIn(ExperimentalStdlibApi::class, ExperimentalUnsignedTypes::class)
class Day16PacketDecoder(override val filename: String) : Solver {
    private val input = InputParser.parseLines(filename).single()
    private val bitString = input.hexToUByteArray().joinToString("") { it.toString(2).padStart(8, '0') }

    override fun solvePart1(): String {
        val packet = Packet.parse(bitString)
        return packet.versionSum().toString()
    }

    override fun solvePart2(): String {
        val packet = Packet.parse(bitString)
        return packet.value().toString()
    }
}

abstract class Packet(open val version: ULong, open val typeId: ULong) {
    abstract val length: Int

    abstract fun versionSum(): ULong

    abstract fun value(): ULong

    companion object {
        fun parse(bitString: String): Packet {
            val version = bitString.substring(0, 3).toULong(2)
            val typeId = bitString.substring(3, 6).toULong(2)
            val index = AtomicInteger(6)

            return when (typeId) {
                4uL -> {
                    var valueBits = ""
                    while (true) {
                        val controlBit = bitString[index.getAndIncrement()]
                        val dataBits = bitString.substring(index.get(), index.addAndGet(4))
                        valueBits += dataBits
                        if (controlBit == '0') break
                    }
                    val value = valueBits.toULong(2)
                    DataPacket(version, typeId, index.get(), value)
                }
                else -> {
                    val subPackets = mutableListOf<Packet>()
                    val lengthTypeId = bitString[index.getAndIncrement()]
                    when (lengthTypeId) {
                        '0' -> {
                            val subPacketsLengthBits = bitString.substring(index.get(), index.addAndGet(15)).toULong(2)
                            val endIndex = index.get() + subPacketsLengthBits.toInt()
                            while (true) {
                                val subPacketBitString = bitString.substring(index.get(), endIndex)
                                if (subPacketBitString.length < 8) break
                                val subPacket = parse(subPacketBitString)
                                subPackets.add(subPacket)
                                index.addAndGet(subPacket.length)
                            }
                        }
                        '1' -> {
                            val numSubPackets = bitString.substring(index.get(), index.addAndGet(11)).toULong(2)
                            repeat(numSubPackets.toInt()) {
                                val subPacketBitString = bitString.substring(index.get(), bitString.length)
                                val subPacket = parse(subPacketBitString)
                                subPackets.add(subPacket)
                                index.addAndGet(subPacket.length)
                            }
                        }
                    }

                    OperatorPacket(version, typeId, index.get(), subPackets)
                }
            }
        }
    }
}

data class DataPacket(override val version: ULong, override val typeId: ULong, override val length: Int, val value: ULong) :
    Packet(value, typeId) {
    override fun versionSum(): ULong = version

    override fun value(): ULong = value
}

data class OperatorPacket(override val version: ULong, override val typeId: ULong, override val length: Int, val subpackets: List<Packet>) :
    Packet(version, typeId) {
    override fun versionSum(): ULong = version + subpackets.sumOf { it.versionSum() }

    override fun value(): ULong = when (typeId.toInt()) {
        0 -> subpackets.sumOf { it.value() }
        1 -> subpackets.fold(1uL) { acc, it -> acc * it.value() }
        2 -> subpackets.minOf { it.value() }
        3 -> subpackets.maxOf { it.value() }
        5 -> if (subpackets[0].value() > subpackets[1].value()) 1uL else 0uL
        6 -> if (subpackets[0].value() < subpackets[1].value()) 1uL else 0uL
        7 -> if (subpackets[0].value() == subpackets[1].value()) 1uL else 0uL
        else -> throw UnsupportedOperationException("Unsupported type ID: $typeId")
    }
}

fun main() {
    Solver.run { Day16PacketDecoder("input/2021/16.test0.txt") }
    Solver.run { Day16PacketDecoder("input/2021/16.test1.txt") }
    Solver.run { Day16PacketDecoder("input/2021/16.test2.txt") }
    Solver.run { Day16PacketDecoder("input/2021/16.test3.txt") }
    Solver.run { Day16PacketDecoder("input/2021/16.test4.txt") }
    Solver.run { Day16PacketDecoder("input/2021/16.txt") }
}
