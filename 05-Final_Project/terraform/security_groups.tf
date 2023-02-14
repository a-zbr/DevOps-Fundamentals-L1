resource "aws_security_group" "sg_public" {
  name        = "public-sg"
  description = "open 22, 8080"
  vpc_id      = aws_vpc.vpc.id

  ingress {
    description = "open ssh port"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "open jenkins port"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = [aws_subnet.private_subnet.cidr_block]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "sg-public-fin"
  }
}

resource "aws_security_group" "sg_private" {
  name        = "private-sg"
  description = "open 22"
  vpc_id      = aws_vpc.vpc.id

  ingress {
    description = "open ssh port"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [aws_subnet.public_subnet.cidr_block]
    # security_groups = [aws_security_group.sg_public.id]
  }

  ingress {
    description     = "open for alb"
    from_port       = 80
    to_port         = 80
    protocol        = "tcp"
    security_groups = [aws_security_group.sg_alb.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "sg-private-fin"
  }
}

resource "aws_security_group" "sg_alb" {
  name        = "alb-sg"
  description = "open 80"
  vpc_id      = aws_vpc.vpc.id

  ingress {
    description = "open for alb"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    description = "open for alb"
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "sg-for-alb-fin"
  }
}