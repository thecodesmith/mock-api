package com.thecodesmith.mock.api

import com.google.inject.Inject
import groovy.sql.Sql

class RouteService {

    static REQUIRED_KEYS = ['name', 'path']
    static DEFAULTS = [
            method: 'GET',
            status: 200,
            active: true,
            content_type: 'application/json',
            body_content: ''
    ]

    @Delegate DatabaseService databaseService

    @Inject
    RouteService(DatabaseService databaseService) {
        this.databaseService = databaseService
    }

    def getAll() {
        sql.rows 'SELECT rowid as id, * FROM routes'
    }

    Map create(Map data) {
        validate(data)

        def values = setDefaults(data)

        def query = """
                INSERT INTO routes (
                    name, 
                    method, 
                    status, 
                    path, 
                    active, 
                    content_type, 
                    body_content
                ) VALUES (
                    $values.name,
                    ${values.method.toString().toUpperCase()},
                    $values.status,
                    ${values.path - ~/^\//},
                    $values.active,
                    $values.content_type,
                    $values.body_content
                ) """

        def result = sql.executeInsert query, ['rowid']

        [id: result[0]['last_insert_rowid()']]
    }

    def get(int id) {
        sql.firstRow '''
                SELECT rowid as id, * 
                  FROM routes 
                 WHERE rowid = ?''', id
    }

    Route getByPath(String method, String path) {
        def row = sql.firstRow '''
                SELECT rowid as id, *
                  FROM routes 
                 WHERE active = 1
                   AND method = ?
                   AND path   = ?''',
                [method.toUpperCase(), path - ~/^\//]

        row ? new Route(row) : null
    }

    boolean update(int id, Map data) {
        def values = data.collect { column, value ->
            value instanceof String ?
                    "$column = '$value'" :
                    "$column = $value"
        } join(',\n')

        sql.executeUpdate """
                UPDATE routes
                   SET ${ Sql.expand(values) }
                 WHERE rowid = $id"""
    }

    def delete(int id) {
        sql.execute 'DELETE FROM routes WHERE rowid = ?', id
    }

    protected void validate(Map data) {
        def missing = REQUIRED_KEYS.findAll { !data.containsKey(it) }
        if (missing) {
            throw new RuntimeException("Missing required parameter(s): ${missing.join(', ')}")
        }
    }

    protected Map setDefaults(Map data) {
        DEFAULTS + data
    }
}