package com.scienaptic.common

import com.redis._

object RedisUtils {

  val r = new RedisClient("localhost", 6379)

  def read(key: String): Option[String] = r.get(key)

  def write(key: String, str: String): Boolean = r.set(key, str)

}
