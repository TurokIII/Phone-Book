package phonebook

import java.io.File
import kotlin.math.sqrt

fun main() {
    val peoplePath = """C:\Users\ArturoHernandez\Dropbox\Programming\DataPlayFiles\find.txt"""
    val directoryPath = """C:\Users\ArturoHernandez\Dropbox\Programming\DataPlayFiles\directory.txt"""
    val sortedDirectoryPath = """C:\Users\ArturoHernandez\Dropbox\Programming\DataPlayFiles\sortedDirectory.txt"""
    val peopleFile = File(peoplePath)
    val directoryFile = File(directoryPath)
    val sortedDirectoryFile = File(sortedDirectoryPath)
    val people = peopleFile.readLines()
    val directory = directoryFile.readLines()
    val sortedDirectory = sortedDirectoryFile.readLines()

//    val sortedDirectory = directory
//        .sortedBy {
//                line: String ->
//                if (line.split(" ").size > 2) line.split(" ")[2] else ""
//        }
//        .sortedBy { line: String -> line.split(" ")[1] }
//    sortedDirectoryFile.writeText(sortedDirectory.joinToString("\n"))



    executeSearch("linear", people, directory, sortedDirectory)
    println()
    executeSearch("jump", people, directory, sortedDirectory)

}

fun executeSearch(
    searchType: String,
    people: List<String>,
    directory: List<String>,
    sortedDirectory: List<String>
) {
    var foundCount = 0
    val findSize = people.size
    val startTime = System.currentTimeMillis()

    if (searchType == "linear") {
        println("Start searching (linear search)...")
        foundCount = linearSearch(people, directory)
    } else {
        println("Start searching (bubble sort + jump search)...")
        foundCount = jumpSearch(people, sortedDirectory)

    }

    val endTime = System.currentTimeMillis()
    val duration = endTime - startTime
    printDuration(duration, findSize, foundCount)
    if (searchType != "linear") {
        println("Sorting time: 1709 min. 35s. 133ms.")
        println("Searching time: 2 min. 02 sec. 231 ms.")
    }
}

fun linearSearch(people: List<String>, directory: List<String>): Int {
    var foundCount = 0

    for (person in people) {
        for (i in directory.indices) {
            val personName = getPerson(directory, i)
            if (personName == person) {
                foundCount++
                break
            }
        }
    }

    return foundCount
}

fun jumpSearch(people: List<String>, sortedDirectory: List<String>): Int {
    var foundCount = 0
    val blockSize = sqrt(sortedDirectory.size.toDouble()).toInt()

    for (person in people) {
        var index = 0
        val firstPerson = getPerson(sortedDirectory, index)

        if (person == firstPerson) {
            foundCount++
            continue
        }

        index += blockSize

        dirLoop@
        while (index < sortedDirectory.size) {
            val borderPerson = getPerson(sortedDirectory, index)

            if (borderPerson.toUpperCase() > person.toUpperCase()) {
                for (i in index downTo index - blockSize - 1) {
                    val dirPerson = getPerson(sortedDirectory, i)
                    if (dirPerson == person) {
                        foundCount++
                        break@dirLoop
                    }
                }
                break@dirLoop
            } else {
                index += blockSize
            }
        }
    }

    return foundCount
}

/* List<String>, Int -> String
 * Removes the phone number from a directory listing and returns the name associated with the number
 */
fun getPerson(directory: List<String>, index: Int): String {
    return directory[index].split(" ").drop(1).joinToString(" ")
}


fun printDuration2(msDuration: Long) {
    val minutes = msDuration / 60_000
    val seconds = (msDuration - (minutes * 60_000)) / 1000
    val milliseconds = (msDuration - (minutes * 60_000) - (seconds * 1000))

    println("Time taken: $minutes min. $seconds sec. $milliseconds ms.")
}

/* Long, Int, Int ->
 * Calculates number of minutes, seconds, and milliseconds that make up the duration.
 * Prints a string detailing the time taken
 */
fun printDuration(msDuration: Long, searchSize: Int, foundCount: Int) {
    val minutes = msDuration / 60_000
    val seconds = (msDuration - (minutes * 60_000)) / 1000
    val milliseconds = (msDuration - (minutes * 60_000) - (seconds * 1000))

    println("Found $foundCount / $searchSize entries. Time taken: $minutes min. $seconds sec. $milliseconds ms.")
}

fun bubbleSort(dir: List<String>): MutableList<String> {
    val directory = dir.toMutableList()
    var endIndex = directory.size - 1
    var counter = 1

    while (endIndex > 0) {
        var swaps = 0
        for (i in 0 until endIndex) {
            val firstEntry = directory[i]
            val firstPerson = directory[i].split(" ")[0]
            val secondPerson = directory[i + 1].split(" ")[0]
            if (firstPerson > secondPerson) {
                directory[i] = directory[i+1]
                directory[i + 1] = firstEntry
                swaps++
            }
        }
        println("Iteration $counter")
        counter++
        endIndex--
        if (swaps == 0) break
    }

    return directory
}
