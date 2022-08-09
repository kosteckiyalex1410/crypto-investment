package com.crypto.service;

import com.crypto.model.CryptoType;
import com.crypto.model.RequestType;
import com.crypto.model.Values;
import com.crypto.parser.CsvParser;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log
public class CryptoService {

    private final CsvParser parser;
    private final String DATE_PATTERN = "yyyy-MM-dd";

    public List<Values> getAllValues(){
        return parser.getAllValues();
    }

    public List<Values> getAllValuesByCryptoType(CryptoType type){
        return parser.parseCsvFile(type);
    }

    public List<Double> getAllPrices(){
        return parser.getAllValues().stream().map(Values::getPrice).collect(Collectors.toList());
    }

    public List<Values> getCryptosByDate(String date){
        return parser.getAllValues().stream()
                .filter(values -> {
                    Date timestamp = values.getTimestamp();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
                    return date.equals(simpleDateFormat.format(timestamp));
                }).collect(Collectors.toList());
    }

    public List<Values> getSpecificCryptoByDate(CryptoType type, String date){
        return getAllValuesByCryptoType(type).stream()
                .filter(values -> {
                    Date timestamp = values.getTimestamp();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
                    return date.equals(simpleDateFormat.format(timestamp));
                }).collect(Collectors.toList());
    }

    @SneakyThrows
    public List<Values> getValuesOfDateRange(String startDate, String endDate){
        Date start = new SimpleDateFormat(DATE_PATTERN).parse(startDate);
        Date end = new SimpleDateFormat(DATE_PATTERN).parse(endDate);
        return getAllValues().stream()
                .filter(v -> v.getTimestamp().after(start) && v.getTimestamp().before(end))
                .collect(Collectors.toList());
    }

    public List<Values> getSortedValues(){
        return parser.getAllValues().stream().sorted(Comparator.comparing(Values::getPrice).reversed()).collect(Collectors.toList());
    }

    public Double getNormalizedScore(CryptoType type){
        Values minValue = calculateMinValues(type);
        Values maxValue = calculateMaxValues(type);
        return  (maxValue.getPrice() - minValue.getPrice()) / minValue.getPrice();
    }
    public Double getNormalizedScoreOfSpecificDate(CryptoType type, String date){
        List<Values> values = getSpecificCryptoByDate(type, date);
        Optional<Values> max = values.stream().max(Comparator.comparing(Values::getPrice));
        Optional<Values> min = values.stream().min(Comparator.comparing(Values::getPrice));
        return (max.get().getPrice() - min.get().getPrice())/ min.get().getPrice();
    }

    public Values getRequestedValue(CryptoType cryptoType, RequestType requestType){

        switch (requestType){
            case max: return calculateMaxValues(cryptoType);
            case  min: return calculateMinValues(cryptoType);
            case newest: return getNewestCrypto(cryptoType);
            case oldest: return getOldestCrypto(cryptoType);
            default:
                throw new IllegalArgumentException("Undefined request type");
        }
    }

    private Values getOldestCrypto(CryptoType type){
        return getAllValuesByCryptoType(type).stream().max(Comparator.comparing(Values :: getTimestamp)).orElse(null);
    }

    private Values getNewestCrypto(CryptoType type){
        return getAllValuesByCryptoType(type).stream().min(Comparator.comparing(Values :: getTimestamp)).orElse(null);
    }

    private Values calculateMaxValues(CryptoType type){
        return parser.parseCsvFile(type).stream()
                .max(Comparator.comparing(Values::getPrice)).orElse(null);
    }

    private Values calculateMinValues(CryptoType type){
        return parser.parseCsvFile(type).stream()
                .min(Comparator.comparing(Values::getPrice)).orElse(null);
    }

}
