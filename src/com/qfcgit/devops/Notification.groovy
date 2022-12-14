package com.qfcgit.devops


/**
 *
 * @param type
 * @param credentialsId
 * @param title
 * @return
 */
def getObject(String project, String receiver, String credentialsId, String title) {
    this.project = project
    this.receiver = receiver
    this.credentialsId = credentialsId
    this.title = title
    return this
}


def notification(String type){
    String msg ="๐๐ ${this.title} ๐๐"

    if (this.title == "") {
        msg = "<font color='info'>๐๐ ๆตๆฐด็บฟๆๅๅฆ ๐๐</font>"
    }
    // failed
    if (type == "failure") {
        msg ="๐โ ${this.title} โ๐"
        if (this.title == "") {
            msg = "<font color='warning'>๐โ ๆตๆฐด็บฟๅคฑ่ดฅไบ โ๐</font>"
        }
    }
    String title = msg
    // rich notify msg
    msg = genNotificationMessage(msg)
    if( this.receiver == "dingTalk") {
        try {
            //new DingTalk().markDown(title, msg, this.credentialsId)
        } catch (Exception ignored) {}
    }else if(this.receiver == "wechat") {
        try {
            new Wechat().markDown(title, msg, this.credentialsId)
        } catch (Exception ignored) {}
    }else if (this.receiver == "email"){
        //todo
    }else{
        error "no support notify type!"
    }
}


/**
 * get notification msg
 * @param msg
 * @return
 */
def genNotificationMessage(msg) {
    // project
    msg = "${msg}  \n  **้กน็ฎๅ็งฐ**: ${this.project}"
    // get git log
    def gitlog = ""
    try {
        sh "git log --oneline -n 1 > gitlog.file"
        gitlog = readFile "gitlog.file"
    } catch (Exception ignored) {}

    if (gitlog != null && gitlog != "") {
        msg = "${msg}  \n  **Git log**: ${gitlog}"
    }
    // get git branch
    def gitbranch = env.BRANCH_NAME
    if (gitbranch != null && gitbranch != "") {
        msg = "${msg}  \n  **Git branch**: ${gitbranch}"
    }
    // build tasks
    msg = "${msg}  \n  **Build Tasks**: ${env.BUILD_TASKS}"

    // get buttons
    msg = msg + getButtonMsg()
    return msg
}
def getButtonMsg(){
    String res = ""
    def  buttons = [
            [
                    "title": "ๆฅ็ๆตๆฐด็บฟ",
                    "actionURL": "${env.RUN_DISPLAY_URL}"
            ],
            [
                    "title": "ไปฃ็?ๆซๆ็ปๆ",
                    "actionURL": "http://sonar_url/dashboard?id=${this.project}"
            ]
    ]
    buttons.each() {
        if(res == ""){
            res = "   \n >"
        }
        res = "${res} --- ["+it["title"]+"]("+it["actionURL"]+") "
    }
    return res
}