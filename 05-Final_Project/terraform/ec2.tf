data "aws_ami" "amazon_linux_latest" {
  most_recent = true
  owners      = ["137112412989"]
  filter {
    name   = "name"
    values = ["amzn2-ami-kernel-*-x86_64-gp2"]
  }
}

resource "aws_instance" "nat_instance" {
  ami                    = data.aws_ami.amazon_linux_latest.id
  instance_type          = "t2.micro"
  subnet_id              = aws_subnet.public_subnet.id
  vpc_security_group_ids = [aws_security_group.sg_public.id]
  user_data              = file("files/nat_instance-user_data.sh")
  key_name               = aws_key_pair.main_key.key_name
  source_dest_check      = false

  tags = {
    Name = "nat-instance-fin"
  }
}

resource "aws_instance" "jenkins" {
  ami                    = data.aws_ami.amazon_linux_latest.id
  instance_type          = "t2.micro"
  subnet_id              = aws_subnet.public_subnet.id
  vpc_security_group_ids = [aws_security_group.sg_public.id]
  user_data              = file("files/jenkins-user_data.sh")
  key_name               = aws_key_pair.main_key.key_name

  tags = {
    Name = "jenkins-fin"
  }
}

resource "aws_instance" "web_server" {
  ami                    = data.aws_ami.amazon_linux_latest.id
  instance_type          = "t2.micro"
  subnet_id              = aws_subnet.private_subnet.id
  vpc_security_group_ids = [aws_security_group.sg_private.id]
  user_data              = file("files/web_server-user_data.sh")
  key_name               = aws_key_pair.main_key.key_name

  tags = {
    Name = "web-server-fin"
  }
}