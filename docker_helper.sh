#!/bin/bash

buildDocker() {
	echo "Building docker for project $1..."
	cd $1
	./gradlew buildDocker 1>/dev/null 2>/dev/null
	if [ $? -ne 0 ]; then
		echo "Building docker for project $1 was not succesfull!"
	fi
	cd ..
}

buildDockers() {
	buildDocker ApplicationService
	buildDocker CpService
	buildDocker ParcelService
	buildDocker UserService
	buildDocker WebService
}

cleanDocker() {
	docker image rm cz.novros.cp/$1:1.0.0
}

cleanDockers() {
	cleanDocker cp-app_service
	cleanDocker cp-web_service
	cleanDocker cp-user_service
	cleanDocker cp-parcel_service
	cleanDocker cp-cp_service
}

runDocker() {
	docker run --rm -d --name $1 -p $2:8080 cz.novros.cp/$1:1.0.0
}

runDockers() {
	runDocker cp-app_service 8084
	runDocker cp-web_service 8080
	runDocker cp-user_service 8081
	runDocker cp-parcel_service 8082
	runDocker cp-cp_service 8083
}

stopDocker() {
	docker stop $1
}

stopDockers() {
	stopDocker cp-app_service
	stopDocker cp-web_service
	stopDocker cp-user_service
	stopDocker cp-parcel_service
	stopDocker cp-cp_service
}

case $1 in
	"build")
		buildDockers
	;;
	"clean")
		cleanDockers
	;;
	"run")
		runDockers
	;;
	"stop")
		stopDockers
	;;
esac
