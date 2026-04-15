#!/bin/sh

# Capture CLI arguments
cmd=$1
db_username=$2
db_password=$3

# Start docker
# Make sure you understand the double pipe operator
sudo systemctl status docker > /dev/null || sudo systemctl start docker > /dev/null

# Check container status (try the following cmds on terminal)
docker container inspect jrvs-psql  >/dev/null 2>&1

container_status=$?

# User switch case to handle create|stop|start opetions
case "$cmd" in 
  create)
  
  # Check if the container is already created
  if [ $container_status -eq 0 ]; then
		echo 'Container already exists'
		exit 1	
	fi

  # Check # of CLI arguments
  if [ "$container_status" -ne 0 ]; then
    echo 'Create requires username and password'
    exit 1
  fi
  
  # Create container
	docker volume create pgdata
  # Start the container
	    docker run --name $db_username -e DB_PASSWORD=$db_password -d -v pgdata:/var/lib/postgresql/data -p 5432:5432 postgres:18-alpine

  # Make sure you understand what's `$?`
	exit $?
	;;

stop)
  # Check instance status; exit 1 if container has not been created
  if [ $container_status -ne 0 ]; then
 echo 'container does not exist'
 exit 1
  fi

  # Start or stop the container
	docker container stop jrvs-psql
	exit $?
	;;
start)
  if [ $container_status -ne 0 ]; then

 echo 'container does not exist'
 exit 1
  fi
  # Start or stop the container
        docker container start jrvs-psql
        exit $?
        ;;


  
  *)
	echo 'Illegal command'
	echo 'Commands: start|stop|create'
	exit 1
	;;
esac 









