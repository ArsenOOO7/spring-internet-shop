image_name=internet-shop

docker build -t $image_name -f Dockerfile-multistage .
#docker-compose up

docker run -d -p 8080:8080 $image_name