package ru.hse.sd.cli.env

import java.nio.file.Path

/**
 * Stores information about all the environment variables and the current working directory.
 */
class Environment {
    private val myVariables = mutableMapOf<String, String>()

    /**
     * Current working directory.
     */
    var workingDirectory: Path = Path.of("").toAbsolutePath()

    /**
     * All the current environment variables.
     */
    val variables: Map<String, String>
        get() = myVariables.toMap()

    /**
     * If [path] is absolute, then it is returned.
     * Otherwise, [workingDirectory] is used to resolve the actual full path of [path].
     */
    fun resolvePath(path: Path): Path = if (path.isAbsolute) path else workingDirectory.resolve(path)

    /**
     * Returns the value of [name] in the environment variables.
     * If it is not presented, then the empty string is returned.
     */
    fun getVariable(name: String): String = myVariables.getOrDefault(name, "")

    /**
     * Puts or replaces the value of [name] with [value] in the environment variables.
     */
    fun putVariable(name: String, value: String) {
        myVariables[name] = value
    }

    /**
     * @see getVariable
     */
    operator fun get(variableName: String): String = getVariable(variableName)

    /**
     * @see putVariable
     */
    operator fun set(variableName: String, variableValue: String) = putVariable(variableName, variableValue)
}
