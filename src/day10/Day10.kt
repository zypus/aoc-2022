package day10

import AoCTask

// https://adventofcode.com/2022/day/10

sealed class Operation(val cycles: Int) {
    data class AddX(val amount: Int): Operation(cycles = 2)
    object Noop: Operation(cycles = 1)

    companion object {
        fun fromString(string: String): Operation {
            return if (string == "noop") {
                Noop
            } else {
                val (_, amount) = string.split(" ")
                AddX(amount.toInt())
            }
        }
    }
}

data class ExecutionResult(val cycle: Int, val registerStartOfCycle: Int, val registerEndOfCycle: Int, val ss: Int)


private fun executeProgram(input: List<String>): List<ExecutionResult> {
    val operations = input.map { Operation.fromString(it) }
    val expandedOps = operations.flatMap {
        when (it) {
            is Operation.Noop -> listOf(Operation.Noop)
            is Operation.AddX -> listOf(Operation.Noop, it)
        }
    }

    val registerAndScores = expandedOps.scanIndexed(ExecutionResult(0, 1, 1, 0)) { previousCycle, (_, _, register, _), op ->
        val cycle = previousCycle + 1
        when (op) {
            is Operation.Noop -> ExecutionResult(cycle, register, register, cycle * register)
            is Operation.AddX -> ExecutionResult(cycle, register, register + op.amount, cycle * register)
        }
    }
    return registerAndScores
}

fun part1(input: List<String>): Int {
    val results = executeProgram(input)

    val padStartWith19DummyValues = (-19..-1).map { ExecutionResult(it, 0, 0,0) }
    val paddedResults = padStartWith19DummyValues + results
    val finalSignalStrengthScore = paddedResults
        .take(240)
        .windowed(40, 40)
        .map {
            it.last()
        }
        .sumOf {
            it.ss
        }

    return finalSignalStrengthScore
}


fun part2(input: List<String>): String {
    val cycles = executeProgram(input)

    val crt = cycles
        .drop(1)
        .take(240)
        .map { cycle ->
            val crtCursor = (cycle.cycle - 1) % 40
            if (crtCursor in (cycle.registerStartOfCycle - 1)..(cycle.registerStartOfCycle + 1)) {
                "#"
            } else {
                "."
            }
        }

    return crt
        .windowed(40, 40)
        .joinToString("\n") {
            it.joinToString("")
        }
}

fun main() = AoCTask("day10").run {
    // test if implementation meets criteria from the description, like:
    check(part1(testInput) == 13140)

    val expectedOutput = """
        ##..##..##..##..##..##..##..##..##..##..
        ###...###...###...###...###...###...###.
        ####....####....####....####....####....
        #####.....#####.....#####.....#####.....
        ######......######......######......####
        #######.......#######.......#######.....
    """.trimIndent()

    check(part2(testInput) == expectedOutput)

    println(part1(input))
    println(part2(input))
}
