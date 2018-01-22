

/**
  * this class is used to model a callback in order to send content to the users
  */
class UserCallback(f: (String => Unit)){
  def send(message: String): Unit = {
    f(message)
  }
}

/**
  * this class models a generic user
  */
class User(userName: String, password: String) {

  val name: String = userName
  val pass: String = password
  private var logged: Boolean = false
  private var callback: Option[UserCallback] = None

  def isLogged: Boolean = {
    logged
  }

  def switchLogged(): Unit = {
    logged = !logged
  }

  override def equals(obj: scala.Any): Boolean = {
    obj match {
      case s: User => s.name.equals(name) && s.pass.equals(pass)
      case _ => false
    }
  }

  def setCallback(userCallback: UserCallback): Unit = {
    callback = Option(userCallback)
  }

  def send(message: String): Unit = {
    callback match {
      case Some(c) => c.send(message)
    }
  }

}
