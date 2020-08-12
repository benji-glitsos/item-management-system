import cats._
import cats.data._
import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import java.util.UUID
import java.time.LocalDateTime

object Main {
    implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

    val xa = Transactor.fromDriverManager[IO](
        "org.postgresql.Driver",
        s"jdbc:postgresql://${System.getenv("DATABASE_SERVICE")}/${System.getenv("POSTGRES_DATABASE")}",
        System.getenv("POSTGRES_USER"),
        System.getenv("POSTGRES_PASSWORD"),
        Blocker.liftExecutionContext(ExecutionContexts.synchronous)
    )

    case class Record2(
        id: Int,
        updated_by: Option[Int],
    )

    val program3: ConnectionIO[Int] =
      for {
        record <- sql"""
            INSERT INTO records (uuid, created_at, created_by)
            VALUES ('846bc87f-4efe-43cb-9a57-75e20f96db6f', '2020-08-11 08:28:09.517903', 1)
            ON CONFLICT (uuid)
            DO UPDATE SET
                  updated_at = EXCLUDED.created_at
                , updated_by = EXCLUDED.created_by
            RETURNING id, updated_by
            """.query[Record2].unique
        val isNew = record.updated_by.isDefined
        _ <- if (isNew) {
                sql"""
                WITH new_person AS (
                    INSERT INTO people (
                      record_id
                    , first_name
                    , last_name
                    , other_names
                    , sex_id
                    , email_address
                    , phone_number
                    , address_line_one
                    , address_line_two
                    , zip
                    ) VALUES (
                          ${record.id}
                        , 'fn'
                        , 'ln'
                        , 'on'
                        , 1
                        , 'test@example.com'
                        , '0444444444'
                        , '1 Test St'
                        , 'Sydney NSW'
                        , '2000'
                    ) RETURNING id
                )
                INSERT INTO users (person_id, username, password)
                VALUES (
                    (SELECT id FROM new_person)
                  , 'un'
                  , 'pw'
                )
                """.query[Unit].unique
            } else {
                sql"""
                UPDATE users SET
                    username = 'un'
                  , password = 'pw'
                WHERE person_id = 1
                """.query[Unit].unique
            }
      } yield record.id

    def main(args: Array[String]) {
        println(program3.transact(xa).unsafeRunSync)
    }
}
