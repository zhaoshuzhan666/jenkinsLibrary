package org.devops

    //构建类型
    def Build(buildType,buildShell){
        def buildTools = ["mvn":"M2","ant":"ANT","gradle":"GRADLE","npm":"NPM"]
        println("当前选择的构建类型为 ${buildType}")
        sh "${buildHome}/bin/${buildType} ${buildShell}"
    
}