import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.Path
import java.nio.file.Paths
import java.time.Duration
import kotlin.io.path.createTempFile
import kotlin.io.path.exists
import kotlin.io.path.writeText

fun inputFile(input: String, trimIndent: Boolean = true, printInput: Boolean = true): Path {
    val file = createTempFile(prefix = "aoc", suffix = input.hashCode().toString())

    if (trimIndent) {
        file.writeText(input.trimIndent())
    } else {
        file.writeText(input)
    }

    if (printInput) {
        println("---START---")
        for (line in InputParser.parseLines(file.toString())) {
            println(line)
        }
        println("----END----")
    }

    return file
}

fun downloadInput(year: Int, day: Int): Path {
    val dayFormatted = day.toString().padStart(2, '0')
    val filePath = Paths.get("input/$year/$dayFormatted.txt")

    if (filePath.exists()) {
        println("File already downloaded: $filePath")
        return filePath
    }

    // To get the cookie, download an input file and check the request headers: https://adventofcode.com/2023/day/7
    // It should look like 'session=some-string'. Copy the whole value.
    // To set this project-wide: Run configs -> Edit configurations -> Edit configuration templates
    val sessionCookie = System.getenv("AOC_SESSION_COOKIE")!!

    val request =
        HttpRequest
            .newBuilder()
            .uri(URI.create("https://adventofcode.com/$year/day/$day/input")) // Note: day is not padded
            .header("Cookie", sessionCookie)
            .header("User-Agent", "https://github.com/willfenton/adventofcode")
            .timeout(Duration.ofSeconds(10))
            .GET()
            .build()

    val response = HttpClient.newHttpClient().send(request, BodyHandlers.ofString())

    if (response.statusCode() != 200) {
        throw Exception("Non-200 status when downloading input file: ${response.statusCode()}")
    }

    filePath.writeText(response.body())

    return filePath
}
