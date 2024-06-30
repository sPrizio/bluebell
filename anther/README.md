# Anther
The anther module is responsible for back-testing implemented strategies and providing reports for data-analysis.


Current Version: **0.0.1**\
Current Release: **Pre-release**

---

### Viable Strategies
- Bloom **v0.0.1**: A strategy that aims to capture the trend of the intra-day by entering at statistically-relevant time using a 2-entry straddle method

---

### To Do
- Research November from each year (2020-2023) to investigate why the intra-day movements were so difficult to capture
- Implement logic to capture bar averages from a previous look-back period to dynamically compute averages (test this to see if it can beat current estimates)
- Review the 9:50 candle on higher time frames. Possible long and shorts above bracket levels?
- The goal is to look for ways to reduce points lost. As it stands the strategy is pretty good but it could stand to be improved
- Another possible improvement is to double down once the first entry is stopped out. Possible to extract additional points?
