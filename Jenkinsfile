#!groovy

@Library('jenkinsLibrary') _

def tools = new org.devops.tools()



String workspace = "/opt/jenkins/workspace"
String hellow="/opt/jenkins/workspace/target/demo-0.0.1-SNAPSHOT.jar"
String pid="$(ps -ef|grep demo-0.0.1-SNAPSHOT.jar|grep -v grep | awk '{print \$2}')"
//Pipeline
pipeline {
    agent { node {  label "master"   //指定运行节点的标签或者名称
        customWorkspace "${workspace}"   //指定运行工作目录（可选）
    }
    }

    options {
        timestamps()  //日志会有时间
        skipDefaultCheckout()  //删除隐式checkout scm语句
        disableConcurrentBuilds() //禁止并行
        timeout(time: 1, unit: 'HOURS')  //流水线超时设置1h
    }

    stages {
        //下载代码
        stage("GetCode"){ //阶段名称
            when { environment name: 'test', value: 'abcd' }
            steps{  //步骤
                timeout(time:5, unit:"MINUTES"){   //步骤超时时间
                    script{ //填写运行代码
                        println('获取代码')
                        tools.PrintMes("获取代码",'green')
                        println("${test}")

                        git credentialsId: '58821007-7e5c-4ccb-81f5-b51369b9029d', url: 'https://gitee.com/sheyuzsz/sonar-demo.git'
                    }
                }
            }
        }

        stage("01"){
            failFast true
            parallel {

                //构建
                stage("Build"){
                    steps{
                        timeout(time:20, unit:"MINUTES"){
                            script{
                                println('应用打包')
                                tools.PrintMes("应用打包",'green')
                                mvnHome = tool "m2"
                                println(mvnHome)

                                sh "${mvnHome}/bin/mvn clean package -DskipTests "
                            }
                        }
                    }
                }

                //代码扫描
                stage("CodeScan"){
                    steps{
                        timeout(time:30, unit:"MINUTES"){
                            script{
                                print("代码扫描")
                                tools.PrintMes("代码扫描",'green')
                            }
                        }
                    }
                }
            }
        }
    }

    //构建后操作
    post {
        always {
            script{
                println("always")
                sh """
                    echo "ddddddddddddddddddddddd"
                    
                    if  [ -f $hellow ] ; then
                    cp -r $hellow $workspace
                    fi
                    
                    function stop(){
                    if [ -n "$pid" ]
                    then
                    echo "pid进程 :$pid"
                    kill -9 $pid
                    else
                    echo "进程没有启动"
                    fi
                    }
                    stop
                    
                    BUILD_ID=dontKillMe
                    function start(){
                    cd $workspace
                    nohup /usr/local/java/jdk17/bin/java -jar demo-0.0.1-SNAPSHOT.jar  &
                    }
                    start
                    echo "dddddddddd333333333d"
                    """
            }
        }

        success {
            script{
                currentBuild.description = "\n 构建成功!"
            }
        }

        failure {
            script{
                currentBuild.description = "\n 构建失败!"
            }
        }

        aborted {
            script{
                currentBuild.description = "\n 构建取消!"
            }
        }
    }
}