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
    bufferedSource.getLines()
      .map(_.split(";"))
      .filter((values: Array[String]) => "BME280".equals(values(1)))
      .foreach(values => {
        Thread.sleep(1)
        produceToKafka(values)
    })
  }

  def produceToKafka(values: Array[String]): Unit = {
    Measurement.newBuilder()
      .setSensorId(values(0))
      .setSensorType(values(1))
      .setLocation(values(2).toInt)
      .setLat(values(3).toLong)
      .setLon(values(4).toLong)
      .setTimestamp(values(5))
      .setPressure(values(6).toLong)
      .setAltitude(values(7).toLong)
      .setPressureSealevel(values(8).toLong)
      .setTemperature(values(9).toLong)
      .setHumidity(values(10).toLong)
  }
}
