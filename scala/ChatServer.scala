import java.util.concurrent.{ConcurrentHashMap, Executors, Future}

import scala.collection.JavaConverters._
import scala.collection.convert.Wrappers.ConcurrentMapWrapper

/**
  * Server implemented by a Publish-subscribe pattern, clients sends commands, the server executes and reply.
  * Clients can Register, Login, Logout, Publish contents. When clients register they also start receiving contento from
  * other users. It is easy to divide contents in categories (or stanzas) and selectively receive a part of the content.
  * Also is simple implement a remote porting.
  */
class ChatServer {

  private val users = new ConcurrentMapWrapper[String, User](new ConcurrentHashMap[String, User] asScala)
  private val executor = Executors.newCachedThreadPool()

  /**
    * register an user to a chat
    */
  private def register(command: Command): Response = {
    command.getUser match {
      case Some(u) =>
        u.switchLogged()
        u.setCallback(command.getCallback match {
          case Some(c) => c
          case _ => return new Response(ResponseCode.NotValid)
        })
        users.put(u.name, u)
        return new Response(ResponseCode.Ok)
    }
    new Response(ResponseCode.NotValid)
  }

  /**
    * log-in a user into a chat
    */
  private def login(command: Command): Response = {
    val response = command.getUser match {
       case Some(u) =>
         if (u.equals(users.get(u.name)))
           if (!users.get(u.name).isLogged) {
             users.get(u.name).switchLogged()
             new Response(ResponseCode.Ok)
           }
           else new Response(ResponseCode.AlreadyLogged)
         else new Response(ResponseCode.NotValid)
       case _ => new Response(ResponseCode.NotValid)
    }
    response
  }

  /**
    * remove an user from the chat
    */
  private def logout(command: Command): Response = {
    command.getUser match {
      case Some(u) =>
        if (u.equals(users.get(u.name)))
          if (users.get(u.name).isLogged) {
            users.get(u.name).switchLogged()
            new Response(ResponseCode.Ok)
          }
          else new Response(ResponseCode.NotLogged)
        else new Response(ResponseCode.NotValid)
      case _ => new Response(ResponseCode.NotValid)
    }

  }

  /**
    * sends content to other users
    */
  private def send(command: Command): Response = {
    command.getUser match {
      case Some(u) =>
        if (u.equals(users.get(u.name)))
          if (users.get(u.name).isLogged) {
            users.forEach((name, user) =>
              if (!u.name.equals(name))
                user.send(command.getMessage)
            )
            new Response(ResponseCode.Ok)
          } else new Response(ResponseCode.NotLogged)
          else new Response(ResponseCode.NotValid)
      case _ =>  new Response(ResponseCode.NotValid)
    }
   }

  /**
    * easy receive, execute and reply function (async)
    */
  def submit(request: Command): Future[Response] = {
    executor.submit(() => {
      request.code match {
        case CommandCode.Register => register(request)
        case CommandCode.Login => login(request)
        case CommandCode.Logout => logout(request)
        case CommandCode.Send => send(request)
        case _ => new Response(ResponseCode.NotValid)
      }
    })
  }

  def shutdown(): Unit = {
    executor.shutdown()
  }

}
