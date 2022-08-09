package com.crypto.controller;

import com.crypto.model.CryptoType;
import com.crypto.model.RequestType;
import com.crypto.model.Values;
import com.crypto.service.CryptoService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/crypto")
@Api(value = "Crypto Controller")
public class CryptoController {

    private final CryptoService cryptoService;
    private final Bucket bucket;

    public CryptoController(CryptoService cryptoService){
        Bandwidth limit = Bandwidth.classic(20, Refill.greedy(20, Duration.ofMinutes(1)));
        this.bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
        this.cryptoService = cryptoService;
    }

    @ApiOperation(httpMethod = "GET", value = "Returns cryptos by type", notes = "description")
    @GetMapping("/{cryptoType}")
    public List<Values> cryptoByType(@PathVariable String cryptoType){
        return cryptoService.getAllValuesByCryptoType(CryptoType.valueOf(cryptoType));
    }

    @ApiOperation(httpMethod = "GET", value = "Returns all cryptos", notes = "description")
    @GetMapping("/all")
    public List<Values> allCrypto(){
        return cryptoService.getSortedValues();
    }

    @ApiOperation(httpMethod = "GET", value = "Returns all prices", notes = "description")
    @GetMapping("/prices")
    public List<Double> allPrices(){
        return cryptoService.getAllPrices();
    }

    @ApiOperation(httpMethod = "GET", value = "Returns all cryptos for a specific day", notes = "description")
    @GetMapping("/dates")
    public List<Values> cryptoForDate(@RequestParam String date){
        return cryptoService.getCryptosByDate(date);
    }

    @ApiOperation(httpMethod = "GET", value = "Returns specific cryptos for a specific day", notes = "description")
    @GetMapping("/{cryptoType}/dates")
    public List<Values> dates(@RequestParam String date, @PathVariable String cryptoType){
        return cryptoService.getSpecificCryptoByDate(CryptoType.valueOf(cryptoType), date);
    }

    @ApiOperation(httpMethod = "GET", value = "Returns cryptos over a period of time", notes = "description")
    @GetMapping("/range")
    public List<Values> range(@RequestParam String start, @RequestParam String end){
        return cryptoService.getValuesOfDateRange(start, end);
    }

    @ApiOperation(httpMethod = "GET", value = "Returns max/min/newest/oldest crypto of a specific type", notes = "description")
    @GetMapping("/{cryptoType}/request/{requestType}")
    public Values requestedValue(@PathVariable String cryptoType, @PathVariable String requestType){
        return cryptoService.getRequestedValue(CryptoType.valueOf(cryptoType), RequestType.valueOf(requestType));
    }

    @ApiOperation(httpMethod = "GET", value = "Returns normalized score of a specific crypto type", notes = "description")
    @GetMapping("/normalized/{cryptoType}")
    public Double normalizedScore(@PathVariable String cryptoType){
        return cryptoService.getNormalizedScore(CryptoType.valueOf(cryptoType));
    }

    @ApiOperation(httpMethod = "GET", value = "Returns normalized score of a specific crypto type of a specific day", notes = "description")
    @GetMapping("/normalized/{cryptoType}/dates")
    public Double normalizedScoreOfDate(@PathVariable String cryptoType,@RequestParam String date){
        return cryptoService.getNormalizedScoreOfSpecificDate(CryptoType.valueOf(cryptoType), date);
    }





}
