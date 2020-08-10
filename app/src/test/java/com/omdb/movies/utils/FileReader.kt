package com.omdb.movies.utils

import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

class FileReader {
  companion object {

    fun readFileAsString(jsonPath: String): String {
      val buf = StringBuilder()

      val kotlinBuildClassesFolder =
        FileReader::class.java.protectionDomain!!.codeSource.location.path
      val assetsPath = kotlinBuildClassesFolder.replace(
        "/build/tmp/kotlin-classes/debugUnitTest/",
        "/src/test/assets/api_mocks/$jsonPath"
      )

      val inputStream = FileInputStream(assetsPath)
      val bufferedReader = BufferedReader(InputStreamReader(inputStream))

      var str: String? = bufferedReader.readLine()
      while (str != null) {
        buf.append(str)
        str = bufferedReader.readLine()
      }
      inputStream.close()
      bufferedReader.close()
      return buf.toString()
    }
  }
}