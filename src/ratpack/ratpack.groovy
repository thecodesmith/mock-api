import com.thecodesmith.mock.api.DatabaseService
import com.thecodesmith.mock.api.RouteService
import org.slf4j.LoggerFactory

import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.fromJson
import static ratpack.jackson.Jackson.json

final log = LoggerFactory.getLogger(ratpack)

ratpack {
    bindings {
        bind DatabaseService
        bind RouteService
    }

    handlers {
        get {
            render json([message: 'See the README for API documentation'])
        }

        prefix('api') {
            prefix('routes') {
                path {
                    byMethod {
                        get {
                            def router = registry.get(RouteService)
                            render json(router.getAll())
                        }

                        post {
                            parse fromJson(Map) then {
                                try {
                                    def router = registry.get(RouteService)
                                    render json(router.create(it))
                                } catch (e) {
                                    response.status 400
                                    render json([error: e.message])
                                }
                            }
                        }
                    }
                }

                path(':id') {
                    byMethod {
                        get {
                            def id = pathTokens.id as int
                            def router = registry.get(RouteService)
                            def result = router.get(id)

                            if (result) {
                                render json(result)
                            } else {
                                response.status(404).send()
                            }
                        }

                        put {
                            parse fromJson(Map) then { Map payload ->
                                def id = pathTokens.id as int
                                def router = registry.get(RouteService)
                                def result = router.update(id, payload)
                                def status = result ? 204 : 404

                                response.status(status).send()
                            }
                        }

                        delete {
                            def id = pathTokens.id as int
                            def router = registry.get(RouteService)
                            def result = router.delete(id)
                            def status = result ? 204 : 404

                            response.status(status).send()
                        }
                    }
                }
            }
        }

        path(':path:.+') {
            def method = request.method.toString()
            def path = pathTokens.path
            def router = registry.get(RouteService)
            def route = router.getByPath(method, path)

            if (route) {
                response.status(route.status)
                        .contentType(route.content_type)
                        .send(route.body_content)
            } else {
                response.status(404)
                render json([error: "No route matches: $method $path".toString()])
            }
        }
    }
}
