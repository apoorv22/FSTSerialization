package com.scienaptic.common

import java.io._
import java.util.Base64

import scala.util.{ Failure, Try }

/**
  * Base64 encoding and decoding in Java 8 using Base64 variant known as Basic(RFC 4648).
  */
// https://stackoverflow.com/a/134918
object Base64Codec {

  /**
    * Encode object to String.
    *
    * @param o Object to be Base64 encoded
    * @param e Exception on failing to encode.
    * @tparam T Type of the object.
    * @return A String containing the resulting Base64 encoded characters
    */
  def encode[T](o: T): Try[String] =
    Try {
      val baos = new ByteArrayOutputStream()
      val oos  = new ObjectOutputStream(baos)
      oos.writeObject(o)
      oos.close()
      Base64.getEncoder.encodeToString(baos.toByteArray)
    }.recoverWith {
      case e: IOException =>
        Failure(e)
    }

  /**
    * Decode string to object of type T.
    *
    * @param s String to be Base64 decoded.
    * @param e Exception on failing to decode.
    * @tparam T Type of the object.
    * @return Object decoded from Base64 encoded string.
    */
  def decode[T](s: String): Try[T] =
    //TODO: Catch all exceptions
    Try {
      val bytes = Base64.getDecoder.decode(s)
      val ois   = new ObjectInputStream(new ByteArrayInputStream(bytes))
      val value = ois.readObject()
      ois.close()
      value.asInstanceOf[T]
    }
}
