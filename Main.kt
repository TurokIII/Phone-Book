package phonebook

import java.io.File

fun main() {
    val peoplePath = """C:\Users\ArturoHernandez\Dropbox\Programming\DataPlayFiles\find.txt"""
    val directoryPath = """C:\Users\ArturoHernandez\Dropbox\Programming\DataPlayFiles\directory.txt"""
    val peopleFile = File(peoplePath)
    val directoryFile = File(directoryPath)
    val people = peopleFile.readLines()
    val directory = directoryFile.readLines()
    val searchSize = people.size

    val dirSmall = directory.slice(0..50000)
    val startTime = System.currentTimeMillis()

    //println(small)
    val smallSorted = bubbleSort(dirSmall)
    println(smallSorted)
    val endTime = System.currentTimeMillis()
    val duration = endTime - startTime
    println(printDuration2(duration))



//    var foundCount = 0
//
//    println("Start searching...")

//    for (person in people) {
//        for (listing in directory) {
//            val personName = listing.split(" ").drop(1).joinToString(" ")
//            if (personName == person) {
//                foundCount++
//                break
//            }
//        }
//    }
//
//    val endTime = System.currentTimeMillis()
//    val duration = endTime - startTime
//
//    printDuration(duration, searchSize, foundCount)
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
        //println("Iteration $counter: $people")
        counter++
        endIndex--
        if (swaps == 0) break
    }

    return directory
}
