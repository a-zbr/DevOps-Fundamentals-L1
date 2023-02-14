resource "aws_lb" "alb" {
  name               = "alb-fin"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.sg_alb.id]
  subnets            = [aws_subnet.public_subnet.id, aws_subnet.private_subnet.id]
}

resource "aws_lb_target_group" "target_group_alb" {
  name     = "target-group-alb"
  port     = 80
  protocol = "HTTP"
  vpc_id   = aws_vpc.vpc.id
}

resource "aws_lb_listener" "listener_alb_80" {
  load_balancer_arn = aws_lb.alb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.target_group_alb.arn
  }
}

resource "aws_lb_listener" "listener_alb_443" {
  load_balancer_arn = aws_lb.alb.arn
  port              = 443
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-2016-08"
  certificate_arn   = "arn:aws:acm:eu-central-1:423234818320:certificate/3fe808a3-eda0-4b9a-80e7-a770e308a1df"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.target_group_alb.arn
  }
}

resource "aws_lb_target_group_attachment" "target_group_attachment" {
  target_group_arn = aws_lb_target_group.target_group_alb.arn
  target_id        = aws_instance.web_server.id
  port             = 80
}