package ru.hse.sd.cli.executor

import java.io.File
import java.nio.file.Path

object FileContentResources {
    val resourcesDirPath = Path.of("src", "test", "resources").toString()

    val engLoremFilename = Path.of(resourcesDirPath, "lorem.txt").toString()

    val rusLoremFilename = Path.of(resourcesDirPath, "лорем.txt").toString()

    val notExistsFilename = Path.of(resourcesDirPath, "not-exists.txt").toString()

    val notExistsDirectory = Path.of(resourcesDirPath, "not-exists").toString()

    val hellosFilename = Path.of(resourcesDirPath, "hellos.txt").toString()

    val engLorem = File(engLoremFilename).readText()

    val rusLorem = File(rusLoremFilename).readText()
}
