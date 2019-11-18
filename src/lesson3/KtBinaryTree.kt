package lesson3

import java.util.*
import kotlin.NoSuchElementException
import kotlin.math.max



// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    override var size = 0
        private set

    private class Node<T>(val value: T) {

        var left: Node<T>? = null

        var right: Node<T>? = null
    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
            }
        }
        size++
        return true
    }

    override fun checkInvariant(): Boolean =
        root?.let { checkInvariant(it) } ?: true

    override fun height(): Int = height(root)

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    private fun height(node: Node<T>?): Int {
        if (node == null) return 0
        return 1 + max(height(node.left), height(node.right))
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    override fun remove(element: T): Boolean {
        var current = root
        var parent = root
        var isLeftChild = true
        if (root == null) return false

        while (current!!.value != element) {                //поиск удаляемого элемента и его "родителя"
            parent = current
            if (element < current.value) {
                current = current.left
                isLeftChild = true
            } else {
                current = current.right
                isLeftChild = false
            }
        }


        if (current.left == null && current.right == null) {  //Удаление узла без потомков
            when {
                current == root -> {
                    root = null
                }
                isLeftChild -> {
                    parent?.left = null
                }
                else -> {
                    parent?.right = null
                }
            }
        }

        if (current.right == null)                              //Удаление узла с 1-м потомком
            when {
                current == root -> root = current.left
                isLeftChild -> parent?.left = current.left
                else -> parent?.right = current.left
            }
        else if (current.left == null)
            when {
                current == root -> root = current.right
                isLeftChild -> parent?.left = current.right
                else -> parent?.right = current.right
            }

        val successor = getSuccessor(current)                   //Удаление узла с 2-я потомками
        when {
            current == root -> root = successor
            isLeftChild -> parent?.left = successor
            else -> parent?.right = successor
        }
        successor.left = current.left
        return true
    }

    private fun getSuccessor(removable: Node<T>): Node<T> {         //Поиск преемника удаляемого элемента
        var successorParent = removable                             //В случаях, когда удаляемый узел имеет 2 потомков
        var successor = removable
        var current = removable.right
        while (current != null) {
            successorParent = successor
            successor = current
            current = current.left
        }
        if (successor != removable.right) {
            successorParent.left = successor.right
            successor.right = removable.right
        }
        return successor
    }

    //Функция remove тесты не проходит, к сожалению времени на фикс не хватило
    //Максимально может пройтись 2 раза по дереву


    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? =
        root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    inner class BinaryTreeIterator internal constructor() : MutableIterator<T> {

        private var current = root
        private var stack = Stack<Node<T>>()

        /**
         * Проверка наличия следующего элемента
         * Средняя
         */
        override fun hasNext(): Boolean {
            return current != null
        }

        /**
         * Поиск следующего элемента
         * Средняя
         */
        override fun next(): T {
            while (current != null) {
                stack.push(current)
                current = current?.left
            }

            current = stack.pop()
            val node = current
            current = current?.right

            return node!!.value
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        override fun remove() {
            // TODO
            throw NotImplementedError()
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Найти множество всех элементов в диапазоне [fromElement, toElement)
     * Очень сложная
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        TODO()
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }
}
