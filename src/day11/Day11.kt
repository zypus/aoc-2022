package day11

import AoCTask
import java.math.BigInteger

// https://adventofcode.com/2022/day/11

sealed class Operation {

    abstract operator fun invoke(value: BigInteger): BigInteger

    data class Add(val amount: BigInteger): Operation() {
        override fun invoke(value: BigInteger): BigInteger = value + amount
    }
    data class Multiply(val amount: BigInteger): Operation() {
        override fun invoke(value: BigInteger): BigInteger = value * amount
    }
    object AddSelf: Operation() {
        override fun invoke(value: BigInteger): BigInteger = value + value
    }
    object Squared: Operation() {
        override fun invoke(value: BigInteger): BigInteger = value * value
    }
}

data class Test(val divisor: BigInteger, val ifTrue: Int, val ifFalse: Int) {
    operator fun invoke(value: BigInteger): Int {
        return if (value.mod(divisor) == BigInteger.ZERO) {
            ifTrue
        } else {
            ifFalse
        }
    }
 }

data class Monkey(val items: MutableList<BigInteger>, val op: Operation, val test: Test, var inspections: BigInteger = BigInteger.ZERO) {

    companion object {
        fun fromLines(lines: List<String>): Monkey {
            val (itemLine, operationLine, testLine, ifTrueLine, ifFalseLine) = lines.drop(1).map(String::trim)
            val items = itemLine.replace("Starting items: ", "").split(", ").map { it.toBigInteger() }
            val (opType, amount) = operationLine.replace("Operation: new = old ", "").split(" ")
            val op = when(opType) {
                "+" -> if (amount == "old") Operation.AddSelf else Operation.Add(amount.toBigInteger())
                "*" -> if (amount == "old") Operation.Squared else Operation.Multiply(amount.toBigInteger())
                else -> throw Exception("Unknown op type: '$opType'")
            }
            val divisor = testLine.replace("Test: divisible by ", "").toBigInteger()
            val ifTrue = ifTrueLine.split(" ").last().toInt()
            val ifFalse = ifFalseLine.split(" ").last().toInt()
            return Monkey(
                items = items.toMutableList(),
                op = op,
                test = Test(divisor, ifTrue, ifFalse)
            )
        }
    }

}


private fun parseMonkeys(input: List<String>): List<Monkey> {
    var monkeyIndex = 0
    val monkeyLines = input.groupBy {
        if (it.isBlank()) {
            monkeyIndex += 1
        }
        monkeyIndex
    }.map { group ->
        group.value.filter { it.isNotBlank() }
    }

    val monkeys = monkeyLines.map { lines ->
        Monkey.fromLines(lines)
    }
    return monkeys
}

private fun simulateRounds(monkeys: List<Monkey>, rounds: Int, transformWorry: (BigInteger) -> BigInteger = {it}) {

    (1..rounds).forEach { round ->
        if ((round + 1) % 100 == 0) {
            println("Simulating round ${round + 1}")
        }
        monkeys.forEach { monkey ->
            val (items, op, test) = monkey
            monkey.inspections += items.size.toBigInteger()
            items.forEach { item ->
                val worryLevel = transformWorry(op(item))
                val targetMonkeyIndex = test(worryLevel)
                monkeys[targetMonkeyIndex].items.add(worryLevel)
            }
            items.clear()
        }
    }
}

fun part1(input: List<String>): BigInteger {
    val monkeys = parseMonkeys(input)

    val three = 3.toBigInteger()

    simulateRounds(monkeys, 20) {
        it / three
    }

    val (topMonkey1, topMonkey2) = monkeys.map { it.inspections }.sortedDescending().take(2)
    return topMonkey1 * topMonkey2
}

fun part2(input: List<String>): BigInteger {
    val monkeys = parseMonkeys(input)

    val mod = monkeys.map { it.test.divisor }.reduce { acc, bigInteger -> acc * bigInteger }

    simulateRounds(monkeys, 10000) {
        it.mod(mod)
    }

    val (topMonkey1, topMonkey2) = monkeys.map { it.inspections }.sortedDescending().take(2)
    return topMonkey1 * topMonkey2
}

fun main() = AoCTask("day11").run {
    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 10605.toBigInteger())
    check(part2(testInput) == 2713310158.toBigInteger())

    println(part1(input))
    println(part2(input))
}
