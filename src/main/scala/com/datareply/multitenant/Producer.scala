package com.datareply.multitenant

import java.io.File
import scala.io.BufferedSource

object Producer {
  def main(args: Array[String]): Unit = {

    val data = new File("data"); // TODO: Extract config

    if (data.exists && data.isDirectory) {
      data.listFiles()
        .filter(_.isFile)
        .foreach(produceToKafka)
    }
  }

  def produceToKafka(file: File): Unit = {
    val bufferedSource: BufferedSource = io.Source.fromFile(file)
    bufferedSource.getLines().foreach(line => {
      Thread.sleep(1)
      produceToKafka(line)
    })
  }

  def produceToKafka(line: String): Unit = {
    val values: Array[String] = line.split(";") // TODO: Extract config

  }
}
