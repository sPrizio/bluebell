# Sepal
Planter is a trading app designed to help track a trader's progress as they navigate the wonderfully tumultuous environment that is the market.
This app will aim to track trades through each day, offer basic insights and helpful news as well as a place to manage trading accounts.

Current Version: **0.0.2**\
Current Release: **Pre-release**

---

### Static-only Pages
- Forgot Password
- Register Page (External vendors)
- Login Page
- Performance (Filters)

### Integration TODOs
- Need a fetch user context to imitate logging in
- look at how state is updated

---

### Todo List
- Implement importing of trades v0.0.3
- Deploy Site v0.0.4
- Implement security with login and registration pages v0.0.5
- Complete total integration v0.0.6
---

### Version History
- **0.0.1** : Core functionality and initial page designs (static only)
- **0.0.2** : Integrating with Backend

### Bugs
- TODO's on the various functions
- Add documentation to the various functions files
- Inline edit/delete for phone numbers 
- Image screwing up the alignment in the accounts table
- Cannot create an inactive account
- Fix stuff in TradeTable.tsx
- Correctly fetch trade record controls once we have imported trades to test with

## Improvements
- Collapsible table rows for each date in the Market News table
- Passed news day should start auto-collapsed
- Custom create new account page (like the 404) for handling /default
- Creating an account or updating the account can include account close times
- Trades Page -> Filters for dates and trades (and sorting by date)
