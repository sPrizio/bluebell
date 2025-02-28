# bluebell - greenhouse
This module represents bluebell's backend. bluebell's back-end is built using Java Spring and comprises the following modules:

### Modules
- Anther **v0.0.9**: Strategy Development & Simulation system for inventing and back-testing trading strategies.
  - Holds custom trading bots (referred to as expert advisors)
  - Holds custom technical indicators (referred to as indicators)
- Planter **v0.0.9**: Back-end API system capable of tracking user trades and accounts to build a comprehensive look at multiple portfolios.
- Platform **v0.0.9**: System entities & functionality used by all other modules
- Processing **0.0.9**: Code generation module
- Radicle **v0.0.9**: Data-access & Service-layer with integrated data functionality
  - Core models and services, repositories
  - Importing functionality for bringing trades & market data into the system
  - Data parsing system meant to collect historical data for use with back-testing and data-visualization systems. (v0.0.1: FirstRateData, v0.0.2: Trading View)

Current Version: **0.1.0**\
Current Release: **Pre-release**

---

### Supported Platforms
- .csv Imports
- MetaTrader 4
- MetaTrader 5

### Supported Brokers
- CMC Markets
- FTMO

---

### Todo List

N/A

---

### Improvements
- (0.1.0) : Change testing code to Jupiter
- (0.1.1) : Add @Api documentation for Swagger in Controller endpoints. Make it look nice
- (0.1.1) : CronJob to auto fetch news every late Sunday evening
- (0.1.1) : Improve code using better Lombok, look into builders
- (0.1.7) : Improve ApexChart service to select data, parsers etc...
- (0.1.8) : Improve Trade import service to include withdrawals and deposits (for withdrawal example, look at live CFD account)
- (0.1.8) : Accounts that have not been traded on for 1 calendar year should be marked as inactive (use a cronjob for this)
---

### Version History
- **0.0.1** : Basic data modeling & Trade Import feature. Allows users with multiple trading accounts to import trades into their accounts from supported brokers.
- **0.0.2** : API Controller Authentication
- **0.0.3** : Testing Suite implementation & integration
- **0.0.4** : Forex Factory calendar integration
- **0.0.5** : Trade Record Integration
- **0.0.6** : Charting Implementation
- **0.0.7** : Integrating with **sepal**
- **0.0.8** : Import FTMO Results
- **0.0.9** : Major Code Improvements & Refactoring
- **0.1.0** : (In Progress) Major Testing Refactoring and Coverage Improvements
- **0.1.1** : (Upcoming) Improve Api Documentation, Improve Lombok usage, Cronjob for fetching Market News, Refactor controller endpoints to use pojo's instead of Maps for Request Objects (Look into OpenAPI)
- **0.1.2** : (Upcoming) Refactoring of Portfolios and Account Details (work with sepal) including page for managing portfolios, selector for active portfolio, CRUD
- **0.1.3** : (Upcoming) Dockerize App (including front-end)
- **0.1.4** : (Upcoming) Store Market data from MT4 and FirstRateData into database
- **0.1.5** : (Upcoming) Automate fetching market data
- **0.1.6** : (Upcoming) Automate fetching account trades from MT4 (possibly using an API)
- **0.1.7** : (Upcoming) Improve ApexChart service to select data, parsers etc...
- **0.1.8** : (Upcoming) Improve Trade import service to include withdrawals and deposits (for withdrawal example, look at live CFD account), Accounts that have not been traded on for 1 calendar year should be marked as inactive (use a cronjob for this)