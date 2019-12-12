@file:Suppress("UNUSED_PARAMETER")

package lesson6

import java.io.File
import java.util.*
import kotlin.math.max

/**
 * Наибольшая общая подпоследовательность.
 * Средняя
 *
 * Дано две строки, например "nematode knowledge" и "empty bottle".
 * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
 * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
 * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
 * Если общей подпоследовательности нет, вернуть пустую строку.
 * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
 * При сравнении подстрок, регистр символов *имеет* значение.
 */
fun longestCommonSubSequence(first: String, second: String): String {
    if (first.isEmpty() || second.isEmpty()) return ""
    val fLen = first.length
    val sLen = second.length
    val matrix = Array(fLen + 1) { IntArray(sLen + 1) }
    var output = ""

    for (i in 0..fLen) {
        for (j in 0..sLen) {
            if (i == 0 || j == 0)
                matrix[i][j] = 0
            else if (first[i - 1] == second[j - 1])
                matrix[i][j] = matrix[i - 1][j - 1] + 1
            else
                matrix[i][j] = maxOf(matrix[i - 1][j], matrix[i][j - 1])
        }
    }


    var index = matrix[fLen][sLen]
    val lcs = ArrayDeque<Char>()
    var i = fLen
    var j = sLen
    while (i > 0 && j > 0) {
        when {
            first[i - 1] == second[j - 1] -> {
                lcs.push(first[i - 1])
                i--
                j--
                index--
            }
            matrix[i - 1][j] > matrix[i][j - 1] -> i--
            else -> j--
        }
    }

    for (k in 0 until lcs.size)
        output += lcs.pollFirst()

    return output
}

/**
 * Наибольшая возрастающая подпоследовательность
 * Сложная
 *
 * Дан список целых чисел, например, [2 8 5 9 12 6].
 * Найти в нём самую длинную возрастающую подпоследовательность.
 * Элементы подпоследовательности не обязаны идти подряд,
 * но должны быть расположены в исходном списке в том же порядке.
 * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
 * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
 * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
 */
fun longestIncreasingSubSequence(list: List<Int>): List<Int> {
    TODO()
}

/**
 * Самый короткий маршрут на прямоугольном поле.
 * Средняя
 *
 * В файле с именем inputName задано прямоугольное поле:
 *
 * 0 2 3 2 4 1
 * 1 5 3 4 6 2
 * 2 6 2 5 1 3
 * 1 4 3 2 6 2
 * 4 2 3 1 5 0
 *
 * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
 * В каждой клетке записано некоторое натуральное число или нуль.
 * Необходимо попасть из верхней левой клетки в правую нижнюю.
 * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
 * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
 *
 * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
 */
fun shortestPathOnField(inputName: String): Int {

    val matrix = File(inputName).readLines().map { it -> it.split(" ").map { it.toInt() }.toMutableList() }

    for (i in 0 until matrix.size)
        for (k in 0 until matrix.first().size) {
            val prevPoints = when {
                i == 0 && k == 0 -> mutableListOf(matrix[i][k])
                i > 0 && k == 0 -> mutableListOf(matrix[i - 1][k])
                i == 0 && k > 0 -> mutableListOf(matrix[i][k - 1])
                else -> mutableListOf(matrix[i][k - 1], matrix[i - 1][k], matrix[i - 1][k - 1])
            }
            matrix[i][k] += prevPoints.min()!!
        }
    return matrix[matrix.size - 1][matrix.first().size - 1]
}

// Задачу "Максимальное независимое множество вершин в графе без циклов"
// смотрите в уроке 5