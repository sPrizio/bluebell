# bluebell - greenhouse
This module represents bluebell's backend. TODO - add more info

Current Version: **0.0.9**
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
- (0.1.0) : Add testing
---

### Improvements
- (0.0.9) : Add @Api documentation for Swagger in Controller endpoints. Make it look nice
- CronJob to auto fetch news every late Sunday evening
- (0.0.9) : Improve code using better Lombok, look into builders
- (0.1.0) : Change testing code to Jupiter
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
- **0.0.9** : (In Progress) Major Code Improvements & Refactoring
- **0.1.0** : (Upcoming) Major Testing Refactoring and Coverage Improvements