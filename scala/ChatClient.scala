
/**
  * Test client used only to check the server
  */
class ChatClient(server: ChatServer,name: String, password: String) {

  def receive(string: String): Unit ={
    println(name + " received " + string)
  }

  def register() = {
    var command = new Command(CommandCode.Register,name)
      .setPassword(password)
      .setCallback(new UserCallback(receive))
    server.submit(command).get()
  }

  def send() = {
    var command = new Command(CommandCode.Send,name)
      .setMessage("ciao io sono " + name)
      .setPassword(password)
    server.submit(command).get()
  }

  def logout() = {
    var command = new Command(CommandCode.Logout,name)
      .setPassword(password)
      .setCallback(new UserCallback(receive))
    server.submit(command).get()
  }

}
