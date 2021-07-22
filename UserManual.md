<p align="center">
<img src="https://demo.knowage-suite.com/knowage/themes/commons/img/defaultTheme/logoCover.svg" width="30%"/> <img src="https://portal.ogc.org/files/?artifact_id=92076" width="30%"/> 
</p>

# Knowage for dashboards - User Manual

Welcome to the user manual section, here we describe a step by step guide to setup and configure 
the Knowage for dashboard instance.

## Contents

* [**ACS application registration**](#acs-application-registration)
* [**Comunication check between VM and ACS**](#comunication-check-between-vm-and-acs)
* [**Setup local instance**](#setup-local-instance)
* [**Dataset configuration**](#dataset-configuration)
* [**Dashboard execution**](#dashboard-execution)
* [**Dashboards view by role**](#dashboards-view-by-role)

## ACS application registration

1. Sign-up on ACS (follow this guide: **[Demeter user signup and activate](https://gitlab.com/demeterproject/wp3/se/components/acs/-/blob/master/videos/Demeter-user-signup_and_activate.mp4)**)

2. Sign-in to access the Home page

3. Into the Home page, simply click the **Register** button or click **applications** on main menu and then the **Register** button.

![ACS Register App 1](/screenshots/UserManualImages/acsstep1.PNG)

4. A three-step wizard will appear. On the first step we need to set the following fields:
	* **Name:** Name of the application
	* **Description:** Some description
	* **Url:** Application url, for a local installation of Knowage set it as:
		* ```http://localhost:8080/knowage/servlet/AdapterHTTP?PAGE=LoginPage&NEW_SESSION=TRUE```
	* **Callback Url:** The user agent will be redirected to this URL when OAuth flow is finished.
		* ```http://localhost:8080/knowage/servlet/AdapterHTTP?PAGE=LoginPage&NEW_SESSION=TRUE```
	* **Sign-out Callback Url:** 
		* ```http://localhost:8080/knowage/servlet/AdapterHTTP?PAGE=LoginPage&NEW_SESSION=TRUE```

![ACS Register App 2](/screenshots/UserManualImages/acsstep2.PNG)

5. Once done, click the **Next** button to move on second step.

6. Here it's possible to choose an icon image. Once done click the **Next** button.

![ACS Register App 3](/screenshots/UserManualImages/acsstep3.PNG)

7. In the third step we can set up roles and permissions

8. Click the **"+"** icon on the roles section and add an **admin** role.

![ACS Register App 4](/screenshots/UserManualImages/acsstep4.PNG)

9. Click **Save** to complete.

10. Now click on the application to open the details, from there it's possible to **edit** or **manage roles**.

![ACS Register App 5](/screenshots/UserManualImages/acsstep5.PNG)

![ACS Register App 6](/screenshots/UserManualImages/acsstep6.PNG)

11. Click on **OAuth2 Credentials** and take note of the **Cliend ID** and **Client Secret** tokens

12. Scroll down the page and click the **Authorize** button on **Authorized users** section.

![ACS Register App 7](/screenshots/UserManualImages/acsstep7.PNG)

13. Here it's possible to select the ACS users to grant them access and roles to use our application.

![ACS Register App 8](/screenshots/UserManualImages/acsstep8.PNG)

## Comunication check between VM and ACS 

Before proceeding to the next steps, it is recommended to verify the correct communication between 
the docker Virtual Machine (VM) and ACS by running the following command:

```
curl --location --request POST 'https://acs.bse.h2020-demeter-cloud.eu:5443/v1/auth/tokens' --header 'Content-Type: application/json' --data '{
  "name": "ACS_MAIL",
  "password": "ACS_PASSWORD"
}'
```

Put your ACS account mail on **ACS_MAIL** and your ACS password on **ACS_PASSWORD**.

If this test is not successful, then you must check with the system administrator that 
**port 5443** is open and can connect to the ACS host.

## Setup local instance

1. Visit the docker hub page and check for the latest tag version of the instance:
	* **[Knowage](https://hub.docker.com/r/demeterengteam/knowage-7.2.0/tags?page=1&ordering=last_updated)**
	* **[Knowagedb](https://hub.docker.com/r/demeterengteam/knowagedb-7.2.0/tags?page=1&ordering=last_updated)**

2. Create a **new folder** and name it as your preference.

3. Now create a new file inside that folder and rename it as **docker-compose.yml**

4. Copy and paste the content below into the docker-compose file. 
	* Note: remember to check and change the **image** tag version with the latest one: 
		* **image: demeterengteam/knowagedb-7.2.0:<mark>v2.0</mark>**
		* **image: demeterengteam/knowage-7.2.0:<mark>idm-v2.0</mark>**

*docker-compose.yml:*
```
version: "3"
services:

   knowagedb:
      hostname: knowagedb
      image: demeterengteam/knowagedb-7.2.0:v2.0
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
      image: demeterengteam/knowage-7.2.0:idm-v2.0
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

5. Now create a new file into the same folder and rename it as **KnowageParameters.env** (or as your preference).
	* Note: If a different name is given, then remember to update also the **env_file** reference into the **docker-compose.yml**.

6. Copy and paste the content below into the KnowageParameters file.
	* Note: The acronym **"IDM"** (Identity Manager) is used to indicate the **Access Control System (ACS)**.

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

7. On **OAUTH2_CLIENT_ID** and **OAUTH2_SECRET**, paste the tokens from your Knowage application registered on ACS.
	* Note: Refer to **point 11** of [**ACS application registration**](#acs-application-registration)

8. Set your **ACS Admin account ID, e-mail** and **password** on **ACS_ADMIN_ID, ACS_ADMIN_EMAIL** and **ACS_ADMIN_PASSWORD** fields.

9. Set the **REDIRECT_URL** with the **Callback Url** provided on your Knowage application registered on ACS.

10. If you need to configure a proxy, set **PROXY_HOST** and **PROXY_PORT** else, leave those values empty.

11. Once everything is set, open a **command prompt** on the same folder of the **docker-compose** file.

12. If it's the first installation of the instance, or you want to update the version,
then execute the command `docker-compose up -d`, else, execute the command `docker-compose start`.

13. Wait until the service is fully started (it should take 1 minute or less).

14. Open a browser and paste the URL for logging in Knowage:

```
http://localhost:8080/knowage/servlet/AdapterHTTP?PAGE=LoginPage&NEW_SESSION=TRUE
```

15. Use the ACS account registered to access.

16. If it's the first time, an **Authorization** message will pop out to **Allow** the access.

17. Execute the command `docker-compose stop` to stop the instance. That's wont delete your data from the container.
	* Note: Using again `docker-compose up -d` to start the instance , will reset all the data and configuration made inside Knowage.

## DSS Table

The following table shows, for each **Dashboard (DSS)**, the name, which pilots use it and the datasets name that feed it.

| Component | DSS        | DSS Name                                        | Pilot                   | Datasets Name                                                                    |
|-----------|------------|-------------------------------------------------|-------------------------|---------------------------------------------------------------------------------|
| A1        | DSS1.A1    | A1 Plant Yield Estimation                       | 1.1, 1.2, 1.3, 1.4, 3.4 | 4A1.DSS1.FieldBordersLayer, 4A1.DSS1.PredictedFieldValueLayer                   |
| A2        | DSS2.A2    | A2 Plant Phenology Estimation                   | 3.1                     | 4A2_PhenologyEstimation                                                         |
| A3        | DSS3.A3    | A3 Plant Stress Detection                       | 1.4                     | 4A3_PlantStressData                                                             |
| A5        | DSS5.A5    | A5 Estimate Beehive                             | 5.3                     | 4A5_EstimateBeehive                                                             |
| B1        | DSS1.B1    | B1 Irrigation Management                        | 1.1, 1.2, 1.3, 3.1, 3.2 | 4B1_IrrigationManagement                                                        |
| C1        | DSS1.C1    | C1 Nitrogen Balance Model                       | 1.3, 1.4, 2.3, 3.1, 3.2 | 4C1_NitrogenBalanceData                                                         |
| C2        | DSS1.C2.D1 | C2 Nutrient and Sowing recommendation           | 1.3, 1.4, 2.3, 3.1, 3.2 | 4C2_NutrientMonitorZones, 4C2_NutrientMonitorWeather, 4C2_NutrientMonitorParcel |
| D1        | DSS1.D1    | Emission                                        | 2.1                     | N/A                                                                             |
| D2        | DSS2.D2    | D2 Field Operation                              | 2.3, 5.1, 5.2           | 4D2_FieldOperation                                                              |
| D3        | DSS2.D3    | D3 Variable Rate                                | 3.4, 5.1, 5.2           | 4D3_VariableRate                                                                |
| E1        | DSS1.E1    | E1 Pest Estimation with Sterile Fruit Flies     | 1.1, 1.2, 3.3, 5.1, 5.3 | 4E1_PestEstimation                                                              |
| E2        | DSS1.E2    | E2 Estimate Temperature-Related Pest Events     | 3.1, 3.3                | 4E2_TempPestEvents                                                              |
| F1        | DSS1.F1    | F1 Estimate Milk Production                     | 4.1, 4.2, 4.4, 5.4      | 4F1_EstimateMilkProduction                                                      |
| F2        | DSS2.F2    | F2 Poultry Feeding                              | 4.4, 5.4                | 4F2_PoultryFeeding                                                              |
| G1        | DSS1.G1.D1 | G1 Estimate Animal Welfare Condition Training   | 4.2, 4.3, 4.4, 5.4      | 4G1.DSS1.AnimalWelfareTraining, 4G1.DSS1.AnimalWelfareMetrics                   |
| G1        | DSS1.G1.D2 | G1 Estimate Animal Welfare Condition Prediction | 4.2, 4.3, 4.4, 5.4      | 4G1.DSS2.AnimalWelfarePrediction                                                |
| G2        | DSS1.G2    | G2 Poultry Well Being                           | 4.3, 5.4                | 4G2_PoultryWellBeing                                                            |
| H1        | DSS1.H1.D1 | H1 Milk Quality Training                        | 4.2                     | 4H1.DSS1.MilkQualityMetrics, 4H1.DSS1.MilkQualityTraining                       |
| H1        | DSS1.H1.D2 | H1 Milk Quality Prediction                      | 4.2                     | 4H1.DSS2.MilkQualityPrediction                                                  |
| H2        | DSS1.H2    | H2 Transport Condition                          | 5.1, 5.4                | 4H2_TransportCondition                                                          |
| I1        | DSS1.I1    | I1 Generic Farm Comparison                      | ALL                     | 4I1_GenericFarmComparison                                                       |
| I2        | DSS1.I2    | I2 Neighbour Benchmarking                       | ALL                     | 4I2_NeighbourBenchmarking                                                       |
| I3        | DSS1.I3    | Technology Benchmarking                         | ALL                     | 4I3_TechnologyBenchmarking                                                      |

## Dataset configuration

Note: The dataset configuration is optional and can be skipped if your component is registered within the BSE.

1. After login into Knowage click on **Menu** at top-left.

![Knowage 1](/screenshots/UserManualImages/ka1.PNG)

2. Then click on **Data set**.

![Knowage 2](/screenshots/UserManualImages/ka2.PNG)

3. A list of datasets will be loaded, input your component code into the **search** field to filter the list (i.e. "G1"). This funtionality will be very useful during the endpoint customization process.

![Knowage 3](/screenshots/UserManualImages/ka3.PNG)

4. Click on the dataset you wish to configure and click on **Type**, here you can change the default value for the endpoint **URL parameter**

5. Click on **Preview** to check the data, then **Save**.

![Knowage 4](/screenshots/UserManualImages/ka4.PNG)

6. Now click the **folder icon** on the top-left menu to open the dashboards explorer.

![Knowage 5](/screenshots/UserManualImages/ka5.PNG)

7. Select your area and open the dashboard by clicking on the **Play** button at right side.

![Knowage 6](/screenshots/UserManualImages/ka6.PNG)

8. Now click the **Edit** button on top-right side menu.

![Knowage 7](/screenshots/UserManualImages/ka7.PNG)

9. Click the red circle to open the **Cockpit menu** and click on **Data Configuration**

![Knowage 8](/screenshots/UserManualImages/ka8.PNG)

10. Here you will notice the same **endpoint URL parameter** that need to be changed with the new one, then **Save**.

![Knowage 9](/screenshots/UserManualImages/ka9.PNG)

11. Click again the **Cockpit menu** and click on **Clear Cache** first, then **Save** the cockpit.

![Knowage 10](/screenshots/UserManualImages/ka10.PNG)

12. Click on the eye to go back in **View mode** or **close** and **re-open** the dashboard.

![Knowage 11](/screenshots/UserManualImages/ka11.PNG)

## Dashboard execution

1. Once logged in, click the **folder icon** on the left side menu.

![Knowage 5](/screenshots/UserManualImages/ka5.PNG)

2. This will open the cockpit window, with a list (**Functionalities**) containing each **area component**.

3. Click the desired area to show up the relative list of **dashboards** developed.

4. To open a dashboard simply click on the **"play"** button at the right side of the dashboard description.

![Knowage 6](/screenshots/UserManualImages/ka6.PNG)

## Dashboards view by role

1. Management of **roles** by the **administrator**.

![Knowage 12](/screenshots/UserManualImages/ka12.PNG)

2. List of **user roles** by **pilot**, visible and manageable by the **administrator**.

![Knowage 13](/screenshots/UserManualImages/ka13.PNG)

3. **Pilot x.y user** login.

![Knowage 14](/screenshots/UserManualImages/ka14.PNG)

4. View dashboards list for **Pilot x.y user**.

![Knowage 15](/screenshots/UserManualImages/ka15.PNG)
