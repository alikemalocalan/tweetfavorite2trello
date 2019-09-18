package com.github.alikemalocalan

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

object OrganizeTweetApp {
  def main(args: Array[String]) : Unit = SpringApplication.run(classOf[OrganizeTweetApp], args :_ *)
}

@SpringBootApplication
class OrganizeTweetApp {}
