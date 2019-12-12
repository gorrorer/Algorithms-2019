@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson5

import java.util.*
import lesson5.Graph.*
import lesson5.impl.*

/**
 * Эйлеров цикл.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
 * Если в графе нет Эйлеровых циклов, вернуть пустой список.
 * Соседние дуги в списке-результате должны быть инцидентны друг другу,
 * а первая дуга в списке инцидентна последней.
 * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
 * Веса дуг никак не учитываются.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
 *
 * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
 * связного графа ровно по одному разу
 */
fun Graph.findEulerLoop(): List<Edge> {
    if (this.vertices.isEmpty() || this.edges.isEmpty()
        || !this.vertices.none { getNeighbors(it).size % 2 != 0 }
    )
        return listOf()

    val edges = this.edges
    val vertStack = Stack<Vertex>()
    val output = mutableListOf<Edge>()

    vertStack.push(vertices.first())
    while (!vertStack.isEmpty()) {
        val currVertex = vertStack.peek()
        for (vertex in vertices) {
            val edge = getConnection(currVertex, vertex)
            if (edges.contains(edge)) {
                vertStack.push(vertex)
                edges.remove(edge)
                break
            }
        }
        if (currVertex == vertStack.peek()) {
            vertStack.pop()
            if (vertStack.isNotEmpty())
                output.add(getConnection(currVertex, vertStack.peek())!!)
        }
    }

    return output
}

/**
 * Минимальное остовное дерево.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему минимальное остовное дерево.
 * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
 * вернуть любое из них. Веса дуг не учитывать.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ:
 *
 *      G    H
 *      |    |
 * A -- B -- C -- D
 * |    |    |
 * E    F    I
 * |
 * J ------------ K
 */
fun Graph.minimumSpanningTree(): Graph {
    if (vertices.isEmpty()) {
        return GraphBuilder().build()
    }

    val edgeList = mutableListOf<Edge>()
    val vertexSet = mutableSetOf<Vertex>()

    for (vertex in vertices) {
        for ((curVertex, edge) in getConnections(vertex)) {
            if (curVertex !in vertexSet) {
                vertexSet += curVertex
                edgeList += edge
            }
        }
    }

    return GraphBuilder().apply {
        for (edge in edgeList) {
            addVertex(edge.begin)
            addConnection(edge.begin, edge.end)
        }
    }.build()
}

//Пришлось изменить addVertex c private на public, не уверен в законности этого действия


/**
 * Максимальное независимое множество вершин в графе без циклов.
 * Сложная
 *
 * Дан граф без циклов (получатель), например
 *
 *      G -- H -- J
 *      |
 * A -- B -- D
 * |         |
 * C -- F    I
 * |
 * E
 *
 * Найти в нём самое большое независимое множество вершин и вернуть его.
 * Никакая пара вершин в независимом множестве не должна быть связана ребром.
 *
 * Если самых больших множеств несколько, приоритет имеет то из них,
 * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
 *
 * В данном случае ответ (A, E, F, D, G, J)
 *
 * Если на входе граф с циклами, бросить IllegalArgumentException
 *
 * Эта задача может быть зачтена за пятый и шестой урок одновременно
 */
fun Graph.largestIndependentVertexSet(): Set<Graph.Vertex> {
    TODO()
}

/**
 * Наидлиннейший простой путь.
 * Сложная
 *
 * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
 * Простым считается путь, вершины в котором не повторяются.
 * Если таких путей несколько, вернуть любой из них.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ: A, E, J, K, D, C, H, G, B, F, I
 */
fun Graph.longestSimplePath(): Path {
    if (vertices.isEmpty() || edges.isEmpty())
        return Path()

    val graph = this
    var output = Path(vertices.first())
    val pathDeque = ArrayDeque<Path>()

    vertices.mapTo(pathDeque) { Path(it) }
    while (!pathDeque.isEmpty()) {
        val current = pathDeque.pop()
        val end = current.vertices.last()
        val neighVert = getNeighbors(end)
        if (current.length > output.length)
            output = current
        neighVert.filter { !current.contains(it) }
            .forEach { pathDeque.addLast(Path(current, graph, it)) }
    }
    return output
}