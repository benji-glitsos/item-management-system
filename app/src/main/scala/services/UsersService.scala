import scala.concurrent._

object UsersService extends UsersDAO with Connection {
    def list(rows: Int, page: Int): Future[Seq[UserFull]] = {
        db.run(list(rows, page)) // TODO: make the last page return a full list of the last items rather than nothing. requires maths.
    }

    def show(id: Int): Future[Option[User]] = {
        db.run(show(id))
    }

    def delete(id: Int): Future[Int] = {
        db.run(item(id).delete)
    }

    // def delete(id: Int): Future[Int] = {
    //     users.filter(_.id === id).deleted_at.update(Seeder.currentTimestamp()).run
    // }
}
