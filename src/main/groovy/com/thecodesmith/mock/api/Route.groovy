package com.thecodesmith.mock.api

import groovy.transform.CompileStatic

/**
 * @author Brian Stewart
 */
@CompileStatic
class Route {

    @Delegate Database database = new Database()

    def getAll() {
        sql.rows 'SELECT * FROM routes'
    }

    def create(Map data) {
        sql.executeInsert('')
    }

    def get(int id) {
        sql.firstRow 'SELECT * FROM routes WHERE id = ?', id
    }

    def update(String path, Map data) {
        sql.executeUpdate('')
    }

    def delete(int id) {
        sql.execute 'DELETE FROM routes WHERE id = ?', id
    }
}