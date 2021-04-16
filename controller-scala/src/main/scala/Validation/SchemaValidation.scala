import org.everit.json.schema.{Schema, ValidationException}
import org.everit.json.schema.loader.{SchemaClient, SchemaLoader}
import org.json.{JSONObject, JSONTokener}
import scala.io.Source
import scala.jdk.CollectionConverters._
import upickle.default._
import cats.implicits._
import scala.util.{Try, Success, Failure}
import upickle_import.general._

object SchemaValidation extends ValidationTrait {
  final def apply(
      endpointName: String,
      entityText: String
  ): Validated[ujson.Value] = {
    val entityObject: JSONObject = new JSONObject(entityText)

    val rawSchema: JSONObject = {
      val schemaSource: Source =
        Source.fromResource(s"schemas/$endpointName.json")

      val schemaString: String =
        try schemaSource.mkString
        finally schemaSource.close()

      new JSONObject(
        new JSONTokener(schemaString)
      )
    }

    val schema: Schema =
      SchemaLoader
        .builder()
        .schemaClient(SchemaClient.classPathAwareClient())
        .useDefaults(true)
        .schemaJson(rawSchema)
        .resolutionScope(schemaPath)
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
      case _ =>
        InvalidInputError(
          "Input validation failed for an unknown reason."
        ).invalidNec
    }
  }
}
