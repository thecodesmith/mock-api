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
            render 'root'
        }
        get('foo') {
            render 'called foo endpoint'
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

                            render json(router.get(id))
                        }
                        put {
                            parse fromJson(Map) then { Map payload ->
                                def id = pathTokens.id as int
                                def router = registry.get(RouteService)
                                def result = router.update(id, payload)

                                if (result) {
                                    response.status(204).send()
                                } else {
                                    response.status(404)
                                    render json([error: "No route with id: $id".toString()])
                                }
                            }
                        }
                        delete {
                            def id = pathTokens.id as int
                            def router = registry.get(RouteService)
                            def result = router.delete(id)

                            if (result) {
                                response.status(204).send()
                            } else {
                                response.status(404)
                                render json([error: "No route with id: $id".toString()])
                            }
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

            if (!route) {
                response.status(404)
                render json([error: "No route matches: $method $path".toString()])
            } else {
                response.status(route.status)
                        .contentType(route.content_type)
                        .send(route.body_content)
            }
        }
    }
}
