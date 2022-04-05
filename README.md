<p align="center" >
<img src="https://demo.knowage-suite.com/knowage/themes/commons/img/defaultTheme/logoCover.svg" width="30%"/> <img src="https://portal.ogc.org/files/?artifact_id=92076" width="30%"/> 
</p>

[![License: APGL](https://img.shields.io/github/license/KnowageLabs/Knowage-Server.svg)](https://opensource.org/licenses/AGPL-3.0)
[![Docker badge](https://img.shields.io/docker/pulls/demeterengteam/knowage-7.2.0.svg)](https://hub.docker.com/r/demeterengteam/knowage-7.2.0)

Knowage is the open source suite that implements a full set of features, such as data federation, mash-up, data/text mining 
and advanced data visualization, with a special focus on big data analytics and comprehensive support to rich 
and multi-source data analysis. 
The new Knowage BD product satisfies the whole spectrum of analytical needs in any Big Data architecture. 
Knowage suite is realized by Engineering Group - the leading Italian software and services company, 
with about 8,200 people and more than 40 offices in Italy, Belgium, Republic of Serbia, Norway, Brazil, Argentina, and United States.

| :books: [Documentation](https://knowage-suite.readthedocs.io/en/7.2/) | :page_facing_up: [Site](https://www.knowage-suite.com/site/home/) | :whale: [Docker Hub](https://hub.docker.com/r/demeterengteam/knowagedb-7.2.0) | :dart: [Roadmap](https://github.com/Engineering-Research-and-Development/knowage-for-dashboards/blob/main/ROADMAP.md) |
| --- | --- | --- | --- |

## Contents

* [**Architecture**](#architecture)
* [**Knowage Pilot Integration**](#knowage-pilot-integration)
* [**Dashboard Catalogue**](#dashboard-catalogue)
* [**Dashboard Status Development**](#dashboard-status-development)
* [**Technologies**](#technologies)
* [**Features**](#features)
* [**Production**](#production)
* [**Installation Requirements**](#installation-requirements)
* [**Setup local instance**](#setup-local-instance)
* [**How to use**](#how-to-use)
* [**Workaround**](#workaround)
* [**Pilot Open Issue**](#pilot-open-issue)
* [**Support team**](#support-team)
* [**Release**](#release)
* [**License**](#license)

## Architecture

![KA Architecture](/screenshots/kaarchitecture.png)

## Knowage Pilot Integration

![Pilot integration](/screenshots/kapilotintegration.jpg)

## Dashboard Catalogue

The dashboard catalogue will list all the dashboards currently released on Knowage. 

**[Dashboard Catalogue](https://github.com/Engineering-Research-and-Development/knowage-for-dashboards/blob/main/DashboardCatalogue.md)**

## Dashboard Status Development

| Area | Components   | Component Leader                            | Development |
| :--- | :----------: | :------------------------------------------ | :---------: |
| A    | A1           | Bart Beusen (VITO)                          | 100%        |
| A    | A2           | Izar Azpiroz (VICOM)                        | 100%        |
| A    | A3           | Mihai Angheloiu (SIVECO)                    | 100%        |
| A    | A5           | Ross Campbell (ICE)                         | 100%        |
| B    | B1, B3, B4   | Manuel Mora (UMU)                           | 100%        |
| C    | C1           | Mihai Angheloiu (SIVECO)                    | 100%        |
| C    | C2           | Stefan Louriero (Ubiwhere)                  | 100%        |
| D    | D1           | Andreas Schröder (John Deere)               | 100%        |
| D    | D2           | Nenad Gligoric (DNET)                       | 100%        |
| D    | D3           | Isabelle Piccard (VITO)                     | 100%        |
| E    | E1           | Ross Campbell (ICE), Sergio Salmeron (ATOS) | 100%        |
| E    | E2           | Diego Guidotti (AGRICOLUS)                  | 100%        |
| F    | F1           | Harald Volden (MIMIRO)                      | 100%        |
| F    | F2           | Nenad Gligoric (DNET)                       | 100%        |
| G    | G1.D1, G1.D2 | Antonio Caruso (ENG)                        | 100%        |
| G    | G2           | Nenad Gligoric (DNET)                       | 100%        |
| H    | H1.D1, H1.D2 | Antonio Caruso (ENG)                        | 100%        |
| H    | H2           | Nenad Gligoric (DNET)                       | 100%        |
| I    | I1           | Diego Guidotti (AGRICOLUS)                  | 100%        |
| I    | I2           | Diego Guidotti (AGRICOLUS)                  | 100%        |
| I    | I3           | Diego Guidotti (AGRICOLUS)                  | 100%        |

## Technologies

| Technology   |
| :---:        |
| Java         |
| Tomcat       |
| Python       |
| R            |
| JavaScript   |
| Angular      |
| OpenLayers   |
| MariaDB      |
| MySQL        |
| Oracle       |
| PostgreSQL   |
| MongoDB      |
| Teradata     |
| ChartJS      |
| Highcharts   |


## Features

|                                                                                                                    | Name                   | Description                                                                                                              |
| ------------------------------------------------------------------------------------------------------------------ | ---------------------- | ------------------------------------------------------------------------------------------------------------------------ |
| <img src="https://www.knowage-suite.com/site/wp-content/uploads/2021/03/BD_txt-180x180-1.jpg" alt="BD" width="50px"/> | Big Data               | To analyse data stored on big data clusters or NoSQL databases                                                           |
| <img src="https://www.knowage-suite.com/site/wp-content/uploads/2021/03/SI_txt-180x180-1.jpg" alt="SI" width="50px"/> | Smart Intelligence     | The usual business intelligence on structured data, but more oriented to self-service capabilities and agile prototyping |
| <img src="https://www.knowage-suite.com/site/wp-content/uploads/2021/03/ER_txt-180x180-1.jpg" alt="ER" width="50px"/> | Enterprise Reporting   | To produce and distribute static reports                                                                                 |
| <img src="https://www.knowage-suite.com/site/wp-content/uploads/2021/03/LI_txt-180x180-1.jpg" alt="LI" width="50px"/> | Location Intelligence  | To relate business data with spatial or geographical information                                                         |
| <img src="https://www.knowage-suite.com/site/wp-content/uploads/2021/03/PM_txt-180x180-1.jpg" alt="PM" width="50px"/> | Performance Management | To manage KPIs and organize scorecards, to monitor your business in real-time                                            |
| <img src="https://www.knowage-suite.com/site/wp-content/uploads/2021/03/PA_txt-180x180-1.jpg" alt="PA" width="50px"/> | Predictive Analysis    | To perform advanced analyses for forecasting and prescriptive purposes                                                   |

Knowage supports a modern vision of the data analytics, providing new
self-service capabilities that give autonomy to the end-user, able to build
his own analysis and explore his own data space, also combining data that come
from different sources.


## Production

The instance will be installed **on pilots premises**.

Refer to the following sections for installation and usage:

* [**Setup local instance**](#setup-local-instance)
* [**How to use**](#how-to-use)

## Installation Requirements

| Name           | Version  | Build      | Notes                                   |
| :------------: | :------: | :--------: | --------------------------------------- |
| Docker         | 19.03.11 | 42e35e61f3 | [Docker docs](https://docs.docker.com/) |
| Docker-Compose | 1.26.0   | d4451659   |                                         |


## Setup local instance

### Run the application

It's possible to run the application using `docker-compose`.

#### Docker-compose

```
version: "3"
services:

   knowagedb:
      hostname: knowagedb
      image: demeterengteam/knowagedb-7.2.0:v2.5
      networks:
         - hostnet
      ports:
       - "3306:3306"
      environment:
         - MYSQL_ROOT_PASSWORD=r00t
         - MYSQL_DATABASE=knowagedb
      volumes:
         - ./mariadb_conf:/home/knowage/mariadb_conf

   knowage:
      hostname: knowage
      image: demeterengteam/knowage-7.2.0:idm-v2.5
      networks:
         - hostnet
      ports:
         - "8080:8080"
      depends_on:
         - knowagedb
      environment:
         - DB_HOST=knowagedb
         - DB_PORT=3306
         - DB_USER=root
         - DB_PASS=r00t
         - DB_DB=knowagedb
	  env_file:
         - ./KnowageParameters.env

networks:
   hostnet:

```
If the host port 3306 is already taken, set another one for knowagedb, i.e. **"3307:3306"**.

Create a **KnowageParameters.env** file into the same folder of the above docker-compose:

*KnowageParameters.env content:*
```
ACS_INTEGRATION=true
SECURITY_LOGOUT_URL=https://acs.bse.h2020-demeter-cloud.eu:5443/auth/logout?_method=DELETE
OAUTH2_CLIENT_ID=INSERT APP CLIENT ID FROM IDM
OAUTH2_SECRET=INSERT APP CLIENT SECRET FROM IDM
ACS_BASE_URL=https://acs.bse.h2020-demeter-cloud.eu:5443
ACS_ADMIN_ID=ADMIN ID
ACS_ADMIN_EMAIL=ADMIN EMAIL
ACS_ADMIN_PASSWORD=ADMIN PASSWORD
REDIRECT_URL=http://localhost:8080/knowage/servlet/AdapterHTTP?PAGE=LoginPage\&NEW_SESSION=TRUE
PROXY_HOST=
PROXY_PORT=

```

Run the command `docker-compose up -d`.

Open a browser and paste the URL below for logging in Knowage:
```
http://localhost:8080/knowage/servlet/AdapterHTTP?PAGE=LoginPage&NEW_SESSION=TRUE
```

**An ACS account is needed to login.**

## How to use

Please check the **[User Manual](https://github.com/Engineering-Research-and-Development/knowage-for-dashboards/blob/main/UserManual.md)**

## Workaround

* NDVI Image, Knowage approach:

	* **Using Base64 encode**
	* **Using Image URL**

* AIM Model, information file size:
 
	* **Use Knowage Analytical Driver**

* Profile attribute extension.
			
## Pilot Open Issue

### AIM/Mockup to review

* **Area A:**
	* Component A.3 – **AIM missing value**

* **Area C:**
	* Component C.1 – **AIM missing value**

* **Area D:**
	* Component D.1 – **Missing AIM:**
		* Waiting for AIM output data.

* **Area E:**
	* Component E.1 – **AIM to verify:**
		* AIM format not correct (some input missing and some input wrong).
		
		
## Support team

**[Luigi di Corrado](mailto:luigi.dicorrado@eng.it)** _(Development)_

**[Massimo Giacalone](mailto:Massimo.Giacalone@eng.it)** _(Development)_

**[Gianluca Isgro'](mailto:gianluca.isgro@eng.it)** _(Delivery)_

**[Antonio Caruso](mailto:Antonio.Caruso@eng.it)** _(Integration)_

## Release
![Version badge](https://img.shields.io/badge/Candidate-0.8-green)

## License

[AGPL](LICENSE) © 2021 Engineering Ingegneria Informatica S.p.A.
