Recruitment task for Paidy described in Forex.md

# Thoughts 
We got 9 currencies. Count possible pairs: !9/!7 => 9*8=72

10.000 requests per day = 6.9(4) per minute, once per 8.643042351 seconds

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

ApiKey, baseUrl and sync period are configurable.

Git not handled as for production development. (no feature branches, direct pushes to master, no issues tracking etc.)

#Endpoints

Rate for pair Euro and Yen:
```bash
curl http://localhost:8888/?from=EUR&to=JPY
```

Allowed currencies: AUD, CAD, CHF, EUR, GBP, NZD, JPY, SGD, USD

If server gots a problem with synchronizing with 1Forge you'll receive:
```
TODO
```

# Could do
- more tests,
- cover more endpoints from 1Forge,
- extend supported currencies, need to check max number of pairs in 1Forge Quote endpoint
- use some more functional HTTP Client, Future is faux pa(eg. sttp or/and Akka HTTP)
- cover more corner cases , ex. now in case of 1Forge/network problems silently the outdated Rates are returned.
- DevOps CI, deployment etc.