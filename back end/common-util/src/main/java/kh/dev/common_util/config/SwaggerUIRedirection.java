package kh.dev.common_util.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerUIRedirection {

  @GetMapping
  public String redirectToSwaggerUI() {
    return "redirect:/swagger-ui/index.html";
  }
}
