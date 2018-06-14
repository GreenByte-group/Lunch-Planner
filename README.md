
# Foodastic
Organise your lunch break to socialize with friends and colleagues.

## What's it about
Our small group of computer science students are developing the Lunch Planner
during the SEP (Software engineer project), at the university of applied sciences Mannheim.
We decided to advance our project from beeing a software, only in use by companies to beeing a
open source software, usable for all. We had the luck to work together with a high motivated 
company, that spent very much time supporting us and
encouraged us to take our creativity to the next level.

Have fun and find some new friends!

The Core-Features this Web-Application is based on, are:
- create events
- invite friends or colleagues to events
- get a push notification 
- write comments in events
- add tasks to events
- subscribe to locations

This project is divided in two parts
#### Backend
The backend is a stateless REST-API written in Java with the framework [Spring Boot](https://spring.io/projects/spring-boot).
   
#### Frontend
The frontend is a responsive Web-APP written in JS with the framework [React](https://reactjs.org).

# Configuration
When you clone the repo it will not work out of the box. You need to set some parameters:

#### Server-Url for the frontend and backend servers
Open the file `frontend/lunchplanner/src/Config.js` and change `HOST` to the url of your 
backend-server and `FRONTEND_HOST` to the url of your frontend-server.

#### Notifications
Push Notifications are send with [Firebase-Cloud-Messaging](https://firebase.google.com/docs/cloud-messaging/).
To get this work you have to create you an developer account and follow [this](https://firebase.google.com/docs/web/setup)
instruction to update the fields `configFirebase` and `publicKey` in `src/Config.js`.

#### Content Security Policy
The frontend is with csp-meta-tags secured to prevent Cross-Side-Scripting. Update this meta-tag
in the `public/index.html` to your needs.

# Installation
You need these software to run the Foodastic APP.
- [npm](https://www.npmjs.com/) and [yarn](https://yarnpkg.com/lang/en/)
- [java](http://www.oracle.com/technetwork/java/javase/downloads/index.html) sdk >= 1.8

Clone the repo to your locale machine.
```
git clone https://github.com/GreenByte-group/Lunch-Planner
```
and change to the created folder
```
cd Lunch-Planner
```

If you are lazy just run `./install.js`

Or you build and start the servers yourself.
## Backend
The default port is 8080. Your can change this in `src/main/resources/application.properties` by 
changing the value of the field `server.port`
   
You can build the backend with gradle. Just run
```
./gradlew build
```
A executable jar is no in `build/libs` and can be executed with 
```
java -jar build/libs/jarName.jar
```

## Frontend
The frontend is in the folder `frontend/lunchplanner`. Change your working directory to it

Please check the [configuration section](#Configuration) to set your backend-server and to make the notifications work correctly.

```
cd frontend/lunchplanner
```
Now we build a version that we can be served over a web-server.
```
yarn install
yarn build
```
This creates an optimized production build in the folder `build`.
Copy the content of this folder into your web-root or start a new server by typing
```
npm install -g serve
serve -s build -l <PORT>
```

# Running the tests
Explain how to run the automated tests for this system

# Break down into end to end tests
Explain what these tests test and why

# Give an example
And coding style tests
Explain what these tests test and why

# Give an example
Deployment
Add additional notes about how to deploy this on a live system

# Built With
Dropwizard - The web framework used
Maven - Dependency Management
ROME - Used to generate RSS Feeds
Contributing
Please read CONTRIBUTING.md for details on our code of conduct, and the process for submitting pull requests to us.

# Versioning
We use SemVer for versioning. For the versions available, see the tags on this repository.

# Authors
Can Arsoy - GreenByte

Martin Schalter - GreenByte

# License
This project is licensed under the HSMannheim License - see the LICENSE.md file for details

Acknowledgments
We want to thank VSFExperst and our professors
for helping us ans sharing theire knowledge to improve ourselves
