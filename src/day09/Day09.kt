package day09

import AoCTask
import kotlin.math.abs
import kotlin.math.sign

// https://adventofcode.com/2022/day/9

data class Direction(val x: Int, val y: Int) {
    companion object {
        fun fromString(string: String): Direction {
            val (dir, dist) = string.split(" ")
            val intDist = dist.toInt()
            return when (dir) {
                "R" -> Direction(intDist, 0)
                "L" -> Direction(-intDist, 0)
                "U" -> Direction(0, intDist)
                "D" -> Direction(0, -intDist)
                else -> throw Exception("Should not happen")
            }
        }
    }

    val length = maxOf(abs(x), abs(y))

    fun normalized(): Direction {
        return Direction(x.sign, y.sign)
    }

    override fun toString(): String {
        return "($x, $y)->"
    }
}

data class Position(val x: Int, val y: Int) {
    override fun toString(): String {
        return "($x, $y)"
    }
}

operator fun Position.plus(direction: Direction) = Position(x + direction.x, y + direction.y)

operator fun Position.minus(other: Position) = Direction(x - other.x, y - other.y)

//           H
//   TH      .
//   T..H   T.    H.T
//


private fun headPath(
    dir: Direction,
    head: Position
): List<Position> {
    val step = dir.normalized()
    return (1..dir.length).scan(head) { currentHead, _ ->
        currentHead + step
    }
}

private fun tailPathFromHeadPath(
    path: List<Position>,
    tail: Position
): List<Position> {
    val tailPath = path.filterIndexed { index, position ->
        if (index < path.size - 1) {
            (tail - position).length > 1 || (tail - path[index + 1]).length > 1
        } else {
            false
        }
    }
    return tailPath
}

private fun path(from: Position, to: Position): List<Position> {
    var delta = to - from
    val path = mutableListOf(from)
    while (delta.x != 0 || delta.y != 0) {
        val dirX = delta.x.sign
        val dirY = delta.y.sign

        path.add(path.last() + Direction(dirX, dirY))
        delta = Direction(delta.x - dirX, delta.y - dirY)
    }
    return path
}

fun testPaths() {

    val tail = Position(0,0)
    // check same starting point distance 1
    check(tailPathFromHeadPath(headPath(Direction(1, 0), Position(0,0)), tail).size == 0) {
        "Same starting point, R 1 doesn't work"
    }
    check(tailPathFromHeadPath(headPath(Direction(-1, 0), Position(0,0)), tail).size == 0) {
        "Same starting point, L 1 doesn't work"
    }
    check(tailPathFromHeadPath(headPath(Direction(0, 1), Position(0,0)), tail).size == 0) {
        "Same starting point, U 1 doesn't work"
    }
    check(tailPathFromHeadPath(headPath(Direction(0, -1), Position(0,0)), tail).size == 0) {
        "Same starting point, D 1 doesn't work"
    }

    // check same starting point distance 2
    check(tailPathFromHeadPath(headPath(Direction(2, 0), Position(0,0)), tail).size == 1) {
        "Same starting point, R 2 doesn't work"
    }
    check(tailPathFromHeadPath(headPath(Direction(-2, 0), Position(0,0)), tail).size == 1) {
        "Same starting point, L 2 doesn't work"
    }
    check(tailPathFromHeadPath(headPath(Direction(0, 2), Position(0,0)), tail).size == 1) {
        "Same starting point, U 2 doesn't work"
    }
    check(tailPathFromHeadPath(headPath(Direction(0, -2), Position(0,0)), tail).size == 1) {
        "Same starting point, D 2 doesn't work"
    }

}

fun drawRope(segments: List<Position>, drawOnlyHashes: Boolean = false) {
    val minX = segments.minOf { it.x }
    val minY = segments.minOf { it.y }
    val maxX = segments.maxOf { it.x }
    val maxY = segments.maxOf { it.y }

    (maxY downTo minY).forEach { y ->
        (minX..maxX).forEach { x ->

            val index = segments.indexOfFirst { it.x == x && it.y == y }
            val symbol = when (index) {
                -1 -> "."
                0 -> if (drawOnlyHashes) "#" else "H"
                else -> if (drawOnlyHashes) "#" else index
            }
            print(symbol)
        }
        println()
    }

}

fun part1(input: List<String>): Int {
    val directions = input.map { Direction.fromString(it) }

    var head = Position(0, 0)
    var tail = Position(0, 0)

    val visitedPositions = mutableSetOf(tail)

    directions.forEach {dir ->

        val path = headPath(dir, head)
        head = path.last()
        val tailPath = tailPathFromHeadPath(path, tail)
        //        println(dir)
//        println("Head: $path")
//        println("Tail: $tailPath")
        tail = tailPath.lastOrNull() ?: tail

        visitedPositions.addAll(tailPath)
    }

    return visitedPositions.size
}

fun part2(input: List<String>): Int {
    val directions = input.map { Direction.fromString(it) }

    val ropeLength = 10
    val rope = MutableList(ropeLength) { Position(0,0) }

    val visitedPositions = mutableSetOf(rope.first())

    directions.forEach {dir ->

        val step = dir.normalized()

        repeat(dir.length) {
            var previousSegmentPosition = rope.first()
            rope.forEachIndexed { index, segment ->
                val newPosition = if (index == 0) {
                   segment + step
                } else {
                    if ((rope[index - 1] - segment).length > 1) {
                        path(segment, rope[index - 1]).dropLast(1).last()
                    } else {
                        segment
                    }
                }

                if (index == rope.size - 1) {
                    visitedPositions.add(newPosition)
                }

                previousSegmentPosition = segment
                rope[index] = newPosition
            }

        }

//        println()
//        drawRope(rope)
    }

//    drawRope(visitedPositions.toList(), true)

    return visitedPositions.size
}

fun main() = AoCTask("day09").run {
    testPaths()
    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)
    check(part2(readTestInput(2)) == 36)

    println(part1(input))
    println(part2(input))
}
