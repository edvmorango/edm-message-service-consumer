package service

import effects.Logger
import effects.repository.MessageRepository

object Environment {

  type UserMessageServiceEnvironment = Logger with MessageRepository

}
