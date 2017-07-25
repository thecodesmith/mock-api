package com.thecodesmith.mock.api

import groovy.sql.Sql
import groovy.transform.Memoized

/**
 * @author Brian Stewart
 */
class Database {

    String driver = 'org.sqlite.JDBC'
    String connection = 'jdbc:sqlite:routes.db'

    @Memoized Sql getSql() {
        Sql.newInstance connection, driver
    }

    void init() {
        sql.execute '''
                CREATE TABLE routes (
                    id           int,
                    name         string,
                    method       string,
                    status       int,
                    path         string,
                    active       boolean,
                    content_type string,
                    body_content string
                )'''
    }

    void destroy() {
        sql.execute 'DROP TABLE IF EXISTS routes'
    }

    String toString() {
        "Database(connection: $connection)"
    }
}
