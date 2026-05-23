package com.lomeone.moyemap.entity

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class VenueTagsConverter : AttributeConverter<List<String>, String> {
    override fun convertToDatabaseColumn(attribute: List<String>?): String {
        if (attribute.isNullOrEmpty()) return "[]"
        val escaped = attribute.map { it.replace("\\", "\\\\").replace("\"", "\\\"") }
        return escaped.joinToString(separator = "\",\"", prefix = "[\"", postfix = "\"]")
    }

    override fun convertToEntityAttribute(dbData: String?): List<String> {
        if (dbData.isNullOrBlank() || dbData.trim() == "[]") return emptyList()
        return dbData.trim()
            .removePrefix("[").removeSuffix("]")
            .split(",")
            .map { it.trim().removePrefix("\"").removeSuffix("\"") }
            .filter { it.isNotBlank() }
    }
}
