package com.datareply.multitenant

import java.io.File
import java.net.URI
import java.net.http.{HttpClient, HttpRequest, HttpResponse}
import java.net.http.HttpClient.{Redirect, Version}
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers
import java.time.Duration
import scala.io.BufferedSource
import scala.io.Source;

object Producer {

  val httpClient :HttpClient = HttpClient.newBuilder()
    .version(Version.HTTP_1_1)
    .followRedirects(Redirect.NORMAL)
    .connectTimeout(Duration.ofSeconds(20))
    .build()

  def main(args: Array[String]): Unit = {

    val data = new File("data") // TODO: Extract config

    val apiEndpoint = args(0)

    println("Got apiEndpoint " + apiEndpoint)

    if (data.exists && data.isDirectory) {
      data.listFiles()
        .filter(_.isFile)
        .foreach(file => sendToApi(file, apiEndpoint))
    }
  }

  def sendToApi(file: File, apiEndpoint: String): Unit = {
    val bufferedSource: BufferedSource = Source.fromFile(file)
    bufferedSource.getLines()
      .map(_.split(";"))
      .filter((values: Array[String]) => "BME280".equals(values(1)))
      .foreach(values => {
        Thread.sleep(1)
        try {
          sendToApi(values, apiEndpoint)
        } catch {
          case e: NumberFormatException => e.printStackTrace()
        }
    })
  }

  def toInt(str: String): Option[Int] = {
    if (str == "") {
      Option.empty
    } else {
      Option(str.toInt)
    }
  }

  def sendToApi(values: Array[String], apiEndpoint: String): Unit = {
    val measurementBuilder: Measurement.Builder = Measurement.newBuilder()
      .setSensorId(values(0))
      .setSensorType(values(1))
      .setLocation(values(2).toInt)
      .setLat(values(3).toFloat)
      .setLon(values(4).toFloat)
      .setTimestamp(values(5))
      .setPressure(values(6).toFloat)
      .setTemperature(values(9).toFloat)
      .setHumidity(values(10).toFloat)

      toInt(values(7)).foreach(measurementBuilder.setAltitude(_))
      toInt(values(8)).foreach(measurementBuilder.setPressureSealevel(_))

    val measurement: Measurement = measurementBuilder.build()

    val response :HttpResponse[String] = httpClient.send(HttpRequest.newBuilder()
      .uri(URI.create(apiEndpoint + "measurements")) // API has an endpoint measurements to which you can POST // TODO: Add intelligent concatenation of url
      .header("Content-Type", "application/json")
      .POST(BodyPublishers.ofString(measurement.toString))
      .build(), BodyHandlers.ofString())

    // TODO: Make HTTP POST request to API.

    println("Sent measurement to API:")
    println(response)
  }
}
