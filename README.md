# Docker Build

mvn clean compile jib:dockerBuild

Environment Vars

SPRING_PROFILES_ACTIVE = dev (or prod)


Run Example:

Local  
docker run -p 7000:8080 --env SPRING_PROFILES_ACTIVE=dev realworld-backend


Production  
docker run -p 7000:8080 --env "SPRING_PROFILES_ACTIVE=prod" realworld-backend:latest


# Run in Intellij
Add to VM options -Dspring.profiles.active=dev

