package com.qfcgit.devops

def updateBuildMessage(String source, String add) {
    if(!source){
        source = ""
    }
    env.BUILD_TASKS = source + add + "\n"
    return env.BUILD_TASKS
}