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

## Possible Stacks

- Ratpack/GORM
- Grails
- Express.js
- Clojure/ClojureScript
- Go
