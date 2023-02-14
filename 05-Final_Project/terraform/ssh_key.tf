resource "tls_private_key" "generated_rsa_key" {
  algorithm = "RSA"
}

resource "aws_key_pair" "main_key" {
  key_name   = "main-key-fin"
  public_key = tls_private_key.generated_rsa_key.public_key_openssh
}

resource "null_resource" "main" {
  provisioner "local-exec" {
    command     = "echo \"${tls_private_key.generated_rsa_key.private_key_pem}\" > main-key-fin.pem"
    interpreter = ["bash", "-c"]
  }

  provisioner "local-exec" {
    command = "chmod 600 main-key-fin.pem"
  }
}