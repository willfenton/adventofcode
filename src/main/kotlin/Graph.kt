data class Edge<T>(val source: T, val dest: T, var weight: Double = 1.0)

class DirectedWeightedGraph<T> {
    val nodesToEdges: MutableMap<T, MutableSet<Edge<T>>> = mutableMapOf()

    fun addNode(node: T) {
        if (node !in nodesToEdges.keys) {
            nodesToEdges[node] = mutableSetOf()
        }
    }

    fun addEdge(edge: Edge<T>) {
        addNode(edge.source)
        addNode(edge.dest)
        nodesToEdges[edge.source]!!.add(edge)
    }
}

fun <T> bfs(graph: DirectedWeightedGraph<T>, startingNode: T) {
    val visited: MutableSet<T> = mutableSetOf(startingNode)
    val queue: MutableList<T> = mutableListOf(startingNode)

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()

        val edges = graph.nodesToEdges[current]!!
        val unvisitedNeighbors = edges.map { edge -> edge.dest }.filter { neighbor -> neighbor !in visited }

        for (neighbor in unvisitedNeighbors) {
            visited.add(neighbor)
            queue.add(neighbor)
        }
    }
}

fun <T> dfs(graph: DirectedWeightedGraph<T>, startingNode: T) {
    val stack = mutableListOf(startingNode)
    val visited = mutableSetOf<T>()

    while (stack.isNotEmpty()) {
        val current = stack.removeLast()

        if (current !in visited) {
            visited.add(current)

            val edges = graph.nodesToEdges[current]!!
            val unvisitedNeighbors = edges.map { edge -> edge.dest }.filter { neighbor -> neighbor !in visited }

            for (neighbor in unvisitedNeighbors) {
                stack.addLast(neighbor)
            }
        }
    }
}

// note: doesn't work with negative edge weights
fun <T> dijkstra(graph: DirectedWeightedGraph<T>, startingNode: T): Pair<MutableMap<T, Double>, MutableMap<T, T>> {
    val visited = mutableSetOf<T>()
    val queue = mutableListOf(startingNode)
    val previous = mutableMapOf<T, T>()
    val distances = mutableMapOf(startingNode to 0.0)

    while (queue.isNotEmpty()) {
        val minDistanceIndex = queue.withIndex().minBy { (index, node) -> distances[node] ?: Double.MAX_VALUE }.index
        val current = queue.removeAt(minDistanceIndex)

        visited.add(current)

        val edges = graph.nodesToEdges[current]!!.filter { it.dest !in visited }

        for (edge in edges) {
            val currentDistance = distances[edge.dest]
            val newDistance = distances[current]!! + edge.weight
            if (currentDistance == null || newDistance < currentDistance) {
                distances[edge.dest] = newDistance
                previous[edge.dest] = current
            }

            if (edge.dest !in queue) {
                queue.add(edge.dest)
            }
        }
    }

    return Pair(distances, previous)
}
