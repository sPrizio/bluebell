# Planter API
Planter is a trading app designed to help track a trader's progress as they navigate the wonderfully tumultuous environment that is the market.
This app will aim to track trades through each day, offer basic insights and helpful news as well as a place to manage trading accounts.

Current Version: **0.0.7**\
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
- Improve Trade import service to include withdrawals and deposits (for withdrawal example, look at live CFD account)
- Accounts that have not been traded on for 1 calendar year should be marked as inactive (use a cronjob for this)
- Improvements to UniqueIdentifierService to really make every possible entity unique
- CSV import service for FTMO
- Add testing
---

### Version History
- **0.0.1** : Basic data modeling & Trade Import feature. Allows users with multiple trading accounts to import trades into their accounts from supported brokers.
- **0.0.2** : API Controller Authentication
- **0.0.3** : Testing Suite implementation & integration
- **0.0.4** : Forex Factory calendar integration
- **0.0.5** : Trade Record Integration
- **0.0.6** : Charting Implementation
- **0.0.7** : Integrating with **sepal**
- **0.0.8** (In Progress) : Import FTMO Results

