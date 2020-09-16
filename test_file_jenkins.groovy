
def String package_source_dir = "scripts"
def String autopackage_dir = "package"
def String version = "V.import.${env.BUILD_NUMBER}"
def String schema = "schema_name"

pipeline {
    agent any
    stages{
        stage('Example') {
            steps{
                script{
                    sh "printenv"
                    sh "mkdir ${autopackage_dir}"
                    sh "chmod -R 777 ${autopackage_dir}"
                    dir (autopackage_dir) {
                        //sh "find . -type f -printf \"%T@ %p\\n\" | sort -nr | cut -d\\  -f2-"
                        sh "echo version is ${version}"
                        sh "mkdir ${version}"
                        sh "mkdir ${version}/${schema}"
                        //sh "mv ${package_source_dir}/* ${version}/${schema}/"
                        
                    }
                }
            }
        }
    }
}
