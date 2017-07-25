package com.thecodesmith.mock.api

import groovy.transform.Canonical

@Canonical
class Route {
    int id
    String name
    String method
    int status
    String path
    boolean active
    String content_type
    String body_content
}
