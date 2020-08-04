import cats._
import cats.data._
import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts

object Main {
    implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

    val xa = Transactor.fromDriverManager[IO](
        "org.postgresql.Driver",
        s"jdbc:postgresql:${System.getEnv("POSTGRES_DATABASE")}",
        System.getEnv("POSTGRES_USER"),
        System.getEnv("POSTGRES_PASSWORD"),
        Blocker.liftExecutionContext(ExecutionContexts.synchronous)
    )

    def run(args: List[String]): IO[ExitCode] = {
        println(
            UsersServices.createOrUpdate(
                uuid = java.util.UUID.fromString("c1db1203-23ca-42da-8219-aa0b2442e0fb"),
                user_id = 1,
                user = Users(
                    id = 0,
                    uuid = java.util.UUID.fromString("c1db1203-23ca-43da-8219-aa0b2442e0fb"),
                    person_id = 0,
                    username = "unnnn",
                    password = "pwwwww"
                ),
            person = People(
                id = 0,
                record_id = 2,
                first_name = "fn",
                last_name = "ln",
                other_names = Some("otn"),
                sex_id = 1,
                email_address = "test@example.com",
                phone_number = "0444444444",
                address_line_one = "34 fkldsj st",
                address_line_two = "slkdf alskdjf",
                zip = "2000"
            )
            )
        )
    }
}

// UsersDAO.upsert(Users(0, java.util.UUID.fromString("c1db1203-23ca-43da-8219-aa0b2442e0fb"), 2, "IEMMaMMMRYDS", "bCCCCb"))
// RecordsDAO.upsert(uuid = java.util.UUID.fromString("c1db1203-23ca-43da-8219-aa0b2442e0fb"), user_id = 2)
// PeopleDAO.upsert(People(
//     id = 0,
//     record_id = 2,
//     first_name = "fn",
//     last_name = "ln",
//     other_names = Some("otn"),
//     sex_id = 1,
//     email_address = "test@example.com",
//     phone_number = "0444444444",
//     address_line_one = "34 fkldsj st",
//     address_line_two = "slkdf alskdjf",
//     zip = "2000"
// ))

// import cats.effect._
// import org.http4s.server.blaze._
//
// object Main extends IOApp {
//
//     def run(args: List[String]): IO[ExitCode] = {
//         Loader.setup()
//
//         BlazeServerBuilder[IO]
//             .bindHttp(
//                 System.getenv("CONTROLLER_PORT").toInt,
//                 System.getenv("DOCKER_LOCALHOST")
//             )
//             .withHttpApp(Routes.service)
//             .serve
//             .compile
//             .drain
//             .as(ExitCode.Success)
//     }
// }
//
// UsersServices.createOrUpdate(
//     id = 0,
//     user_id = 1,
//     user = Users(0, 0, "un", "pw"),
//     person = People(
//         id = 0,
//         record_id = 0,
//         first_name = "fn",
//         last_name = "ln",
//         other_names = Some("on"),
//         sex_id = 2,
//         email_address = "test@example.com",
//         phone_number = "0444444444",
//         address_line_one = "43 lfkjasd st",
//         address_line_two = "aslfj alksjdf",
//         zip = "1111"
//     )
// )
