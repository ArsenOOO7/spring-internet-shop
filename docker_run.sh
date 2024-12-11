image_name=internet-shop

docker build -t $image_name -f Dockerfile-multistage .
#docker-compose up