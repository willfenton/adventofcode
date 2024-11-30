package aoc2021

data class Point(val x: Int, val y: Int) {
    val neighbors
        get() =
            listOf(
                Point(x - 1, y),
                Point(x + 1, y),
                Point(x, y - 1),
                Point(x, y + 1)
            )

    val diagonalNeighbors
        get() =
            listOf(
                Point(x - 1, y - 1),
                Point(x - 1, y + 1),
                Point(x + 1, y - 1),
                Point(x + 1, y + 1)
            )
}
