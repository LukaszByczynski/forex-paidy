Recruitment task for Paidy described in Forex.md

# Initial thoughts 
We got 9 currencies. Count possible pairs: !9/!7 => 9*8=72. Order matters so 144.

10.000 proxy requests per day = 6.9(4) per minute, once per 8.643042351 seconds

3rd party free tier limitation: 1,000 requests per day => once per 1,44 minutes

Data is valid for 5 minutes. with so little requests per sec. there is not much data to store. DB not necessary. Recovery of the data may just consume more request to 3rd party.

# Plan:
1. fire Rate request with dummy implementation ~write first test
2. write a client to 1Forge
3. rates source as a separate entity with already cached 1Forge
4. refactor and improve API feedback for bad scenarios

# Implementation

Background task exists which synchronizes with 1Forge every 4 minutes.
That gives a smallest latency for getting the rates from the proxy.

ApiKey, baseUrl and sync period are configurable. Smallest TimeUnit is a Second.

Git not handled as for production development. (no feature branches, direct pushes to master, no issues tracking etc.)

#Endpoints

Rate for pair Euro and Yen:
```bash
curl http://localhost:8888/?from=EUR&to=JPY
```

Returned time is in zone UTC.

Allowed currencies: AUD, CAD, CHF, EUR, GBP, NZD, JPY, SGD, USD

If server gots a problem with synchronizing with 1Forge you'll receive:
```
 HttpResponse(500 Internal Server Error,List(),HttpEntity.Strict(text/plain; charset=UTF-8,Proxy error.Service malfunction. Rate too old. Last known Rate: Rate(Pair(EUR,JPY),Price(1),Timestamp(2018-07-30T07:17:18Z)))
```

# Could do
- reduce tech debt in tests, more Unit testing instead of Integration testing  
- cover more endpoints from 1Forge,
- extend supported currencies list. ~needs to check the max number of pairs in 1Forge Quote endpoint
- use some more functional HTTP Client, Future is faux pa(eg. sttp or/and Akka HTTP)
- DevOps CI, deployment etc.
- make 1Forge key config more secure,