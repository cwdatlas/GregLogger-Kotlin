# horizons-kotlin

This project uses Quarkus, very cool web framework for java and kotlin.
The horizons-kotlin project was built to store and then persist log data to a gui client.
It uses a postgres database running in a container, a Rest api to post and get logs, and 
a websocket connection to actively persist logs to a website.
### Using the application
- Web interface at http://localhost:8080. From there you can click the button to connect then it will display any logs added
to the database.
- To add a log: post json to http://localhost:8080/logs in this format:
![img.png](img.png)
 I use postman for websocket and Rest calls
- The date must be in (YYYY-MM-DD) format, if you dont have it in the correct format, a date object will be returned 
signaling to you to change the date. The object is not very clear, so try to stick with the known format.
- Machine, machineID, Message and OperationCode can be any text. 
- OperationCode does have recommended strings, which will be returned if data is invalid.
Codes:
![img_1.png](img_1.png)

### Application dependencies
- Container engine on your computer: it must be able to be accessible through a docker api.
I use podman which has a compatible api for quarkus.
- Java 17
- git 
- postgres container (optional)

### Cloning the project
- When your in a directory where you want a project folder, clone the repo by typing:
```shell script
git clone https://github.com/cwdatlas/GregLogger-Kotlin
```
- Then navigate into the repo:
```shell script
cd GregLogger-Kotlin
```
- Now you can proceed to run the application how you like. This application will take
some time to build for the first time, especially if you have not pulled a postgres image in your container engine.
Quarkus will pull a postgres container if one does not exist.
### Running the application in dev mode

You can run the application in dev mode that enables live coding using:
```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.
<= this is super cool

### Thanks
Thank you for checking out this project. I was new to many of its parts so it didn't end up exactly how i wanted.
I did learn lots doing it. By the way, the nice format of this file came with Quarkus, I just added onto it!


