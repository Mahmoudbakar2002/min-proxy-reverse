package com.bakar.miniproxy;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@SpringBootApplication
@RestController
@Slf4j
public class MiniProxyApplication {
    public  static final String BASE_URL = "https://population.un.org/dataportalapi/";


    public static void main(String[] args) {
        SpringApplication.run(MiniProxyApplication.class, args);
    }


    @GetMapping("/**")
    Object hello(HttpServletRequest request){
        try{
            String url = BASE_URL + request.getRequestURI();
            if(request.getQueryString() != null){
                url += "?" + request.getQueryString();
            }

            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
            headers.add(HttpHeaders.AUTHORIZATION,"Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6Im1haG1vdWRhdGVmLmNvZGVyQGdtYWlsLmNvbSIsIm5iZiI6MTcxNTg3NTgwMiwiZXhwIjoxNzQ3NDExODAyLCJpYXQiOjE3MTU4NzU4MDIsImlzcyI6ImRvdG5ldC11c2VyLWp3dHMiLCJhdWQiOiJkYXRhLXBvcnRhbC1hcGkifQ.lHFklmDg4UlPYLi2q2_KED0ifxlUtfWkj8uNloIYrdE");
            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
            ResponseEntity<?> result =
                    restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);

            log.info("GET-REQUEST URL: " + url);
            return result.getBody();
        }catch (Exception ex){
            log.error("ERROR Happen:{}", ex.getMessage());
            log.error("{}",ex.toString());
            return "ERROR";
        }
    }
}
