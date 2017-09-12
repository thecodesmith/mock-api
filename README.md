# Mock REST Server

## API

### Endpoints

    GET     /api/routes
    POST    /api/routes
    GET     /api/routes/:id
    PUT     /api/routes/:id
    DELETE  /api/routes/:id

### Route Data

    route:
      name: foo
      method: get
      status: 200
      path: /foo/bar
      active: true
      content-type: application/json
      body-content: '{"foo":"bar"}'
      headers:
        - X-Source-Server: mock
        - X-Meta-Tags: foo bar

### Examples

Here is the minimum required to created a route (`name` and `path` must be
specified):

    curl localhost:5050/api/routes \
            -X POST \
            -d '{"name": "foo", "path": "foo"}' \
            -H 'Content-Type: application/json'
    {"id":1}

Get definition of an existing route:

    curl localhost:5050/api/routes/1
    {
        "id":1,
        "name":"foo",
        "method":"GET",
        "status":200,
        "path":"api/foo",
        "active":1,
        "content_type":"application/json",
        "body_content":""
    }

Get list of all routes:

    curl localhost:5050/api/routes

Update an existing route:

    curl localhost:5050/api/routes/1 -X PUT -d '{"status": 404}'

Delete a route:

    curl localhost:5050/api/routes/1 -X DELETE


## Deploy

### Build the Docker image

Build the docker image:

    ./gradlew buildDockerImage

This builds an image named `thecodesmith/mock-api`.

### Run the container

    docker run -p 5050:5050 -it thecodesmith/mock-api

The app runs on port `5050` inside the container, and can be bound to any
desired port.

### Persist data between restarts

The app writes its data to the `/web/data` directory inside the container.
The container's filesystem is ephemeral, and will disappear when the container
is stopped. To persist data between restarts, mount a host volume when starting
the container, like this:

    mkdir data
    docker run -p 5050:5050 -v $(pwd)/data:/web/data -it thecodesmith/mock-api
