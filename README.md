# bluebell 
bluebell Wealth Management is a software system designed to track trading accounts and their performance, as well as evaluate trading strategies in order to evolve and adapt strategies
to maintain their effectiveness. bluebell comprises both a front-end project (flower) and back-end project (greenhouse), each comprising numerous modules, each with different functionalities. bluebell is
built on a MariaDB ORM and is integrated with React.js and Java Spring Boot.

Dependencies & Integrations
- **React.js**: Core front-end functionality
- **Java Spring** : Core back-end functionality
- **Forex Factory**: External Market News provider for trading events 

---

### flower | Front-End
***flower*** is the front-end, consumer-facing subset of bluebell. Currently, ***flower*** is composed of the following modules:

- Current Iteration: **0.0.2**
- Current Release: **Pre-Release**

#### sepal
***sepal*** is the administrative dashboard application that manages user accounts, provides trade importing and strategy analysis. Currently, 
sepal is not designed to be customer facing but improvements are planned for the future to be a public-facing
application.

#### petal
***petal*** is the public website accessible for bluebell and serves as the interaction point with potential clients.
It advertises bluebell and opens the product up to external consumption and purchasing.

---

### greenhouse | Back-End
***greenhouse*** represents the Java back-end system that exposes web apis consumed by ***sepal*** & ***petal***, as well as 
the automation systems for importing, obtaining and manipulating external data. Additionally, strategy visualizers and
data parsing systems are contained within ***greenhouse***. ***greenhouse*** is currently composed of the following modules:

- Current Iteration: **0.1.1**
- Current Release: **Pre-Release**

#### anther ####

***anther*** provides market data analysis and strategy implementations. This is an exploratory R & D module
responsible for exploring and backtesting trading strategies. It leverages technology from the rest of the project but 
lives largely independently of the application.
- Strategy Development & Simulation system for inventing and back-testing trading strategies.
- Holds custom trading bots (referred to as expert advisors)
- Holds custom technical indicators (referred to as indicators)

***planter*** exposes the api to the public for consumption. Endpoints for importing trades, managing users and their accounts
and any other account management lives within this module. Additionally, statistical analysis of strategy and account performances
can be obtained through ***planter***. Anything that needs to be consumed or visualized comes from this module.

- Back-end API system capable of tracking user trades and accounts to build a comprehensive look at multiple portfolios.
- Strategy Visualization and basic Charting abilities

***platform*** contains the core data models and entities of the application. This module will not stand on its own and serves
primarily as the core data library used by other modules.

- System entities & functionality used by all other modules

***radicle*** is the core spring module. The data models from ***platform*** are used in service-layers defined
as spring beans here. All business logic, integrations & automations live inside ***radicle***.

- Data-access & Service-layer with integrated data functionality
- Core models and services, repositories
- Importing functionality for bringing trades & market data into the system
- Data parsing system meant to collect historical data for use with back-testing and data-visualization systems. (v0.0.1: FirstRateData, v0.0.2: Trading View)

---

bluebell &copy; created by Stephen Prizio