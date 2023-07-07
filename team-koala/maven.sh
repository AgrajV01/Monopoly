#!/bin/bash

# Navigate to the project directory
cd ~/team-koala

# Compile the project using Maven
mvn clean install

# Check if the build was successful
if [ $? -eq 0 ]
then
  echo "Maven build successful."
  # Run the Game class
  java -cp ~/team-koala/target/team-koala-1.0.jar com.team-koala.Game
else
  echo "Maven build failed."
fi
