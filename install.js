#!/bin/bash
echo "This script will install the backend-server and frontend-server";
echo "Make sure npm, yarn and the java sdk >= 1.8 is installed"
echo "Do you want to build the backend-server? (Y / N)"
read install_backend

if [ $install_backend == "Y" ];
then
   echo "Change the port in 'src/main/resources/application.properties' and then press enter"
   read	temp
   ./gradlew build
   echo "-------------------------------------------------"
   echo "--- The executable jar is now in 'build/libs' ---"
   echo "--- Run 'java -jar build/libs/<JAR-Name>.jar' ---"
   echo "-------------------------------------------------"
fi

echo "Do you want to build the frontend-server? (Y / N)"
read install_frontend

if [ $install_frontend == "Y" ];
then
   echo "Make sure you configured your frontend. Please read the README.md for instructions."
   echo "Press enter when you are ready."
   read temp
   cd frontend/lunchplanner
   echo "installing dependencies"
   yarn install
   yarn build
   echo "Do you have your own webserver for serving the compiled files? (Y / N)"
   read own_server
   if [ own_server == "Y" ];
   then
      echo "-------------------------------------------------"
      echo "--- The optimized build is now in 'build'     ---"
      echo "--- Copy the content to your webroot          ---"
      echo "-------------------------------------------------"
   else
      npm -g install serve
      echo "-------------------------------------------------"
      echo "--- The optimized build is now in 'build'     ---"
      echo "--- Run 'serve -s build -l <PORT>'            ---"
      echo "-------------------------------------------------"
   fi
fi

echo "Good bye"
