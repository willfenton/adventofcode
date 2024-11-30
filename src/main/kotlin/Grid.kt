typealias Grid<T> = MutableMap<Point, T>

val <T> Grid<T>.points: List<Point> get() = keys.toList()

val <T> Grid<T>.minX: Int get() = keys.minOf { it.x }
val <T> Grid<T>.maxX: Int get() = keys.maxOf { it.x }
val <T> Grid<T>.minY: Int get() = keys.minOf { it.y }
val <T> Grid<T>.maxY: Int get() = keys.maxOf { it.y }

val <T> Grid<T>.rows: Int get() = maxX - minX + 1
val <T> Grid<T>.columns: Int get() = maxY - minY + 1

data class Point(var x: Int, var y: Int) {
    val u: Point get() = Point(x, y - 1)
    val ur: Point get() = Point(x + 1, y - 1)
    val r: Point get() = Point(x + 1, y)
    val dr: Point get() = Point(x + 1, y + 1)
    val d: Point get() = Point(x, y + 1)
    val dl: Point get() = Point(x - 1, y + 1)
    val l: Point get() = Point(x - 1, y)
    val ul: Point get() = Point(x - 1, y - 1)

    val cardinalNeighbors: Set<Point> get() = setOf(u, r, d, l)
    val diagonalNeighbors: Set<Point> get() = setOf(ur, dr, dl, ul)
    val allNeighbors: Set<Point> get() = cardinalNeighbors + diagonalNeighbors

    operator fun plus(other: Point): Point = Point(this.x + other.x, this.y + other.y)
}
