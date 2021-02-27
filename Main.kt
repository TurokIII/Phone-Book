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

    val startTime = System.currentTimeMillis()
    var foundCount = 0

    println("Start searching...")

    for (person in people) {
        for (listing in directory) {
            val personName = listing.split(" ").drop(1).joinToString(" ")
            if (personName == person) {
                foundCount++
                break
            }
        }
    }

    val endTime = System.currentTimeMillis()
    val duration = endTime - startTime

    printDuration(duration, searchSize, foundCount)
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
