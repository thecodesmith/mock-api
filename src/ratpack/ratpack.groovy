import com.thecodesmith.mock.api.Route
import org.slf4j.LoggerFactory

import static ratpack.groovy.Groovy.ratpack
import static ratpack.jackson.Jackson.json
import static ratpack.jackson.Jackson.jsonNode

final log = LoggerFactory.getLogger(ratpack)

ratpack {
    bindings {
        bind Route
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
                            render json(registry.get(Route).getAll())
                        }
                        post {
                            def data = parse jsonNode()
                            println "data: $data"
                            render data.map { it.get('name').asText() }
                        }
                    }
                }

                path(':id') {
                    byMethod {
                        get {
                            render "a single route id: $pathTokens.id"
                        }
                        put {
                            render "updating $pathTokens.id"
                        }
                        delete {
                            render "deleting $pathTokens.id"
                        }
                    }
                }
            }
        }

        path(':foo:.+') {
            render "called dynamic: $pathTokens.foo"
        }
    }
}
