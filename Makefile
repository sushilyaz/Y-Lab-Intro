run-dist: gradle-install-dist
	./build/install/Y-Lab-Intro/bin/Y-Lab-Intro

gradle-install-dist:
	docker-compose up -d
	gradle installDist
