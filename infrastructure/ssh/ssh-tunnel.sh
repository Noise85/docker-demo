# this command will create a tunnel from localhost:23750 to the ec2 instance:2375
# this is useful to connect to the docker daemon running on the ec2 instance
# You can setup the tunnel also by configuring intellij to do so
ssh -i "/c/Users/EneaBettè/Desktop/ssh-pem.pem" -L 23750:localhost:2375 admin@16.62.2.41