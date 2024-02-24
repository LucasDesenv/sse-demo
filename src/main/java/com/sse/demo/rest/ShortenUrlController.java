package com.sse.demo.rest;

import com.sse.demo.service.ShortenUrlService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shorten-url")
@AllArgsConstructor
public class ShortenUrlController {

  private ShortenUrlService shortenService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public String shortUrl(String url){
    return shortenService.shortUrl(url);
  }

  @GetMapping("/{url}")
  public String decode(@PathVariable String url){
    return shortenService.decode(url);
  }
}
