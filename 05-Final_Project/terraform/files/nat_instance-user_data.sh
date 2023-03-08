#!/bin/bash
sudo sysctl -w net.ipv4.ip_forward=1
sudo /sbin/iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE
sudo yum install -y iptables-services
sudo service iptables save

echo 'sudo sysctl -w net.ipv4.ip_forward=1
sudo /sbin/iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE
sudo yum install -y iptables-services
sudo service iptables save' | sudo tee -a /etc/rc.local

sudo chmod +x /etc/rc.d/rc.local