//Plugin required: Pipeline Utility Steps	

def String autopackage_dir = "package"
def String version = "V.import.${env.BUILD_NUMBER}"

def String schema = "PRX_RS"
def String packageLoaderPath="http://localhost:9014/Projects/12/packages"

pipeline {
    agent any
    stages{
        stage('Test') {
            steps{
                script{
		    def String package_source_dir = "${env.WORKSPACE}/scripts"
                    sh "printenv"
                    sh "chmod -R 777 ${autopackage_dir}"
                    dir (autopackage_dir) {
                        sh "echo version is ${version}"
                        sh "mkdir ${version}"
			    
                        sh "mkdir ${version}/${schema}"
                        sh "mv ${package_source_dir}/* ${version}/${schema}/"
			def scripts_list = findFiles(glob: "${version}/**/*")
			def scripts = []
			for (def file : scripts_list) {
 				scripts<<file.getName()
			}
			def json = [name: version, 
				    operation: "create", 
				    type: "regular", 
				    enabled: true, 
				    closed: false, 
				    tags: [], 
				    scripts: scripts]
			writeJSON(file: "${version}/package.json", json: json)
			def zipFileName = "${version}.dbmpackage.zip"
			zip(zipFile:"${zipFileName}", dir: "${version}")
			sh "curl -F \"packageFile=@${zipFileName}\" -X POST ${packageLoaderPath}"
	                
			sh "java -jar package command"
                        
                    }
                }
            }
        }
    }
}
