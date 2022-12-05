package day05

import AoCTask
import java.util.Stack

// https://adventofcode.com/2022/day/5

data class Instruction(val from: Int, val to: Int, val count: Int) {
    companion object {
        fun fromString(string: String): Instruction {
            val split = string.split(" ")
            return Instruction(
                from = split[3].toInt() - 1,
                to = split[5].toInt() - 1,
                count = split[1].toInt(),
            )
        }
    }
}


private fun extractInstructions(input: List<String>): List<Instruction> {
    val instructions = input
        .dropWhile { it != "" }
        .drop(1)

    return instructions.map { Instruction.fromString(it) }
}

private fun extractStacks(input: List<String>): List<Stack<Char>> {
    val crateStacks = input.takeWhile {
        it != ""
    }

    val stackIds = crateStacks.last().trim().split("\\s+".toRegex()).map { it.toInt() }
    val stacks = stackIds.map { Stack<Char>() }

    crateStacks.reversed().drop(1).forEach { stackLevel ->
        stackLevel.windowed(3, step = 4)
            .map {
                it.firstOrNull { c -> c in 'A'..'Z' }
            }
            .forEachIndexed { index, crate ->
                if (crate != null) {
                    stacks[index].push(crate)
                }
            }
    }

    return stacks
}

fun stackToString(stack: Stack<Char>): String {
    return stack.joinToString("] [", prefix = "[", postfix = "]")
}

private fun printStacks(stacks: List<Stack<Char>>) {
    stacks.forEachIndexed { index, stack ->
        println("$index ${stackToString(stack)}")
    }
}

private fun getStringFromTopCrates(stacks: List<Stack<Char>>) = stacks.joinToString(separator = "") {
    it.peek().toString()
}

fun part1(input: List<String>): String {
    val stacks = extractStacks(input)
    val instructions = extractInstructions(input)

    instructions.forEach { instruction ->
        val (from, to, count) = instruction
        repeat(count) {
            stacks[to].push(stacks[from].pop())
        }
    }

    val result = getStringFromTopCrates(stacks)

    return result
}


fun part2(input: List<String>): String {
    val stacks = extractStacks(input)
    val instructions = extractInstructions(input)

    instructions.forEach { instruction ->
        val (from, to, count) = instruction
        (1..count)
            .map {
                stacks[from].pop()
            }.reversed()
            .forEach { crate ->
                stacks[to].push(crate)
            }
    }

    val result = getStringFromTopCrates(stacks)

    return result
}

fun main() = AoCTask("day05").run {
    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    println(part1(input))
    println(part2(input))
}
