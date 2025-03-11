package kh.dev.movie_service.config;

import kh.dev.common_util.file.csv.CSVService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@Import(CSVService.class)
public class AppConfig {}
