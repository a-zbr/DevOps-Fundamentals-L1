properties([pipelineTriggers([githubPush()])])

pipeline {
    agent { label 'amazon' }

    parameters {
        string(name: 'Branch',
            description: 'Default branch to create/use',
            defaultValue: 'main', trim: true)
        string(
            name: 'Git_repository_URL',
            description: 'Public/Private full URL for repository',
            defaultValue: 'git@github.com:a-zbr/DevOps-Fundamentals-L1.git', trim: true)
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '10'))
    }

    tools {
        maven '3.9.0'
    }

    stages {
        stage('Clear workspace') {
            steps{
                echo 'Clearing workspace'
                deleteDir()
            }
        }

        stage('Clear docker environment') {
            steps{
                sh '''
                    echo 'Delete all containers'
                    docker ps -a -q | xargs --no-run-if-empty docker rm -f
                    #docker rm -f $(docker ps -a -q) &> /dev/null
                '''
            }
        }

        stage('Checkout') {
            steps{
                checkout([$class                           : 'GitSCM', branches: [[name: params.Branch]],
                    doGenerateSubmoduleConfigurations: false, extensions: [],
                    submoduleCfg                     : [],
                    userRemoteConfigs                : [[credentialsId: "github-final-project-key",
                                                        url          : params.Git_repository_URL]]])
            }
        }

        stage('Compile') {
            steps {
                dir('05-Final_Project/spring-application') {
                    sh 'mvn compile'
                }
            }
        }

        stage('Tests') {
            steps {
                dir('05-Final_Project/spring-application') {
                    sh 'mvn test'
                }
            }
        }

        stage('Dockerfile lint') {
            steps {
                dir('05-Final_Project/spring-application') {
                    sh 'hadolint docker/Dockerfile'
                }
            }
        }

        // stage ('Starting Build job') {
        //     steps {
        //         echo "Triggering Build job for this application"
        //         build job: 'Build-shopping-cart-app', parameters: [string(name: 'Branch', value: params.Branch),
        //                                                            string(name: 'Git_repository_URL', value: params.Git_repository_URL)], wait: false
        //     }
        // }
    }
}