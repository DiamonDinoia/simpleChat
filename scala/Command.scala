

/**
  * possible commands
  */
object CommandCode extends Enumeration{
    val Register, Login, Logout, Send = Value
}

/**
  * abstraction of a single command with communication protocol
  */
class Command(command: CommandCode.Value, username: String) {

  val code: CommandCode.Value = command
  val user: String = username
  private var password:Option[String] = None
  private var callback:Option[UserCallback] = None
  private var message: Option[String] = None

  def setPassword(password: String): Command ={
    this.password=Option(password)
    this
  }

  def getUser: Option[User] = {
    password match {
      case Some(s) => if (!s.equals("")) Option(new User(username, s)) else  None
      case None => None
    }
  }

  def isLogged(user: User): Boolean = {
    username.equals(user.name) && user.isLogged
  }

  def isValid(user: User): Boolean = {
    password match {
      case Some(s) => username.equals(user.name) && s.equals(user.pass)
      case _ => false
    }
  }

  def setCallback(userCallback: UserCallback): Command = {
    callback = Option(userCallback)
    this
  }

  def getCallback: Option[UserCallback] = {
    callback
  }

  def setMessage(text: String): Command ={
    message=Option(text)
    this
  }

  def getMessage: String = {
    message match {
      case Some(s) => s
      case _ => ""
    }
  }

}
