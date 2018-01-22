
/**
  * possible server response
  */
object ResponseCode extends Enumeration{
  val Ok, NotLogged, NotValid, AlreadyLogged = Value
}

/**
  * abstraction of a server response
  */
class Response(responseCode: ResponseCode.Value) {

  val response: ResponseCode.Value = responseCode


}
