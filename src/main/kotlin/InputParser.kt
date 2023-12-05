import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader

object InputParser {
    fun parseLines(filename: String): List<String> {
        val file = File(filename)

        val isValidFile = file.exists() && !file.isDirectory
        if (!isValidFile) {
            throw FileNotFoundException("Can't read $filename")
        }

        return FileReader(file).readLines()
    }
}
