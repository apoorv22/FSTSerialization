package com.scienaptic.pmmlold

import java.io.{ FileInputStream, InputStream }

import com.scienaptic.common.{ Base64Codec, RedisUtils }
import javax.xml.transform.Source
import org.dmg.pmml.{ Model, PMML }
import org.jpmml.evaluator.{ ModelEvaluator, ModelEvaluatorFactory }
import org.jpmml.model.filters.ImportFilter
import org.jpmml.model.visitors.{ LocatorNullifier, StringInterner }
import org.jpmml.model.{ JAXBUtil, SAXUtil }

import scala.util.Try

object WritePMML extends App {

  override def main(args: Array[String]): Unit = {
    val key = "pmml-1234"
    val filePath =
      "/home/apoorv/Downloads/underwrite/single_iris_logreg_41.xml"
    write(key, filePath)
  }

  def getPMMLEvaluator(pmmlFilePath: String): Try[ModelEvaluator[_ <: Model]] =
    Try {
      val is: InputStream = new FileInputStream(pmmlFilePath)
      val source: Source  = SAXUtil.createFilteredSource(is, new ImportFilter())
      JAXBUtil.unmarshalPMML(source)
    }.map { pmml =>
      new StringInterner().applyTo(pmml)

      /**
        * PMML model contains SAX Locator information because of which evaluator cannot be serialized.
        * This information is set to null using LocatorNullifier.
        */
      // http://javadox.com/org.jpmml/pmml-model/1.2.11/org/jpmml/model/visitors/LocatorNullifier.html
      new LocatorNullifier().applyTo(pmml)
      ModelEvaluatorFactory.newInstance.newModelEvaluator(pmml)
    }

  def write(key: String, filePath: String): Try[Boolean] =
    for {
      evaluator <- getPMMLEvaluator(filePath)
      string    <- Base64Codec.encode(evaluator)
    } yield {
      evaluator.getInputFields.forEach(x => println(x.getName.getValue))
      RedisUtils.write(key, string)
    }
}
