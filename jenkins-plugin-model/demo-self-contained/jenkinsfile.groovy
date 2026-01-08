pipeline {
    agent any

    stages {
        stage('Install .NET 7 SDK') {
            steps {
                sh 'curl -sSL https://dot.net/v1/dotnet-install.sh | bash /dev/stdin --channel 7.0'
            }
        }

        stage('Install and Configure Docker') {
            steps {
                sh '''
                    sudo apt-get update -y
                    curl -fsSL https://get.docker.com -o get-docker.sh
                    sudo sh get-docker.sh
                    sudo systemctl start docker
                    sudo systemctl enable docker

                    # Add jenkins user to the docker group.
                    # IMPORTANT: The Jenkins agent process must be restarted for this group change to take effect.
                    # Without a restart, 'docker' commands in subsequent pipelines will still require 'sudo'.
                    sudo usermod -aG docker jenkins

                    # Verify docker installation
                    sudo docker --version
                '''
            }
        }
    }
}
