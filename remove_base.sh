#!/bin/bash

function remove_app() {
    docker rmi distribuida/app
}

function remove_resource() {
    docker rmi distribuida/resource
}

function remove_network() {
    docker network rm t2-distribuida
}

remove_app
remove_resource
remove_network