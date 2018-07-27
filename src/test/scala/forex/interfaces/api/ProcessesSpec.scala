package forex.interfaces.api

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.testkit.ScalatestRouteTest
import forex.domain.Currency
import forex.interfaces.api.rates.Protocol.GetApiResponse
import forex.interfaces.api.utils.ApiMarshallers._
import forex.main.{Processes, Runners}
import org.scalatest.{Matchers, WordSpec}


class ProcessesSpec extends WordSpec with Matchers with ScalatestRouteTest {

  "The configured service" should {

    "return Rate with correct syntax" in {

      val ratesRoutes = rates.Routes(Processes(), Runners())
      val route = Routes(ratesRoutes).route

      val from = Currency.fromString("EUR")
      val to = Currency.fromString("JPY")

      val query = Uri.Query("from" → from.toString, "to" → to.toString)
      val uri = Uri("/").withQuery(query)


      Get(uri) ~> route ~> check {
        val given = responseAs[GetApiResponse]
        given.from shouldEqual from
        given.to shouldEqual to
      }

    }
  }
}