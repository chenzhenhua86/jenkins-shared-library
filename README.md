# jenkins-library
Shared Library for Jenkins Pipeline

关于Shared Library的更多信息请参考Using libraries
配置 Jenkins 使用 Shared Library
若要使用 Shared Library, 您可以通过两种方式进行配置:

Declarative Pipeline 通过简单配置使用 Jenkinsfile 进行控制

Jenkinsfile 在执行时, 也是调用了 Jenkins Shared Library

Scripted Pipeline 通过一些配置使用 Script 进行控制

Declarative Pipeline
1. 创建一个流水线(Pipeline)项目
   创建项目

2. 配置使用 Jenkinsfile
   在项目配置界面，流水线 处，选择 Pipeline script from SCM

SCM
Git
Repository URL
https://github.com/Statemood/jenkins-library
Branch Specifier (blank for 'any')
origin/master
脚本路径
Jenkinsfile
或选择 Pipeline script, 然后输入如下配置

@Library('github.com/Statemood/jenkins-library@dev') _

entry([git_repo: 'https://github.com/Statemood/simple-java-maven-app.git'])
保存即可开始使用。

3. 配置 Global Pipeline Libraries
   配置方式参考 https://jenkins.io/doc/book/pipeline/shared-libraries/#global-shared-libraries

# jenkinsfile  for  git   examples
```bash
@Library('test-devops') _

pipeline {
  agent {
    kubernetes {
      cloud 'k8s115'
      label 'slave'
      inheritFrom 'jenkins-slave'
     // defaultContainer 'node'
      yaml libraryResource('base/base.yaml')
    }
  }

    options {
		timeout(time: 20, unit: 'MINUTES')
		//gitLabConnection('gitlab')
	}
    environment {
        IMAGE_REPO = "harbor_url/library/qfcsspiders"
        IMAGE_CREDENTIAL = "dockerhub"
    }
    stages {
        stage('checkout') {
            steps {
                    checkout scm

            }
        }

        stage('CI'){
            failFast true
            parallel {
                stage('Unit Test') {
                    steps {
                        echo "Unit Test Stage Skip..."
                    }
                }
                stage('Code Scan') {
                    steps {
                        container('tools') {
                            script {
                               devops.scan().start()
                            }
                        }
                    }
                }
            }
        }


        stage('docker-image') {
            steps {
            container('docker') {
                    script{
                        devops.docker(
                            "${IMAGE_REPO}",
                            "${GIT_COMMIT}",
                            IMAGE_CREDENTIAL
                        ).build().push()
                    }
                 }
            }
        }

        stage('deploy') {
            steps {
                container('kubectl') {
                    script{
                    	devops.deploy("manifests", "manifests/k8s.yaml").start()

                    }
                }
            }
        }

        stage('integration test') {
            steps {
                container('tools') {
                    script{
                    	devops.robotTest("qfcsspiders")
                    }
                }
            }
        }




    }

     post {
        success {
            script{
                devops.notificationSuccess("qfcsspiders","wechat")
            }
        }
        failure {
            script{
                devops.notificationFailed("qfcsspiders","wechat")
            }
        }
    }
}

```
# jenkinsfile  for  svn   examples
```bash 
@Library('test-devops') _
def sonarfile = libraryResource "smart/sonar-project.properties"
def dockerfile = libraryResource "smart/Dockerfile"
def k8syaml = libraryResource "smart/manifests/k8s.yaml"
def svcyaml = libraryResource "smart/manifests/svc.yaml"

pipeline {
  agent {
    kubernetes {
      cloud 'k8s115'
      label 'slave'
      inheritFrom 'jenkins-slave'
      yaml libraryResource('base/smartbase.yaml')
    }
  }
    options {
		timeout(time: 20, unit: 'MINUTES')
		//gitLabConnection('gitlab')
	}
    environment {
        PROJECT = "assets-cms"
       // CurrEnv = "test"
        PROPort= "8160"
        OUTPort= "31160"

        IMAGE_REPO = "harbor_url/qfcsmart/${PROJECT}"
        IMAGE_CREDENTIAL = "smartharbor"
        IMGTAG = "${CurrEnv}${SVN_REVISION}-${BUILD_NUMBER}"
    }
    stages {

        stage('sdk-front') {
            steps {
              script{
                smartops.frontbuild()
              }
          }
        }
        stage('checkout') {
            steps {
               checkout scm
          }
        }
        stage('mvn-package') {
            steps {
                container('maven') {
                    script{
                       smartops.mvn()
                    }
                }
            }
        }
        stage('CI'){
            failFast true
            parallel {
                stage('Unit Test') {
                    steps {
                        echo "Unit Test Stage Skip..."
                    }
                }
                stage('Code Scan') {
                    steps {
                        container('tools') {
                            script {
                               sh "echo \'${sonarfile}\' > sonar-project.properties"
                               smartops.scan().start()
                            }
                        }
                    }
                }
            }
        }
        stage('docker-image') {
            steps {
            container('docker') {
                    script{
                        sh "echo \'${dockerfile}\' > Dockerfile"
                        smartops.docker(
                            "${IMAGE_REPO}",
                            "${IMGTAG}",
                            IMAGE_CREDENTIAL
                        ).build().push()
                    }
                 }
            }
        }
        stage('deploy') {
            steps {
                container('kubectl') {
                sh """
                    mkdir manifests
                    echo \'${k8syaml}\' > manifests/k8s.yaml
                    echo \'${svcyaml}\' > manifests/svc.yaml
                """
                    script{
                    	smartops.deploy("manifests", "manifests/k8s.yaml").start()
                    }
                }
            }
        }
        stage('integration test') {
            steps {
                container('tools') {
                    script{
                    	smartops.robotTest(PROJECT)
                    }
                }
            }
        }
    }
     post {
        success {
            script{
                smartops.notificationSuccess(PROJECT,"wechat")
            }
        }
        failure {
            script{
                smartops.notificationFailed(PROJECT,"wechat")
            }
        }
    }
}
```



