<div align="center">

<img src="https://github.com/user-attachments/assets/6645db98-27f6-448e-9768-b0c0ec7c81a9" width="35%" height="35%" />

</div>

[![Build status (GitHub)](https://img.shields.io/github/actions/workflow/status/sPrizio/bluebell/ci.yml?branch=main&label=Build&logo=github&cacheSeconds=600)](https://github.com/sPrizio/bluebell/actions?query=workflow%3AASF-ci+branch%3Amain)
[![Github last commit date](https://img.shields.io/github/last-commit/sPrizio/bluebell.svg?label=Updated&logo=github&cacheSeconds=600)](https://github.com/sPrizio/bluebell/commits)
[![License](https://img.shields.io/github/license/sPrizio/bluebell.svg?label=License&logo=apache&cacheSeconds=2592000)](https://github.com/sPrizio/bluebell/blob/main/LICENSE.txt)
[![Wiki](https://img.shields.io/badge/Read-wiki-cc5490.svg?logo=github)](https://github.com/sPrizio/bluebell/wiki)

[![GitHub stable release version](https://img.shields.io/github/v/release/sPrizio/bluebell.svg?label=Stable&logo=github&cacheSeconds=600)](https://github.com/sPrizio/bluebell/releases/latest)
[![GitHub stable release date](https://img.shields.io/github/release-date/sPrizio/bluebell.svg?label=Released&logo=github&cacheSeconds=600)](https://github.com/sPrizio/bluebell/releases/latest)

[![GitHub sponsor](https://img.shields.io/badge/GitHub-sponsor-ea4aaa.svg?logo=github-sponsors)](https://github.com/sponsors/sPrizio)
[![PayPal.me donate](https://img.shields.io/badge/PayPal.me-donate-00457c.svg?logo=paypal)](https://paypal.me/bluebellStephen)

---

***bluebell Capital*** specializes in software systems that trade equities algorithmically and provides specialized tracking for trading accounts and their performance. Additionally, bluebell provides evaluating trading strategies capabilties in order to evolve and adapt strategies to maintain their effectiveness. bluebell comprises both a front-end project (flower) and back-end project (greenhouse), each comprising numerous modules, each with different functionalities. bluebell is built on a MariaDB ORM and is integrated with NextJs, Java Spring Boot & Docker.

Dependencies & Integrations
- **NextJs**: Core front-end functionality built on React.js
- **Java Spring** : Core back-end functionality
- **Forex Factory**: External Market News provider for trading events 
- **Docker**: Containerization Library

---

### flower | Front-End
***flower*** is the front-end, consumer-facing subset of bluebell. Currently, ***flower*** is composed of the following modules:

- Current Iteration: **0.2.6**
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

- Current Iteration: **0.2.6**
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

### Configuration Notes

#### Windows
This project can be run using Docker. For convenience, startup scripts have been included. These are located in `/scripts`. If running on windows, you can use `./run-bluebell.bat`
which takes 1 required argument for the environment: `dev, staging or prod` and an optional argument `true` or `false` for rebuilding the Docker images. An example execution
would be `./run-bluebell.bat dev`. This will run the project with an existing Docker build. Add the true flag to build a new image.


#### Linux/Mac
The exact same instructions as above, however the script is an .sh script. Here's an example: `./run-bluebell.sh dev`

Either of the above options  will initiate the project in Docker using the `.env.dev` file and variables. Please ensure that the `.env.dev` file is created in your project directory. You can use `.env.example`
as a guide. Also to note, staging and production configuration can likewise be implemented in their respective files: `.env.staging` & `.env.prod`.

---

bluebell &copy; created by Stephen Prizio
