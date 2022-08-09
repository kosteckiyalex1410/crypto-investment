package com.crypto.parser;

import com.crypto.model.CryptoType;
import com.crypto.model.CsvValues;
import com.crypto.model.Values;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvParser {

    @SneakyThrows
    public List<Values> parseCsvFile(CryptoType type) {
        File file = new File(this.getClass().getClassLoader().getResource(type.fileName()).getFile());
        CsvToBeanBuilder csvToBeanBuilder = new CsvToBeanBuilder(new FileReader(file));
        List<CsvValues> values = csvToBeanBuilder.withType(CsvValues.class).build().parse();
        return values.stream().map(v -> Values.builder()
                .price(v.getPrice())
                .symbol(v.getSymbol())
                .timestamp(new Date(Long.parseLong(v.getTimestamp())))
                .build()).collect(Collectors.toList());

    }

    public List<Values> getAllValues(){
        return Arrays.stream(CryptoType.values())
                .map(this::parseCsvFile)
                .flatMap(Collection::stream).collect(Collectors.toList());
    }
}
