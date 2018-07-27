package forex.services.oneforge
import forex.config.{ApplicationConfig, ForexProxyConfig}
import forex.domain.OneForge.Quote
import forex.domain.Rate
import org.zalando.grafter.macros.readerOf

import scala.concurrent.ExecutionContext.Implicits.global

@readerOf[ApplicationConfig]
case class ForexCachedProxy(config: ForexProxyConfig, client: Client) {
  private var quotes: Rate.Pair Map Rate = Map()

  private val task = {
    val t = new java.util.Timer()
    val task = new java.util.TimerTask {
      def run(): Unit = client.getAll.foreach(result =>
        if(result.nonEmpty)
          quotes = result.toMap
      )
    }
    t.schedule(task, 0L, config.ttl.toMillis)
    task
  }
  def stopProxy(): Unit = task.cancel()

  // FIXME initialize phase
  // FIXME bad paths

  def rate(pair: Rate.Pair): Rate = quotes(pair)
}
