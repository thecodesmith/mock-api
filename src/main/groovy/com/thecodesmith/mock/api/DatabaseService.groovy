package com.thecodesmith.mock.api

import groovy.sql.Sql
import groovy.transform.Memoized
import groovy.util.logging.Slf4j
import ratpack.service.Service
import ratpack.service.StartEvent

/**
 * @author Brian Stewart
 */
@Slf4j
class DatabaseService implements Service {

    String driver = 'org.sqlite.JDBC'
    String connection = 'jdbc:sqlite:routes.db'

    @Override
    void onStart(StartEvent event) {
        log.info 'Starting database service'
        init()
    }

    @Memoized Sql getSql() {
        Sql.newInstance connection, driver
    }

    void init() {
        log.info 'Creating database routes.db if it does not already exist'
        sql.execute '''
                CREATE TABLE IF NOT EXISTS routes (
                    name         string,
                    method       string,
                    status       int,
                    path         string,
                    active       boolean,
                    content_type string,
                    body_content string
                )'''
    }

    String toString() {
        "DatabaseService(connection: $connection)"
    }
}
