#!/bin/bash

//usr/bin/env echo '
/**** BOOTSTRAP kscript ****\'>/dev/null
command -v kscript >/dev/null 2>&1 || curl -L "https://git.io/fpF1K" | bash 1>&2
exec kscript $0 "$@"
\*** IMPORTANT: Any code including imports and annotations must come after this line ***/

import java.io.File
import kotlin.system.exitProcess

if (args.isEmpty()) {
    println("Please, provide the day number you want to create:")
}
var day: String = args.getOrElse(0) { readLine() ?: "" }
while (day.toIntOrNull() !in 1..25) {
    println("Please provide a valid number (1..25):")
    day = readLine() ?: ""
}

val paddedDay = day.toString().padStart(2, '0')

val dir = File("src/day$paddedDay")

if (dir.exists()) {
    println("Day $day is already set up.")
} else {
    val templateDir = File("src/day00")
    templateDir.copyRecursively(dir)
    dir.listFiles().forEach { file ->
        val newContent = file.readText().replace("00", "$paddedDay").replace("<link>", "https://adventofcode.com/2022/day/${day}")
        val newName = file.name.replace("00", paddedDay)
        val newFile = file.resolveSibling(newName)
        newFile.writeText(newContent)
        file.delete()
    }
}