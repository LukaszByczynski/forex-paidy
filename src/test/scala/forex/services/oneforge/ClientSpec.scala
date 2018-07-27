package forex.services.oneforge
import forex.config.OneForgeConfig
import forex.domain.Rate
import forex.interfaces.api.rates.Converters
import org.scalatest.AsyncWordSpec

class ClientSpec extends AsyncWordSpec {

  "The 1Forge client" should {

    "return Rate with correct syntax" in {

      val client = OneForgeClientJS(
        OneForgeConfig(
          "4Zc8xsXNAU3N8eO1sHF83wkQO00buyKL",
          "https://forex.1forge.com/1.0.3"
        )
      )

      import Probe.{from, to}

      client.getAll.map(res => Converters.toRates( res.right.get))
        .map( res => {
          val given = res.find(_._1 == Rate.Pair(from, to)).get._1
          assert(given.from == from)
          assert(given.to == to)
        })

    }
  }

}
