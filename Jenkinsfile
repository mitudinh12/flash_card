pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'thaonhi6991/flash_card'
        DOCKER_TAG = 'latest'
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'jenkins', url: 'https://github.com/nhidinh91/flash_card.git'
            }
        }

//         stage('Build') {
//             steps {
//                 script {
//                     if (isUnix()) {
//                         sh 'mvn clean package'
//                     } else {
//                         bat 'mvn clean package'
//                     }
//                 }
//             }
//         }

//         stage('Test') {
//             steps {
//                 script {
//                     if (isUnix()) {
//                         sh 'mvn test'
//                     } else {
//                         bat 'mvn test'
//                     }
//                 }
//             }
//         }
//
//         stage('Code Coverage') {
//             steps {
//                 script {
//                     if (isUnix()) {
//                         sh 'mvn jacoco:report'
//                     } else {
//                         bat 'mvn jacoco:report'
//                     }
//                 }
//             }
//         }
//
//         stage('Publish Test Results') {
//             steps {
//                 junit '**/target/surefire-reports/*.xml'
//             }
//         }
//
//         stage('Publish Coverage Report') {
//             steps {
//                 jacoco()
//             }
//         }

        stage('Build Docker Image') {
            steps {
                script {
                   if (isUnix()) {
                        sh "docker build -t $DOCKER_IMAGE:$DOCKER_TAG ."
                    } else {
                        bat "docker build -t $DOCKER_IMAGE:$DOCKER_TAG ."
                    }
                }
            }
        }

//         stage('Push Docker Image') {
//             steps {
//                 script {
//                     withCredentials([string(credentialsId: env.DOCKERHUB_CREDENTIAL_ID, variable: 'DOCKERHUB_CREDENTIAL')]) {
//                         if (isUnix()) {
//                             sh "echo $DOCKERHUB_CREDENTIAL | docker login -u thaonhi6991 --password-stdin"
//                             sh "docker push $DOCKER_IMAGE:$DOCKER_TAG"
//                         } else {
//                             bat "docker login -u thaonhi6991 -p %DOCKERHUB_CREDENTIAL%"
//                             bat "docker push $DOCKER_IMAGE:$DOCKER_TAG"
//                         }
//                     }
//                 }
//             }
//         }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'dockerhub-credentials') {
                        docker.image(DOCKER_IMAGE).push(DOCKER_TAG)
                    }
                }
            }
        }
    }

//     post {
//         always {
//             junit '**/target/surefire-reports/*.xml'
//             jacoco execPattern: '**/target/jacoco.exec'
//         }
//     }
}
