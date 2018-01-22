
/**
  * simple main with two clients that sends content
  */
object Main {
  def main(args: Array[String]): Unit = {
    val server = new ChatServer()
    val marco = new ChatClient(server,"marco","ciao")
    val matteo = new ChatClient(server,"matteo","ora")
    marco.register()
    matteo.register()
    marco.send()
    matteo.send()
    marco.logout()
    matteo.logout()
    server.shutdown()
  }
}
