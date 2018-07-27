package forex.services.oneforge

import java.time.LocalTime

import forex.config.OneForgeConfig
import forex.domain.{ Price, Rate, Timestamp }
import forex.domain.Rate.Pair.allPairs
import fr.hmil.roshttp.HttpRequest
import monix.execution.Scheduler.Implicits.global

import scala.util.{ Failure, Success }
import fr.hmil.roshttp.response.SimpleHttpResponse

import scala.concurrent.Future

class Client(config: OneForgeConfig) {
  import config.{ baseUrl, key ⇒ apiKey }

  val allAsRequestStr: String =
    allPairs
      .map(p ⇒ p.from.toString + p.to.toString)
      .mkString(",")

  def request(pairs: String) = HttpRequest(s"$baseUrl/quotes?pairs=$pairs&api_key=$apiKey")

  def doGet(pair: Rate.Pair): Future[Error Either Rate] =
    request(allAsRequestStr)
      .send()
      .map(res ⇒ {
        println("Success with body:")
        println(res.body)
        Right(Rate(pair, Price(BigDecimal(100)), Timestamp.now)) // FIXME
      })
}
