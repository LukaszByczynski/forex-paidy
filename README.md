Recruitment task for paidy described in Forex.md

# thoughts 
We got 9 currencies. Count possible pairs: !9/!7 => 9*8=72

10.000 requests per day = 6.9(4) per minute, once per 8.643042351 seconds

3rd party free tier limitation: 1,000 requests per day => once per 1,44 minutes

Data is valid for 5 minutes. with so small requests per sec. there is not much data to store. DB not necessary. Recovery of the data may just consume more request to 3rd party.

# Plan:
1. fire Rate request with dummy implementation ~write first test
2. -||- with direct proxing to 1forge
3. change the rates source to separate entity with cycled requests to 1forge

# Implementation

Realized 1 and 3. Skipped 2

Background task exists which synchronizes with 1Forge every 4 minutes.
That gives a smallest latancy for getting the rates from the proxy.

ApiKey, baseUrl and sync period are configurable.

Git not handled as for production development. (no branches no direct pushes to master, etc.)

# Could do
- cover more endpoints from 1Forge,
- extend supported currencies, need to check max number of pairs in 1Forge Quote endpoint
- use some more funtional HTTP Client, Future is faux pas, sttp or Akka HTTP ?
- cover more corner cases , ex. now in case of 1Forge/network problems silently the outdated Rates are returned.