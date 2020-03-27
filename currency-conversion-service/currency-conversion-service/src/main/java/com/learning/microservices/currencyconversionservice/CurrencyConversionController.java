package com.learning.microservices.currencyconversionservice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CurrencyConversionProxy currencyConversionProxy;
	
	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean retrieveCurrencyConversionBean(@PathVariable String from,
			@PathVariable String to, @PathVariable BigDecimal quantity) {
		Map<String,String> uriVariable = new HashMap<>();
		uriVariable.put("from", from);
		uriVariable.put("to", to);
		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/currency-exchange/from/{from}/to/{to}",
				CurrencyConversionBean.class, uriVariable);
		
		CurrencyConversionBean currencyConversionBean = responseEntity.getBody();
		return new CurrencyConversionBean(currencyConversionBean.getId(), from, to, 
				currencyConversionBean.getConversionMultiple(),
				quantity, quantity.multiply(currencyConversionBean.getConversionMultiple()), 
				currencyConversionBean.getPort());
	}
	
	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean retrieveCurrencyConversionBeanFeign(@PathVariable String from,
			@PathVariable String to, @PathVariable BigDecimal quantity) {
		
		CurrencyConversionBean currencyConversionBean = currencyConversionProxy.retrieveExchangeValue(from, to);
		logger.info("{}",currencyConversionBean);

		return new CurrencyConversionBean(currencyConversionBean.getId(), from, to, 
				currencyConversionBean.getConversionMultiple(),
				quantity, quantity.multiply(currencyConversionBean.getConversionMultiple()), 
				currencyConversionBean.getPort());
	}
}
