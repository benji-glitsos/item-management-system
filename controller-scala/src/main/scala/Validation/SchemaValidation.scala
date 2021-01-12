import org.everit.json.schema.{Schema, ValidationException}
import org.everit.json.schema.loader.{SchemaClient, SchemaLoader}
import akka.http.scaladsl.server._
import org.json.{JSONObject, JSONTokener}
import scala.io.Source
import scala.jdk.CollectionConverters._
import akka.http.scaladsl.server.Directives._
import scala.concurrent.duration._
import akka.http.scaladsl.model.HttpEntity
import upickle.default._
import cats.implicits._
import cats.data.Validated.{Valid, Invalid}
import scala.util.{Try, Success, Failure}
import upickle_bundle.general._

object SchemaValidation extends ValidationTrait {
  final def apply(
      endpointName: String,
      entityText: String
  ): Validated[ujson.Value] = {
    val entityObject: JSONObject = new JSONObject(entityText)

    val schemaSource: Source =
      Source.fromResource(s"schemas/$endpointName.json")

    val schemaString: String =
      try schemaSource.mkString
      finally schemaSource.close()

    val rawSchema: JSONObject =
      new JSONObject(
        new JSONTokener(schemaString)
      )

    val schema: Schema =
      SchemaLoader
        .builder()
        .schemaClient(SchemaClient.classPathAwareClient())
        .useDefaults(true)
        .schemaJson(rawSchema)
        .resolutionScope("classpath://schemas/")
        .draftV7Support()
        .build()
        .load()
        .build()

    Try(schema.validate(entityObject)) match {
      case Success(_) => {
        read[ujson.Value](entityObject.toString()).validNec
      }
      case Failure(e: ValidationException) =>
        e.getCausingExceptions()
          .asScala
          .map(e => InvalidInputError(e.getMessage()).invalidNec)
          .fold(ujsonEmptyValue.validNec) { (a, b) => a <* b }
    }
  }
}
