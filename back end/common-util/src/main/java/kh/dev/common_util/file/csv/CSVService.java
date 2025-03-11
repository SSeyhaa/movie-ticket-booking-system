package kh.dev.common_util.file.csv;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CSVService<O, C> {

  private final CSVMapper<O, C> csvMapper;

  public Set<O> parseCSV(InputStream stream, Class<C> clazz) throws IOException {

    try (Reader reader = new BufferedReader(new InputStreamReader(stream))) {
      HeaderColumnNameMappingStrategy<C> strategy = new HeaderColumnNameMappingStrategy<>();
      strategy.setType(clazz);
      CsvToBean<C> csvToBean =
          new CsvToBeanBuilder<C>(reader)
              .withMappingStrategy(strategy)
              .withIgnoreEmptyLine(true)
              .withIgnoreLeadingWhiteSpace(true)
              .build();
      return csvToBean.parse().stream().map(csvMapper::mapTo).collect(Collectors.toSet());
    }
  }
}
