package forex.config

import org.zalando.grafter.macros._

import scala.concurrent.duration.FiniteDuration

@readers
case class ApplicationConfig(
    akka: AkkaConfig,
    api: ApiConfig,
    oneForge: OneForgeConfig,
    forexProxy: ForexProxyConfig,
    executors: ExecutorsConfig
)

case class AkkaConfig(
    name: String,
    exitJvmTimeout: Option[FiniteDuration]
)

case class ApiConfig(
    interface: String,
    port: Int
)

case class ExecutorsConfig(
    default: String
)

case class OneForgeConfig(
    apiKey: String,
    baseUrl: String
)

case class ForexProxyConfig(
    ttl: FiniteDuration
)
