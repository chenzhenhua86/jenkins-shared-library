package com.smart.svn

import com.smart.svn.BuildMessage


def init(String projectVersion="", Boolean waitScan = true) {
    this.waitScan = waitScan
    this.msg = new BuildMessage()
    if (projectVersion == ""){
        projectVersion = env.SVN_REVISION
    }
    sh "sed -i \"s/<JOB_NAME>/${PROJECT}/\" sonar-project.properties"
    sh "echo '\nsonar.projectVersion=${projectVersion}' >> sonar-project.properties"
    sh "cat sonar-project.properties"
    return this
}

def start() {
    try {
        this.startToSonar()
    }
    catch (Exception exc) {
        throw exc
    }
    return this
}

def startToSonar() {
    withSonarQubeEnv('mysonar') {
        sh "sonar-scanner -X;"
        sleep 5
    }
    if(this.waitScan){
        //wait 3min
        timeout(time: 3, unit: 'MINUTES') {
            def qg = waitForQualityGate()
            String stage = "${env.stage_name}"
            if (qg.status != 'OK') {
                this.msg.updateBuildMessage(env.BUILD_TASKS, "${stage} Failed...  ×")
                //updateGitlabCommitStatus(name: "${stage}", state: 'failed')
                error "Pipeline aborted due to quality gate failure: ${qg.status}"
            }else{
                this.msg.updateBuildMessage(env.BUILD_RESULT, "${stage} OK...  √")
               // updateGitlabCommitStatus(name: "${stage}", state: 'success')
            }
        }
    }else{
        echo "skip waitScan"
    }
    return this
}