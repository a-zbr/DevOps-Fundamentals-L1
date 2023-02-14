terraform {
  backend "s3" {
    bucket  = "my-tf-project"
    key     = "final-project-tf-state/terraform.tfstate"
    region  = "eu-central-1"
    encrypt = true
  }
}