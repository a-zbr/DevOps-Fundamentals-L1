resource "aws_route53_record" "my_www" {
  zone_id = "Z052271931TQSMRBBBNF"
  name    = "my-webserver.zbr-it.pp.ua"
  type    = "A"

  alias {
    name                   = aws_lb.alb.dns_name
    zone_id                = aws_lb.alb.zone_id
    evaluate_target_health = true
  }
}