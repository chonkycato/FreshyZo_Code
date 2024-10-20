package com.example.freshyzo.helper

class StringManipulation() {

    fun capitalizeWords(input: String): String {
        return input.split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }

    fun removeStringAfterDelimiter(input: String, delimiter: String): String{
        return input.substringBefore(delimiter)
    }

}