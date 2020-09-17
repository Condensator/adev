//Plugin required: Pipeline Utility Steps	

def automation_jar_path = "/dbmaestro/DBmaestroAgent.jar"
def project_name = "PrjBT"
def rs_environment_name = "Release Source"
def auth_type = "DBmaestroAccount"
def user_name = "admin@dbmaestro.com"
def auth_token = "123456"
def dbm_address = "192.168.99.1:8017"
def package_source_dir = "scripts"
def autopackage_dir = "package"
def String version = "V.import.${env.BUILD_NUMBER}"

def String schema = "liq2"
def String packageLoaderPath="http://192.168.99.1:4567/Projects/110/packages"

 

pipeline {

  agent any

  stages {

        stage('Packaging') {

          steps{

            script{

               sh "chmod -R 777 ${autopackage_dir}"

             dir (autopackage_dir) {

               sh "echo version is: ${version}"

               sh "ls ${package_source_dir}"

               sh "mkdir ${version}"

               sh "ls -ltr ${version}"

               sh "mkdir ${version}/${schema}"

               sh "mv ${package_source_dir}/* ${version}/${schema}/"

                                           def scripts_list = findFiles(glob: "${version}/**/*")

                                           def scripts = []

                                           for (def file : scripts_list) {

                                                          def scr=[name: file.getName()]

                                                         scripts<<scr

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
		     
		     sh "java -jar ${automation_jar_path} -Package -ProjectName ${project_name} -IgnoreScriptWarnings y -AuthType ${auth_type} -Server ${dbm_address} -PackageName ${version} -UserName ${user_name} -Password ${auth_token}"

 

             }

            }

          }

        }

 

        stage('Pre-Check') {

          steps{

            script{

              dir (autopackage_dir) {

                sh "java -jar ${automation_jar_path} -PreCheck -ProjectName ${project_name} -Server ${dbm_address} -PackageName ${version} -AuthType ${auth_type} -UserName ${user_name} -Password ${auth_token}"

              }

            }

          }

        }

   

        stage('Upgrade RS') {

          steps{

            script{

              dir (autopackage_dir) {

                sh "java -jar ${automation_jar_path} -Upgrade -ProjectName ${project_name} -EnvName ${rs_environment_name} -PackageName ${version} -Server ${dbm_address} -AuthType ${auth_type} -UserName ${user_name} -Password ${auth_token}"

              }

            }

          }

        }

      }

    }
