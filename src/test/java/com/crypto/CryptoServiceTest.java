package com.crypto;

import com.crypto.model.CryptoType;
import com.crypto.model.RequestType;
import com.crypto.service.CryptoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CryptoServiceTest {

    @Autowired
    private CryptoService cryptoService;

    @Test
    public void testOldestValue(){
        assertThat(cryptoService.getRequestedValue(CryptoType.DOGE, RequestType.oldest).getPrice())
                .isEqualTo(0.1415);
    }

    @Test
    public void testNewestValue(){
        assertThat(cryptoService.getRequestedValue(CryptoType.DOGE, RequestType.newest).getPrice())
                .isEqualTo(0.1702);
    }

    @Test
    public void testMinValue(){
        assertThat(cryptoService.getRequestedValue(CryptoType.DOGE, RequestType.min).getPrice())
                .isEqualTo(0.129);
    }

    @Test
    public void testMaxValue(){
        assertThat(cryptoService.getRequestedValue(CryptoType.DOGE, RequestType.max).getPrice())
                .isEqualTo(0.1941);
    }

    @Test
    public void testNormalizedScore(){
        assertThat(cryptoService.getNormalizedScore(CryptoType.DOGE))
                .isEqualTo(0.5046511627906975);
    }

    @Test
    public void testNormalizedScoreSpecificDate(){
        assertThat(cryptoService.getNormalizedScoreOfSpecificDate(CryptoType.BTC, "2022-01-01"))
                .isEqualTo(0.007065740631757662);
    }



}
