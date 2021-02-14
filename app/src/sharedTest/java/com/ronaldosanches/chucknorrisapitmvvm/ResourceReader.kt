@file:Suppress("PackageDirectoryMismatch")

package com.ronaldosanches.chucknorrisapitmvvm

import java.io.InputStreamReader

class ResourceReader {
    companion object {
        operator fun invoke(filePath: String) : String {
            val reader = InputStreamReader(ResourceReader::class.java.classLoader?.getResourceAsStream(filePath))
            val fileContent : String = reader.readText()
            reader.close()
            return fileContent
        }
    }
}