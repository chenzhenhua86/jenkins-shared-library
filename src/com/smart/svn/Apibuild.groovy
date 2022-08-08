package com.smart.svn

import com.smart.svn.BuildMessage

def start(jobname) {
    try{
        echo "Trigger to execute Acceptance Testing"
        def fb = build job: jobname,
                wait: true,
                propagate: false
        def result = fb.getResult()
        def msg = "${env.STAGE_NAME}... "
        if (result == "SUCCESS"){
            msg += "√ success"
        }else if(result == "UNSTABLE"){
            msg += "⚠ unstable"
        }else{
            msg += "× failure"
        }
        echo fb.getAbsoluteUrl()
        env.ROBOT_TEST_URL = fb.getAbsoluteUrl()
        new BuildMessage().updateBuildMessage(env.BUILD_TASKS, msg)
    } catch (Exception exc) {
        echo "trigger  execute Acceptance Testing exception: ${exc}"
        new BuildMessage().updateBuildMessage(env.BUILD_RESULT, msg)
    }
}