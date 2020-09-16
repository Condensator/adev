def String script_dir = "scripts"
def String varsion = "V.import.${env.BUILD_NUMBER}"

pipeline {
    agent any
    stages{
        stage('Example') {
            steps{
                script{
                    sh "chmod -R 777 ${script_dir}"
                    dir (script_dir) {
                        sh "find . -type f -printf \"%T@ %p\\n\" | sort -nr | cut -d\\  -f2-"
                    }
                }
            }
        }
    }
}
