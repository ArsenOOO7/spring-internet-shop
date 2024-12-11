image_name=internet-shop

docker build -t $image_name -f Dockerfile-multistage .
#docker-compose up

docker run -d -p 7000:7000 $image_name