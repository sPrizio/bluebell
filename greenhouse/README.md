# greenhouse

Current Iteration: **0.1.1**\
Current Release: **Pre-release**

---

### Avenues to Explore
1. Implement Krish's strategy and test
2. Look up Bagger's video on his strategy
3. See if we can expand Sprout to other indices


---

### Supported Platforms
- .csv Imports
- MetaTrader 4
- MetaTrader 5

### Supported Brokers
- CMC Markets
- FTMO

---

### Iterations
- **0.0.1** : Basic data modeling & Trade Import feature. Allows users with multiple trading accounts to import trades into their accounts from supported brokers.
- **0.0.2** : API Controller Authentication
- **0.0.3** : Testing Suite implementation & integration
- **0.0.4** : Forex Factory calendar integration
- **0.0.5** : Trade Record Integration
- **0.0.6** : Charting Implementation
- **0.0.7** : Integrating with **sepal**
- **0.0.8** : Import FTMO Results
- **0.0.9** : Major Code Improvements & Refactoring
- **0.1.0** : Major Testing Refactoring and Coverage Improvements
- **0.1.1** : (Current) Improve Api Documentation, Improve Lombok usage, Cronjob for fetching Market News, Refactor controller endpoints to use pojo's instead of Maps for Request Objects (Look into OpenAPI)
- **0.1.2** : (Planned) Refactoring of Portfolios and Account Details (work with sepal) including page for managing portfolios, selector for active portfolio, CRUD
- **0.1.3** : (Planned) Dockerize App (including front-end)
- **0.1.4** : (Planned) Store Market data from MT4 and FirstRateData into database
- **0.1.5** : (Planned) Automate fetching market data
- **0.1.6** : (Planned) Automate fetching account trades from MT4 (possibly using an API)
- **0.1.7** : (Planned) Improve ApexChart service to select data, parsers etc...
- **0.1.8** : (Planned) Improve Trade import service to include withdrawals and deposits (for withdrawal example, look at live CFD account), Accounts that have not been traded on for 1 calendar year should be marked as inactive (use a cronjob for this)