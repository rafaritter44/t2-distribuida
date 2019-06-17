#!/bin/bash

function node_ip_1() {
    echo '172.18.0.23'
}

function node_ip_2() {
    echo '172.18.0.24'
}

function node_ip_3() {
    echo '172.18.0.25'
}

function node_name_1() {
    echo 'node-1'
}

function node_name_2() {
    echo 'node-2'
}

function node_name_3() {
    echo 'node-3'
}

function node_line_1() {
    echo '0'
}

function node_line_2() {
    echo '1'
}

function node_line_3() {
    echo '2'
}

function run_node() {
    ip="$1"
    name="$2"
    line="$3"
    docker run --net t2-distribuida --ip "$ip" --name "$name" --rm -d distribuida/app arquivo_config.txt "$line"
}

function resource_ip() {
    echo '172.18.0.22'
}

function resource_name() {
    echo 'resource'
}

function run_resource() {
    docker run --net t2-distribuida --ip $(resource_ip) --name $(resource_name) --rm -d distribuida/resource
}

run_resource
run_node $(node_ip_3) $(node_name_3) $(node_line_3)
run_node $(node_ip_2) $(node_name_2) $(node_line_2)
run_node $(node_ip_1) $(node_name_1) $(node_line_1)