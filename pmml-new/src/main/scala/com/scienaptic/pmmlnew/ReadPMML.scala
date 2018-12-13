package com.scienaptic.pmmlnew

import com.scienaptic.common.{ Base64Codec, RedisUtils }
import org.jpmml.evaluator.Evaluator

object ReadPMML extends App {

  override def main(args: Array[String]): Unit = {
    val key = "pmml-1234"
    read(key)
  }

  def read(key: String): Option[Unit] =
    for {
      str       <- RedisUtils.read(key)
      evaluator <- Base64Codec.decode[Evaluator](str).toOption
    } yield {
      println("Decoded successfully")
      evaluator.getInputFields.forEach(x => println(x.getName.getValue))
    }

}
