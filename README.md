Recruitment task for Paidy described in [Forex.md](Forex.md)

# Using
To run the tests:
```
sbt test
```
To start the service:
```
sbt run
```
# Endpoints

Ask for a rate for pair of Euro and Yen:
```bash
curl http://localhost:8888/?from=EUR&to=JPY
```
Response:
```
{"from":"EUR","to":"JPY","price":130.8305,"timestamp":"2018-07-31T18:40:36Z"}
```

Returned time is in the UTC zone with seconds precision.

Allowed currencies: AUD, CAD, CHF, EUR, GBP, NZD, JPY, SGD, USD

If server gots a problem with synchronizing with 1Forge you'll receive:
```
 500 Internal Server Error, text/plain; charset=UTF-8, "Proxy error.Service malfunction. Rate too old. Last known Rate: Rate(Pair(EUR,JPY),Price(1),Timestamp(2018-07-30T07:17:18Z))"
```
# Log:
## Thoughts 
We got 9 currencies. Count possible pairs: !9/!7 => 9*8=72. Order matters so 144.

10.000 proxy requests per day = 6.9(4) per minute, once per 8.643042351 seconds

3rd party free tier limitation: 1,000 requests per day => once per 1,44 minutes

Data is valid for 5 minutes. with so little requests per sec. there is not much data to store. DB not necessary. Recovery of the data may just consume more request to 3rd party.

Limit for a URL is about 2080. In one request request may contain all pairs for about 17 Currencies (without Pair ordering about 24).

For much more currencies... I would need to change the architeture of this solution :(. Is it possible to retain so low letancy for all currencies supported by 1Forge? Perhaps fast letancy for common pairs and normal cache for exotic currencies. Checked: 1Forge supports about 30 currencies... too much for free tier but with multiple free keys or a paid account it's possible to retain low letancy for all Currencies (3-4 instead of 1 periodic requests).

## Plan:
1. fire Rate request with dummy implementation ~write first test
2. write a client to 1Forge
3. rates source as a separate entity with already cached 1Forge
4. refactor and improve API feedback for bad scenarios

## Implementation

Background task exists which synchronizes with 1Forge every 4 minutes.
That gives a smallest latency for getting the rates from the proxy.

ApiKey, baseUrl and sync period are configurable. Smallest TimeUnit is a Second.

Git not handled as for production development. (no feature branches, direct pushes to master, no issues tracking etc.)

# Could do
- reduce tech debt in tests, more Unit testing instead of Integration testing  
- cover more endpoints from 1Forge,
- extend supported currencies list
- use some more functional HTTP Client, Future is faux pa(eg. sttp or/and Akka HTTP)
- DevOps CI, deployment etc.
- make 1Forge key config more secure,
