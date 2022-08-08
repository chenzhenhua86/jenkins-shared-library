import com.smart.svn.Hello
import com.smart.svn.Docker
import com.smart.svn.Deploy
import com.smart.svn.Notification
import com.smart.svn.Sonar
import com.smart.svn.Robot
import com.smart.svn.Mvn
import com.smart.svn.Apibuild


def hello(String content) {
    return new Hello().hello(content)
}


/**
 *
 * @param repo, code_url/test-project/test-xxx/trunk
 * @param tag, v1.0
 * @param dockerfile
 * @param credentialsId
 * @param context
 */
def docker(String repo, String tag, String credentialsId, String dockerfile="Dockerfile", String context=".") {
    return new Docker().docker(repo, tag, credentialsId, dockerfile, context)
}


/**
 *
 * @param resourcePath
 * @param watch
 * @param workloadFilePath
 * @return
 */
def deploy(String resourcePath, Boolean watch = true, String workloadFilePath){
    return new Deploy().init(resourcePath, watch, workloadFilePath)
}


/**
 * notificationSuccess
 * @param project
 * @param receiver
 * @param credentialsId
 * @param title
 * @return
 */
def notificationSuccess(String project, String receiver="wechat", String credentialsId="wechat_smart", String title=""){
    new Notification().getObject(project, receiver, credentialsId, title).notification("success")
}

/**
 * notificationFailed
 * @param project
 * @param receiver
 * @param credentialsId
 * @param title
 * @return
 */
def notificationFailed(String project, String receiver="wechat", String credentialsId="wechat_smart", String title=""){
    new Notification().getObject(project, receiver, credentialsId, title).notification("failure")
}

/**
 * sonarqube scanner
 * @param projectVersion
 * @param waitScan
 * @return
 */
def scan(String projectVersion="", Boolean waitScan = true) {
    return new Sonar().init(projectVersion, waitScan)
}

/**
 *
 * @param comp
 * @return
 */
def robotTest(String comp=""){
    new Robot().acceptanceTest(comp)
}

/**
 *
 * @return
 */
def mvn(){
    new Mvn().init()
}


/**
 *
 * @param jobname
 * @return
 */
def frontbuild(String jobname=""){
    new Apibuild().start(jobname)
}



