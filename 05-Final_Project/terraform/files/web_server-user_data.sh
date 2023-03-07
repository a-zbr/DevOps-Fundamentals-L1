#!/bin/bash
sudo yum -y update
sudo yum -y install httpd
myip=`curl http://169.254.169.254/latest/meta-data/local-ipv4`
sudo echo "<html><body bgcolor=gray><center><h1>WebServer with IP $myip</h1><br>Build by Terraform using external script</center></body></html>" > /var/www/html/index.html
sudo service httpd start
sudo chkconfig httpd on

# install openjdk11
sudo amazon-linux-extras install java-openjdk11 -y

# install git
sudo yum install git -y

# create jenkins user with correct permissions
sudo useradd -m -s /bin/bash jenkins
sudo usermod -aG wheel jenkins
sudo usermod -aG docker jenkins

# install hadolint
sudo wget -O /bin/hadolint https://github.com/hadolint/hadolint/releases/download/v2.12.0/hadolint-Linux-x86_64
sudo chmod +x /bin/hadolint

# install docker
sudo yum -y install docker
sudo service docker start
sudo usermod -a -G docker ec2-user
sudo chkconfig docker on
sudo reboot