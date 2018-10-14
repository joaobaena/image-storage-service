# Image store service

## How to start server locally

`sbt run`

## How to build docker image and run locally:

1. Do code changes

2. Build docker image:

   `sbt docker:publishLocal`

3. Run docker-compose

    `docker-compose up`


### Upload endpoint

http://localhost:8080/image-storage-service/api/uploadPhoto/[collection]

### Sample request: 


> POST /image-storage-service/api/uploadPhoto/shoes HTTP/1.1
>
> Host: localhost:8080
>
> User-Agent: insomnia/6.0.2
>
> Content-Type: multipart/form-data; boundary=X-INSOMNIA-BOUNDARY
>
> Accept: */*
>
> Content-Length: 2452361