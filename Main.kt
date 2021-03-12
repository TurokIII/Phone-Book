package phonebook

import java.io.File
import kotlin.math.sqrt

fun main() {
    val peoplePath = """C:\Users\ArturoHernandez\Dropbox\Programming\DataPlayFiles\find.txt"""
    val directoryPath = """C:\Users\ArturoHernandez\Dropbox\Programming\DataPlayFiles\directory.txt"""
    val sortedDirectoryPath = """C:\Users\ArturoHernandez\Dropbox\Programming\DataPlayFiles\sortedDirectory.txt"""
    val newSortDirectoryPath = """C:\Users\ArturoHernandez\Dropbox\Programming\DataPlayFiles\newSortedDirectory.txt"""
    val peopleFile = File(peoplePath)
    val directoryFile = File(directoryPath)
    val sortedDirectoryFile = File(sortedDirectoryPath)
    val newSortDirectoryFile = File(newSortDirectoryPath)
    val people = peopleFile.readLines()
    val directory = directoryFile.readLines().toMutableList()
    val sortedDirectory = sortedDirectoryFile.readLines()
    val newSortedDirectory = newSortDirectoryFile.readLines()

//    val sortedDirectory = directory
//        .sortedBy {
//                line: String ->
//                if (line.split(" ").size > 2) line.split(" ")[2] else ""
//        }
//        .sortedBy { line: String -> line.split(" ")[1] }
//    sortedDirectoryFile.writeText(sortedDirectory.joinToString("\n"))



    executeSearch("linear", people, directory, sortedDirectory)
    println()
    //executeSearch("jump", people, directory, sortedDirectory)
    //executeSearch("binary", people, directory, newSortedDirectory)
    println()
    //val testList = mutableListOf(17, 25, 11, 16, 10, 13, 22, 14)
    //quickSort(testList)
    //println("TESTLIST $testList")

    val startTime = System.currentTimeMillis()
    quickSort(directory)
    val endTime = System.currentTimeMillis()
    val duration = endTime - startTime
    printDuration(duration, 500, 500)
    //newSortDirectoryFile.writeText(directory.joinToString("\n"))

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

    when (searchType) {
        "linear" -> {
            println("Start searching (linear search)...")
            foundCount = linearSearch(people, directory)
        }
        "jump" -> {
            println("Start searching (bubble sort + jump search)...")
            foundCount = jumpSearch(people, sortedDirectory)
        }
        "binary" -> {
            println("Start searching (quicksort + binary search)...")
            foundCount = binarySearch(people, sortedDirectory)
        }
    }


    val endTime = System.currentTimeMillis()
    val duration = endTime - startTime
    printDuration(duration, findSize, foundCount)
//    if (searchType != "linear") {
//        println("Sorting time: 1709 min. 35s. 133ms.")
//        println("Searching time: 2 min. 02 sec. 231 ms.")
//    }
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

/* List<String>, List<String> -> Int
 * Iterates through the persons in the people List, and then uses binary search
 * to look for them in the directory
 */
fun binarySearch(people: List<String>, sortedDirectory: List<String>): Int {
    var foundCount = 0

    for (person in people) {
        var leftEdge = 0
        var rightEdge = sortedDirectory.size - 1
        var middle = getMiddle(leftEdge, rightEdge)

        while (true) {
            val dirPerson = getPerson(sortedDirectory, middle)

            if (dirPerson == person) {
                foundCount++
                break
            }

            // left and right are equal, middle checked. Person is not in directory
            if (leftEdge == rightEdge) break

            if (dirPerson < person) {
                leftEdge = middle + 1
            } else {
                rightEdge = middle - 1
            }

            middle = getMiddle(leftEdge, rightEdge)
        }
    }

    return foundCount
}

/* Int, Int -> Int
 * Calculates the middle of the two edges. Rounded down to nearest Int via integer division
 */
fun getMiddle(leftEdge: Int, rightEdge: Int): Int {
    return (leftEdge + rightEdge) / 2
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

fun quickSort(directory: MutableList<String>) {
    //val directory = dir.toMutableList()
    val lastIndex = directory.lastIndex

    if (directory.size < 2)
        return

    val pivot = directory[lastIndex]
    var i = 0

    for (j in 0..directory.size - 2) {
        val jPerson = getPerson(directory,j)
        val pPerson = getPerson(directory, lastIndex)
        if (jPerson <= pPerson) {
            val temp = directory[j]
            directory[j] = directory[i]
            directory[i] = temp
            i++
        }
    }

    // Swap pivot with i
    directory[lastIndex] = directory[i]
    directory[i] = pivot
    println("PART   $directory")

    val left = directory.subList(0, i)
    val right = directory.subList(i + 1, lastIndex + 1)
    println("PIVOT   $pivot")
    println("LEFT:   $left")
    println("RIGHT:   $right")

    quickSort(left)
    quickSort(right)
}

/* List<String>, Int -> String
 * Removes the phone number from a directory listing and returns the name associated with the number
 */
fun getPerson(directory: List<String>, index: Int): String {
    return directory[index].split(" ").drop(1).joinToString(" ")
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