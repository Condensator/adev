import groovy.json.*
import groovy.io.FileType	
import java.io.*
import java.nio.file.*
import org.json.*
import groovyx.net.http.*
	
@NonCPS
def createPackageManifest(String name, List<String> scripts) {
	def manifest = new JsonBuilder()
	manifest name: name, operation: "create", type: "regular", enabled: true, closed: false, tags: [], scripts: scripts
	echo "Generating manifest:"
	def manifestOutput = manifest.toPrettyString()
	return manifestOutput
}

def String autopackage_dir = "package"
def String version = "V.import.${env.BUILD_NUMBER}"
def String schema = "schema_name"

pipeline {
    agent any
    stages{
        stage('Example') {
            steps{
                script{
		    def String package_source_dir = "${env.WORKSPACE}/scripts"
                    sh "printenv"
                    sh "chmod -R 777 ${autopackage_dir}"
                    dir (autopackage_dir) {
                        //sh "find . -type f -printf \"%T@ %p\\n\" | sort -nr | cut -d\\  -f2-"
                        sh "echo version is ${version}"
                        sh "mkdir ${version}"
                        sh "mkdir ${version}/${schema}"
                        sh "mv ${package_source_dir}/* ${version}/${schema}/"

			def  scripts = findFiles(glob: "${version}/**/*")
				//sh (script: "find ${version} -type f -printf \"%f\\n\"", returnStdout: true).trim()
			def json = [name: version, 
				    operation: "create", 
				    type: "regular", 
				    enabled: true, 
				    closed: false, 
				    tags: [], 
				    scripts: scripts]
			    writeJSON(file: 'message1.json', json: json)
		    
                        sh "echo scripts list: ${scripts}"
                        sh "curl"
	                    
                        
                    }
                }
            }
        }
    }
}
