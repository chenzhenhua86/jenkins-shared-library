package com.smart.svn
import groovy.json.JsonOutput


def sendRequest(method, data, credentialsId, Boolean verbose=false, codes="100:399") {
    def reqBody = new JsonOutput().toJson(data)
    withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        def response = httpRequest(
                httpMode:method,
                url: "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=${PASSWORD}",
                requestBody:reqBody,
                validResponseCodes: codes,
                contentType: "APPLICATION_JSON",
                quiet: !verbose
        )
    }
}

def markDown(String title, String text, String credentialsId, Boolean verbose=false) {
    def data = [
            "msgtype": "markdown",
            "markdown": [
                    "title": title,
                    "content": text
            ]
    ]
    this.sendRequest("POST", data, credentialsId, verbose)
}

