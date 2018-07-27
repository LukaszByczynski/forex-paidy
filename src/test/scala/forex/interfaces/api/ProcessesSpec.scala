package forex.interfaces.api

import java.util.concurrent.TimeUnit

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.testkit.ScalatestRouteTest
import forex.config.ForexProxyConfig
import forex.domain.OneForge.Quote
import forex.interfaces.api.rates.Protocol.GetApiResponse
import forex.interfaces.api.utils.ApiMarshallers._
import forex.main.{Processes, Runners}
import forex.services.oneforge.{ForexCachedProxy, OneForgeClient, Probe}
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration


class ProcessesSpec extends WordSpec with Matchers with ScalatestRouteTest {

  "The configured service" should {

    "return Rate with correct syntax" in {
      val proxy = ForexCachedProxy( // TODO maybe to manipulte with time for better testing
        ForexProxyConfig(FiniteDuration(1, TimeUnit.MILLISECONDS)),
        OneForgeClientStub
      )
      val ratesRoutes = rates.Routes(
        Processes(proxy),
        Runners()
      )
      val route = Routes(ratesRoutes).route

      import Probe.{from, to}

      val query = Uri.Query("from" → from.toString, "to" → to.toString)
      val uri = Uri("/").withQuery(query)

      Get(uri) ~> route ~> check {
        proxy.stopProxy()
        val given = responseAs[GetApiResponse]
        given.from shouldEqual from
        given.to shouldEqual to
      }

    }
  }
}

object OneForgeClientStub extends OneForgeClient {
  override def getAll: Future[QuotesResult] = Future.successful{
    Right(List(Quote("EURJPY", BigDecimal(1), BigDecimal(1), BigDecimal(1), System.currentTimeMillis())))
  }
}