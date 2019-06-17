#!/bin/bash

function bake_app() {
    docker build -t distribuida/app app
}

function bake_resource() {
    docker build -t distribuida/resource resource
}

function create_network() {
    docker network create --subnet=172.18.0.0/16 t2-distribuida
}

bake_app
bake_resource
create_network