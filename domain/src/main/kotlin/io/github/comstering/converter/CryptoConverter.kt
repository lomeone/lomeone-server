package io.github.comstering.converter

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
interface CryptoConverter : AttributeConverter<String,String>
