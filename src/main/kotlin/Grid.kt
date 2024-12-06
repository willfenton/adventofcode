enum class Direction(val position: Position) {
    UP(Position(0, -1)),
    DOWN(Position(0, 1)),
    LEFT(Position(-1, 0)),
    RIGHT(Position(1, 0)),
    UP_LEFT(UP.position + LEFT.position),
    UP_RIGHT(UP.position + RIGHT.position),
    DOWN_LEFT(DOWN.position + LEFT.position),
    DOWN_RIGHT(DOWN.position + RIGHT.position);

    companion object {
        val CARDINAL = listOf(UP, DOWN, LEFT, RIGHT)
        val DIAGONAL = listOf(UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT)
        val ALL = CARDINAL + DIAGONAL
    }

    fun rotate90Right() = when (this) {
        UP -> RIGHT
        DOWN -> LEFT
        LEFT -> UP
        RIGHT -> DOWN
        else -> TODO()
    }

    fun rotate90Left() = when (this) {
        UP -> LEFT
        DOWN -> RIGHT
        LEFT -> DOWN
        RIGHT -> UP
        else -> TODO()
    }
}

data class Position(val x: Int, val y: Int) {
    operator fun plus(other: Position): Position = Position(this.x + other.x, this.y + other.y)

    fun neighbor(direction: Direction): Position = this + direction.position

    fun neighbors(directions: List<Direction> = Direction.CARDINAL) = directions.map { direction -> neighbor(direction) }
}

class Grid<T> {
    val cells: MutableMap<Position, GridCell<T>> = mutableMapOf()

    var minX: Int = 0
    var maxX: Int = 0
    var minY: Int = 0
    var maxY: Int = 0

    var width: Int = 0
    var height: Int = 0

    fun init() {
        minX = cells.keys.minOf { it.x }
        maxX = cells.keys.maxOf { it.x }
        minY = cells.keys.minOf { it.y }
        maxY = cells.keys.maxOf { it.y }

        width = maxX - minX
        height = maxY - minY
    }

    operator fun set(position: Position, data: T) {
        cells[position] = GridCell(position, data)
    }

    operator fun get(position: Position): GridCell<T>? = cells[position]
}

data class GridCell<T>(val pos: Position, var data: T, val grid: Grid<T>? = null) {
    var visited: Boolean = false

    fun neighbor(direction: Direction): GridCell<T>? = grid!!.cells[pos + direction.position]

    fun neighbors(directions: List<Direction> = Direction.CARDINAL): List<GridCell<T>> =
        directions.mapNotNull { direction -> neighbor(direction) }

    fun neighborIsConnected(direction: Direction, isConnected: (cell: GridCell<T>, other: GridCell<T>) -> Boolean): Boolean {
        val neighbor = neighbor(direction) ?: return false
        return isConnected(this, neighbor)
    }

    fun connectedNeighbors(
        directions: List<Direction> = Direction.CARDINAL,
        isConnected: (cell: GridCell<T>, other: GridCell<T>) -> Boolean
    ): List<GridCell<T>> {
        val allNeighbors = neighbors(directions)
        return allNeighbors.filter { neighbor -> isConnected(this, neighbor) }
    }
}

fun <T> bfs(grid: Grid<T>, startingPosition: Position) {
    val neighborDirections = Direction.CARDINAL
    val isConnected: (cell: GridCell<T>, other: GridCell<T>) -> Boolean = { cell, other -> true }

    val visited: MutableSet<Position> = mutableSetOf()
    val queue: MutableList<Position> = mutableListOf(startingPosition)

    while (queue.isNotEmpty()) {
        val currentPos = queue.removeFirst()
        val currentCell = grid[currentPos]!!

        visited.add(currentPos)

        val connectedNeighbors = currentCell.connectedNeighbors(neighborDirections, isConnected)
        val unvisitedNeighbors = connectedNeighbors.filter { !visited.contains(it.pos) }

        for (neighbor in unvisitedNeighbors) {
            if (!queue.contains(neighbor.pos)) {
                queue.add(neighbor.pos)
            }
        }
    }
}
