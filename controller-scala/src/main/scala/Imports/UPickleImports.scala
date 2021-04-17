import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import upickle.default._
import akka.http.scaladsl.marshalling.{Marshaller, ToEntityMarshaller}
import akka.http.scaladsl.model.{HttpEntity}
import akka.http.scaladsl.model.MediaTypes.`application/json`
import org.typelevel.ci.CIString

package upickle_import {
  object general {
    implicit def OptionWriter[T: Writer]: Writer[Option[T]] =
      implicitly[Writer[T]].comap[Option[T]] {
        case None    => null.asInstanceOf[T]
        case Some(x) => x
      }

    implicit def OptionReader[T: Reader]: Reader[Option[T]] = {
      new Reader.Delegate[Any, Option[T]](
        implicitly[Reader[T]].map(Some(_))
      ) {
        override def visitNull(index: Int) = None
      }
    }

    implicit final val upickleLocalDateTime: ReadWriter[LocalDateTime] =
      readwriter[ujson.Value].bimap[LocalDateTime](
        x => x.toString,
        json => {
          val defaultFormat: DateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss.zzz");
          LocalDateTime.parse(json.toString(), defaultFormat)
        }
      )

    implicit final val upickleCaseInsensitive: ReadWriter[CIString] =
      readwriter[ujson.Value].bimap[CIString](
        cistring => cistring.toString.toLowerCase,
        json => CIString(json.toString())
      )

    implicit final val upickleUsersWithMeta: ReadWriter[UsersWithMeta] = macroRW
    implicit final val upickleUsersList: ReadWriter[UsersList]         = macroRW

    implicit final val upickleItemsWithMeta: ReadWriter[ItemsWithMeta] = macroRW
    implicit final val upickleItemsList: ReadWriter[ItemsList]         = macroRW

    implicit final val upickleError: ReadWriter[Error] = macroRW

    implicit final def upickleMarshaller: ToEntityMarshaller[ujson.Value] = {
      Marshaller.withFixedContentType(`application/json`) { json =>
        HttpEntity(`application/json`, write(json))
      }
    }

    implicit final def serialisedErrorsUpickleMarshaller
        : ToEntityMarshaller[SerialisedErrors] = {
      Marshaller.withFixedContentType(`application/json`) { serialisedErrors =>
        HttpEntity(
          `application/json`,
          write(
            ujson.Obj("errors" -> read[ujson.Value](serialisedErrors.errors))
          )
        )
      }
    }

    final val ujsonEmptyValue: ujson.Value = write(ujson.Obj())
  }
}
