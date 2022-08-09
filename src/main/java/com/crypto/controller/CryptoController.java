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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @ApiOperation(httpMethod = "GET", value = "Returns all cryptos for a specific day", notes = "description")
    @GetMapping("/all")
    public List<Values> cryptoForDate(@RequestParam Optional<String> forDate){
        return forDate.isPresent() ? cryptoService.getCryptosByDate(forDate.get()) : cryptoService.getAllValues();
    }

    @ApiOperation(httpMethod = "GET", value = "Returns specific cryptos for a specific day", notes = "description")
    @GetMapping("/{cryptoType}")
    public List<Values> byType(@RequestParam Optional<String> forDate, @PathVariable String cryptoType){
        return forDate.isPresent() ?
                cryptoService.getSpecificCryptoByDate(CryptoType.fromString(cryptoType), forDate.get()) :
                cryptoService.getAllValuesByCryptoType(CryptoType.fromString(cryptoType));
    }

    @ApiOperation(httpMethod = "GET", value = "Returns max/min/newest/oldest crypto of a specific type", notes = "description")
    @GetMapping("/{cryptoType}/request/{requestType}")
    public Values requestedValue(@PathVariable String cryptoType, @PathVariable String requestType){
        return cryptoService.getRequestedValue(CryptoType.fromString(cryptoType), RequestType.fromString(requestType));
    }

    @ApiOperation(httpMethod = "GET", value = "Returns range of normalized data", notes = "description")
    @GetMapping("/normalized")
    public Map<String, Double> normalizedScoreOfDate(@RequestParam Optional<String> date){
        return date.isPresent() ? cryptoService.getNormalizedValuesForSpecificDay(date.get()) : cryptoService.getNormalizedValues();
    }

    @ApiOperation(httpMethod = "GET", value = "Returns cryptos over a period of time", notes = "description")
    @GetMapping("/period")
    public List<Values> range(@RequestParam String start, @RequestParam String end){
        return cryptoService.getValuesOfDateRange(start, end);
    }

    @ApiOperation(httpMethod = "GET", value = "Returns all prices", notes = "description")
    @GetMapping("/prices")
    public List<Double> allPrices(){
        return cryptoService.getAllPrices();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(
            Exception exception
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

}
