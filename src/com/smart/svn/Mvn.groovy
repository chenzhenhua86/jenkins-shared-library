package com.smart.svn
import com.smart.svn.BuildMessage

def init() {
    this.msg = new BuildMessage()
    def isSuccess = false
    def errMsg = ""
    retry(3) {
        try {
            sh "mvn clean package -Dmaven.test.skip=true"
            sh "cp ${PROJECT}/target/${PROJECT}-assembly.tar.gz  /root/.m2/resources/${PROJECT}/${PROJECT}-${BUILD_NUMBER}.${SVN_REVISION}-assembly.tar.gz"
            isSuccess = true
        }catch (Exception err) {
            //ignore
            errMsg = err.toString()
        }
        // check if build success
        def stage = env.STAGE_NAME + '-mvn'
        if(isSuccess){
            // updateGitlabCommitStatus(name: "${stage}", state: 'success')
            this.msg.updateBuildMessage(env.BUILD_TASKS, "${stage} OK...  √")
        }else {
            // updateGitlabCommitStatus(name: "${stage}", state: 'failed')
            this.msg.updateBuildMessage(env.BUILD_TASKS, "${stage} Failed...  x")
            // throw exception，aborted pipeline
            error errMsg
        }

        return this
    }
}



