package effects

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

package object consumer {

  implicit val system: ActorSystem = ActorSystem("sns-alpakka")
  implicit val materializer: ActorMaterializer =
    ActorMaterializer.create(system)

}
