package forex.services.oneforge

import java.time.{Instant, LocalTime, OffsetDateTime, ZoneId}

import forex.config.OneForgeConfig
import forex.domain.OneForge.Quote
import forex.domain.{Currency, Price, Rate, Timestamp}
import forex.domain.Rate.Pair.allPairs
import fr.hmil.roshttp.HttpRequest
import monix.execution.Scheduler.Implicits.global

import io.circe.parser._

import scala.concurrent.Future

class Client(config: OneForgeConfig) {
  import config.{ baseUrl, key ⇒ apiKey }

  val allAsRequestStr: String =
    allPairs
      .map(p ⇒ p.from.toString + p.to.toString)
      .mkString(",")

  def request(pairs: String) = HttpRequest(s"$baseUrl/quotes?pairs=$pairs&api_key=$apiKey")

  def rawGetAll: Future[List[Quote]] =
    request(allAsRequestStr)
      .send()
      .map(res ⇒ {
        println("Success with body:")
        println(res.body)
        decode[List[Quote]](res.body).right.get // FIXME
      })

  def getAll: Future[List[(Rate.Pair, Rate)]] =
    rawGetAll.map(_.map(el => {
      val pair = Rate.Pair(
        Currency.fromString(el.symbol.take(3)),
        Currency.fromString(el.symbol.drop(3))
      )
      val rate = Rate(
        pair,
        Price(el.price),
        Timestamp(OffsetDateTime.ofInstant(Instant.ofEpochSecond(el.timestamp), ZoneId.of("UTC"))) // brr
      )
      pair -> rate
    }))
}
