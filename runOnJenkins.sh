docker stop gateway-c
docker rm gateway-c
docker image rm gateway-c:0.0.1
docker build . -t gateway-c:0.0.1
docker run -it -d -p 5555:5555 --name gateway-c --network crm-network --network-alias alias-gateway-c --link redis gateway-c:0.0.1
