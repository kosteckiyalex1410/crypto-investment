package com.crypto;

import com.crypto.model.CryptoType;
import com.crypto.parser.CsvParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CsvParserTest {

    @Autowired private CsvParser csvParser;

    @Test
    public void testParseAllCsv(){
        assertThat(csvParser.getAllValues()).isNotEmpty();
    }

    @Test
    public void testParseCsvByType(){
        assertThat(csvParser.parseCsvFile(CryptoType.ETH)).isNotEmpty();
    }

}
