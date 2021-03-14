package phonebook

import java.io.File
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis


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

    execute("linear", people, directory, sortedDirectory)
    println()
    execute("jump", people, directory, sortedDirectory)
    println()
    execute("binary", people, directory, newSortedDirectory)
    println()
    execute("hash", people, directory, sortedDirectory)
}

fun execute(
    searchType: String,
    people: List<String>,
    directory: MutableList<String>,
    sortedDirectory: List<String>
) {
    var sortDuration = 0L
    val findSize = people.size
    var foundCount = 0
    var searchDuration = -1L

    when (searchType) {
        "linear" -> {
            println("Start searching (linear search)...")
            searchDuration = measureTimeMillis { foundCount = linearSearch(people, directory) }
        }
        "jump" -> {
            println("Start searching (bubble sort + jump search)...")
            //sortDuration = 102575133L (the real bubble sort time)
            sortDuration = 5039
            searchDuration = measureTimeMillis { foundCount = jumpSearch(people, sortedDirectory) }
        }
        "binary" -> {
            println("Start searching (quick sort + binary search)...")
            sortDuration = measureTimeMillis { quickSort(directory) }
            searchDuration = measureTimeMillis { foundCount = binarySearch(people, directory) }
        }
        "hash" -> {
            println("Start searching (hash table)...")
            val hashDirectory: HashMap<Int, String>
            sortDuration = measureTimeMillis { hashDirectory = createHashMap(directory) }
            searchDuration = measureTimeMillis { foundCount = hashSearch(people, hashDirectory) }
        }
    }

    val fullDuration = sortDuration + searchDuration
    printDuration("Found $foundCount / $findSize entries.", fullDuration)

    if (searchType != "hash" && searchType != "linear") {
        printDuration("Sorting time time:", sortDuration)
        printDuration("Searching time:", searchDuration)
    } else if (searchType == "hash"){
        printDuration("Creating time:", sortDuration)
        printDuration("Searching time:", searchDuration)
    }
}

fun createHashMap(directory: List<String>): HashMap<Int, String> {
    val hashDirectory = HashMap<Int, String>()

    for (person in directory) {
        hashDirectory[getPerson(person).hashCode()] = person
    }

    return hashDirectory
}

fun hashSearch(people: List<String>, hashDirectory: HashMap<Int, String>): Int {
    var foundCount = 0

    for (person in people) {
        if (hashDirectory[person.hashCode()] != null) foundCount++
    }

    return foundCount
}

fun linearSearch(people: List<String>, directory: List<String>): Int {
    var foundCount = 0

    for (person in people) {
        for (i in directory.indices) {
            val personName = getPerson(directory[i])
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
        val firstPerson = getPerson(sortedDirectory[index])

        if (person == firstPerson) {
            foundCount++
            continue
        }

        index += blockSize

        dirLoop@
        while (index < sortedDirectory.size) {
            val borderPerson = getPerson(sortedDirectory[index])

            if (borderPerson.toUpperCase() > person.toUpperCase()) {
                for (i in index downTo index - blockSize - 1) {
                    val dirPerson = getPerson(sortedDirectory[i])
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
            val dirPerson = getPerson(sortedDirectory[middle])

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

fun bubbleSort(directory: MutableList<String>): MutableList<String> {
    var endIndex = directory.size - 1
    var counter = 1

    while (endIndex > 0) {
        var swaps = 0
        for (i in 0 until endIndex) {
            val firstEntry = directory[i]
            val secondEntry = directory[i+1]
            val firstPerson = getPerson(directory[i])
            val secondPerson = getPerson(directory[i+1])
            if (firstEntry > secondEntry) {
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
        val jPerson = getPerson(directory[j])
        val pPerson = getPerson(directory[lastIndex])
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

    val left = directory.subList(0, i)
    val right = directory.subList(i + 1, lastIndex + 1)

    quickSort(left)
    quickSort(right)
}

/* Int, Int -> Int
 * Calculates the middle of the two edges. Rounded down to nearest Int via integer division
 */
fun getMiddle(leftEdge: Int, rightEdge: Int): Int {
    return (leftEdge + rightEdge) / 2
}

/* List<String>, Int -> String
 * Removes the phone number from a directory listing and returns the name associated with the number
 */
fun getPerson(directoryEntry: String): String {
    return directoryEntry.substringAfter(" ")
}

/* Long -> String
 * Calculates number of minutes, seconds, and milliseconds that make up the duration.
 */
fun calculateDuration(msDuration: Long): String {
    val minutes = msDuration / 60_000
    val seconds = (msDuration - (minutes * 60_000)) / 1000
    val milliseconds = (msDuration - (minutes * 60_000) - (seconds * 1000))

    return "$minutes min. $seconds sec. $milliseconds ms."
}

/* String, Long ->
 * Prints the message parameter alongside the calculated duration
 */
fun printDuration(message: String, duration: Long) {
    val durationMessage = calculateDuration(duration)
    println("$message $durationMessage")
}